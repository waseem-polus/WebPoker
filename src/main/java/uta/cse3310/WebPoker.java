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
import uta.cse3310.pokerServer.*;
import uta.cse3310.pokerServer.UserEvent.UserEventType;

public class WebPoker extends WebSocketServer {
    private HashMap<Integer, Room> rooms;
    private HashMap<Integer, Player> players;
    private int nextID;

    public static Object mutex;

    public WebPoker(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.rooms = new HashMap<>();
        this.players = new HashMap<>();
        this.nextID = 0;
        mutex = new Object();
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
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

        conn.setAttachment(this.nextID);

        Player player = new Player(this.nextID);
        this.players.put(this.nextID, player);

        this.nextID++;

        Gson gson = new Gson();

        conn.send(gson.toJson(player));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has closed");

        // int idx = conn.getAttachment();
        // synchronized (mutex) {
        // this.players.remove(idx);

        // System.out.println("removed player index " + idx);

        // // The state is now changed, so every client needs to be informed
        // broadcast(encodeAsJson());
        // System.out.println("the game state" + encodeAsJson());
        // }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Gson gson = new Gson();
        UserEvent evt = gson.fromJson(message, UserEvent.class);

        System.out.println("User[" + evt.playerID + "] wishes to " + evt.event.toString() + "\n");
        processMessage(evt);

        if (evt.event == UserEventType.CREATE_ROOM) {
            conn.setAttachment(evt.playerID);
            conn.send(gson.toJson(this.rooms.get(evt.playerID)));
        } else {
            broadcast(encodeAsJson());
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        synchronized (mutex) {
            broadcast(message.array());
        }
        System.out.println(conn + ": " + message);
    }

    private String encodeAsJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void processMessage(UserEvent evt) {
        switch (evt.event) {
            case START_MATCH:
            case EXCHANGE_CARDS:
            case RAISE:
            case CALL:
            case CHECK:
            case FOLD:
                this.rooms.get(evt.roomID).match.onEvent(evt);;
                break;
            case CREATE_ROOM:
                Player leader = players.get(evt.playerID);
                leader.setName((String) evt.msg[0]);
                this.rooms.put(evt.playerID, new Room(leader, (RoomVisibility) evt.msg[2], (Double) evt.msg[1]));
                break;
            case RESTART_MATCH:
                this.rooms.get(evt.roomID).restartMatch();
                break;
            case JOIN_ROOM:
                Room room = this.rooms.get(evt.roomID);
                if (room == null) {

                } else {
                    room.addPlayer(players.get(evt.playerID));
                }
                break;
        }

    }

    public class upDate extends TimerTask {

        @Override
        public void run() {

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
        setConnectionLostTimeout(100);
        // once a second call update
        // may want to start this in the main() function??
        new java.util.Timer().scheduleAtFixedRate(new upDate(), 0, 1000);
    }
}