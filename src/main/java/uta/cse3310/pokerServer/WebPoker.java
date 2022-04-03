package main.java.uta.cse3310.pokerServer;

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

import main.java.uta.cse3310.poker.Match;
import main.java.uta.cse3310.poker.Player;

public class WebPoker extends WebSocketServer {

    private HashMap<String, Room> rooms;
    private HashMap<Integer, Player> players;
    private int nextID;

    public static Object mutex;

    public WebPoker(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.rooms = new HashMap<>();
        this.players = new HashMap<>();
        this.nextID = 0;
        this.mutex = new Object();
    }

    public WebPoker(InetSocketAddress address) {
        super(address);
    }

    public WebPoker(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    /*
     * Author: Minh Trinh
     * Last Update: 4/1/2022
     *
     * onOpen(): creates a new player and passes in the ID
     *
     * Return:
     *
     * Parameters:
     * WebSocket conn: websocket connection between the client and server
     * (but mainly being used for debugging here)
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

        Player player = new Player(nextID);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has closed");

        int idx = conn.getAttachment();
        // synchronized (mutex) {

        // //rooms.get(removePlayer(idx));

        // //System.out.println("removed player index " + idx);

        // broadcast(this);

        // }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Gson gson = new Gson();
        UserEvent evt = gson.fromJson(message, UserEvent.class);

        processMessage(evt);

        String jsonGameState = gson.toJson(this);

        broadcast(jsonGameState);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        synchronized (mutex) {
            broadcast(message.array());
        }
        System.out.println(conn + ": " + message);
    }

    public void processMessage(UserEvent evt) {

        return;
        // switch(evt) {
        // case RAISE:
        // break;
        // case CALL:
        // break;
        // case CHECK:
        // break;
        // case FOLD:
        // break;
        // case CREATE_ROOM:
        // rooms.put(evt.playerID, new Room(players.get(evt.playerID)));
        // break;
        // case JOIN_ROOM:
        // rooms.get(evt.roomID).addPlayer(players.get(evt.playerID));
        // break;
        // case START_MATCH:
        // rooms.get(evt.roomID).match.onStartMatch();
        // break;
        // case EXCHANGE_CARDS:
        // case RESTART_GAME:

        // }

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
    /*
     * public class upDate extends TimerTask {
     * 
     * @Override
     * public void run
     * }
     * 
     * @Override
     * public void onEvent(UserEvent evt) {
     * 
     * }
     */
}