package pokerServer;

import java.util.HashMap;
import poker.Match;
import poker.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Room {
    public Match match;
    // room type
    public RoomVisibility visibility;
    // players in the room

    public final int pin;

    private int leaderId;

    private HashMap<Integer, Player> players;

    /*
     * INTIALIZING player who start the game to be the game leader
     * assigning leaderid to be zero
     */

    public Room(Player leader)

    {

        // this. = new HashMap();

        this.pin = leaderId;
        this.match = new Match(2000);

    }

    /*
     * adding new player
     * if room is private user will have same pin
     * player will be added
     * giving players id
     * players
     * 
     * 
     */
    public void addPlayer(Player player) {
        if ((players.size() < 5)) {
            players.put(leaderId, player);
            if (this.match.isWaiting()) {
                this.match.addPlayer(player);
            }
        } else {
            System.out.printf("room is full");
        }

    }
    /*
     * given its player a number
     * delet the player from the number array
     */

    public void removePlayer(int id) {

        players.remove(id);
        this.match.removePlayer(id);
    }
    // setting visibilty public or private

    public void setVisibility(RoomVisibility visibility) {

        this.visibility = visibility;

    }

   
    public boolean update() {

        return false;
    }

    // to get players for this challenge

    // return players
    @Override
    public int hashCode() {

        return pin;

    }

    public boolean equals(Room r) {
        return this.pin == r.pin;

    }

    public void restartMatch() {
        this.match = new Match(2000);
        for (Player p : players.values()) {
            this.match.addPlayer(p);

        }
    }

}