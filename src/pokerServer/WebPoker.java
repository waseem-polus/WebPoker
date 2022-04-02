package pokerServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;

import java.util.HashMap;
import poker.Match;
import poker.Player;

public class WebPoker extends WebSocketServer {

    private HashMap<String, Room> rooms;
    private HashMap<Integer, Player> players;
    private int nextID;

    public WebPoker(int numPlayers) {
        this.rooms = new HashMap<>();
        this.players = new HashMap<>();
        this.nextID = numPlayers;
    }

    public WebPoker(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public WebPoker(InetSocketAddress address) {
        super(address);
    }

    public WebPoker(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    /* Author: Minh Trinh
     * Last Update: 4/1/2022
     *
     * onOpen(): creates a new player and passes in the ID
     *
     * Return:
     *
     * Parameters:
     *      WebSocket conn: websocket connection between the client and server
     *      (but mainly being used for debugging here)
     */
    @Override
    public void onOpen(WebSocket conn, CLientHandshake handshake) {

        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

        Player player = new Player(nextID);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has closed");

        int idx = conn.getAttachment();
        // synchronized (mutex) {

        //     //rooms.get(removePlayer(idx));

        //     //System.out.println("removed player index " + idx);

        //     broadcast(this);

        // }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Gson gson = new Gson();
        UserEvent evt = gson.FromJSON(message, UserEvent.class);

        processMessage(evt);

        String jsonGameState = gson.toJson(this);

        broadcast(jsonGameState);
    }

    public void processMessage(UserEvent evt) {

        return;
        // switch(evt) {
        //     case RAISE:

        //     case CALL:
        //         break;
        //     case CHECK:
        //         break;
        //     case FOLD:
        //         break; 
        //     case CREATE_ROOM:
        //         rooms.put(evt.playerID, new Room(players.get(evt.playerID)));
        //         break;
        //     case JOIN_ROOM:
        //         rooms.get(evt.roomID).addPlayer(players.get(evt.playerID));
        //         break;
        //     case START_MATCH:
        //         rooms.get(evt.roomID).match.onStartMatch();
        //         break;
            //case EXCHANGE_CARDS:
            //case RESTART_GAME:

        //}

    } 
/*
    public class upDate extends TimerTask {
        //@Override
        //public void run
    }

    @Override
    public void onEvent(UserEvent evt) {

    }
*/
}