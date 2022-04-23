package uta.cse3310;

import java.nio.ByteBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.print.attribute.AttributeSet;

import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uta.cse3310.poker.Match;
import uta.cse3310.poker.Player;
import uta.cse3310.pokerServer.RoomVisibility;
import uta.cse3310.pokerServer.*;
import uta.cse3310.pokerServer.UserEvent.UserEventType;

public class WebPoker extends WebSocketServer {
    private HashMap<Integer, Room> rooms;
    private HashMap<Integer, Player> players;
    private int nextID;
    private int nextPIN;

    public static Object playersMutex;
    public static Object roomsMutex;

    public WebPoker(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        
        this.rooms = new HashMap<>();
        this.players = new HashMap<>();
        this.nextID = 0;
        this.nextPIN = 0;

        playersMutex = new Object();
        roomsMutex = new Object();
    }

    public WebPoker(InetSocketAddress address) {
        super(address);
    }

    public WebPoker(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    /*
     * Author: Minh Trinh
     * Last Update: 4/2/2022
     * Updated by: Waseem Alkasbutrus
     *
     * onOpen(): creates a new player and passes in the ID
     *
     * Return:
     *
     * Parameters:
     * WebSocket conn: websocket connection between the client and server
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("\n[INFO] " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

        conn.setAttachment(this.nextID);

        Player player = new Player(this.nextID);
        synchronized (playersMutex) {
            this.players.put(this.nextID, player);
        }

        this.nextID++;

        Gson gson = new Gson();

        conn.send(gson.toJson(player));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("\n" + conn + " has closed");

        int attachment = conn.getAttachment();

        Player p = this.players.get(attachment);
        synchronized (playersMutex) {
            if (p.getRoom() != -1) {
                Room room = this.rooms.get(p.getRoom());
                room.removePlayer(attachment);

                if (room.playerCount() <= 0) {
                    synchronized (roomsMutex) {
                        this.rooms.remove(room.pin);
                        System.out.println("\n[INFO] Removed room " + room.pin);
                    }
                } else {
                    broadcast(encodeAsJson(room.pin));
                    System.out.println("\nOn close sent:\n" + encodeAsJson(room.pin));
                }
            } else {
                System.out.println("\n[INFO] Player " + attachment + " wasn't part of a room");
            }
            this.players.remove(attachment);
            System.out.println("\n[INFO] removed player " + attachment + " from server");
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Gson gson = new Gson();

        System.out.println("\nReceived:\n" + message);

        UserEvent evt = gson.fromJson(message, UserEvent.class);

        System.out.println("\n[INFO] User[" + evt.playerID + "] wishes to " + evt.event.toString());
        processMessage(conn, evt);

        if (evt.event == UserEventType.CREATE_ROOM) {
            conn.send(gson.toJson(this.rooms.get(this.nextPIN - 1)));
            System.out.println("\nSent:\n" + gson.toJson(this.rooms.get(this.nextPIN - 1)));
        } else if (evt.event == UserEventType.JOIN_ROOM) {
            broadcast(encodeAsJson(Integer.parseInt((String) evt.msg[0])));
            System.out.println("\nSent:\n" + encodeAsJson(Integer.parseInt((String) evt.msg[0])));
        } else {
            broadcast(encodeAsJson(evt.roomID));
            System.out.println("\nSent:\n" + encodeAsJson(evt.roomID));
        }

        System.out.println("\n[INFO] Active Rooms: " + this.rooms.size());
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        synchronized (playersMutex) {
            broadcast(message.array());
        }
        System.out.println(conn + ": " + message);
    }

    private String encodeAsJson(int roomPin) {
        Gson gson = new Gson();
        return gson.toJson(this.rooms.get(roomPin));
    }

    public void processMessage(WebSocket conn, UserEvent evt) {
        switch (evt.event) {
            case CREATE_ROOM:
                double startingBalance = Double.parseDouble((String) evt.msg[1]);
                Player leader = players.get(evt.playerID);
                leader.setName((String) evt.msg[0]);
                leader.setBalance(startingBalance);

                synchronized (roomsMutex) {
                    this.rooms.put(this.nextPIN,
                            new Room(leader, RoomVisibility.valueOf((String) evt.msg[2]), startingBalance, this.nextPIN));
                    this.nextPIN++;
                }

                System.out.println("\n[INFO] Created room " + (this.nextPIN - 1));
                break;
            case LEAVE_ROOM:
                synchronized (roomsMutex) {
                    Room room = this.rooms.get(evt.roomID);

                    room.removePlayer(evt.playerID);
                    if (room.playerCount() <= 0) {
                        this.rooms.remove(evt.roomID);
                        System.out.println("\n[INFO] Removed room " + evt.roomID);
                    }
                }
                break;
            case RESTART_MATCH:
                this.rooms.get(evt.roomID).restartMatch();
                break;
            case JOIN_ROOM:
                Room room = this.rooms.get(Integer.parseInt((String) evt.msg[0]));
                if (room == null) {

                } else {
                    Player p = this.players.get(evt.playerID);
                    p.setName((String) evt.msg[1]);

                    room.addPlayer(players.get(evt.playerID));
                }
                break;
            default:
                this.rooms.get(evt.roomID).match.onEvent(evt);
                break;
        }

    }

    public class upDate extends TimerTask {

        @Override
        public void run() {
            synchronized (roomsMutex) {
                for (int pin : rooms.keySet()) {
                    if (rooms.get(pin).playerCount() <= 0) {
                        rooms.remove(pin);
                        System.out.println("[CLEANUP] Removed empty room " + pin);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        // create and start the http server

        HttpServer server = new HttpServer(8081, "./html");
        server.start();

        // create and start the websocket server;

        int port = 8881;

        WebPoker web = new WebPoker(port);
        web.start();
        System.out.println("WebPokerServer started on port: " + web.getPort());

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            web.broadcast(in);
            if (in.equals("exit")) {
                web.stop(1000);
                break;
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific
            // websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(1000);
        // once a second call update
        // may want to start this in the main() function??
        new java.util.Timer().scheduleAtFixedRate(new upDate(), 0, 1000);
    }
}