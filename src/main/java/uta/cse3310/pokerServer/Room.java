package main.java.uta.cse3310.pokerServer;

import java.util.HashMap;

import main.java.uta.cse3310.poker.Match;
import main.java.uta.cse3310.poker.Player;

public class Room {
    public Match match;
    public RoomVisibility visibility;
    public final int pin;
    private int leaderId;
    private HashMap<Integer, Player> players;

    public Room(Player leader)

    {
        this.pin = leader.id;
        this.match = new Match(2000);
        this.visibility = RoomVisibility.PRIVATE;
        this.leaderId = leader.id;
        this.players = new HashMap<Integer, Player>();
        this.players.put(leaderId, leader);
    }

    /*
     * Author: Meaza Abera
     * Last Updated: 04/2/2022
     * 
     * 
     * INTIALIZING player who start the game to be the game leader
     * put() : add new player and thier id until they reach 5
     * determine that if we get player size more that five return "room is full"
     * addPLayer(): add player who is waiting in the room
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
     * Author: Meaza Abera
     * Last Updated: 04/2/2022
     * 
     * remove() it removes player from room
     * in order to remove we use the player id
     */
    public void removePlayer(int id) {

        players.remove(id);
        this.match.removePlayer(id);
    }
    /*
     * Author: Meaza Abera
     * Last Updated: 04/2/2022
     * 
     * setting visibility public or private
     */

    public void setVisibility(RoomVisibility visibility) {

        this.visibility = visibility;

    }

    public boolean update() {

        return false;
    }
    /*
     * Author: Meaza Abera
     * Last Updated: 04/2/2022
     * 
     * to get players for this challenge
     * return the pin which is the leaders id.
     */

    @Override
    public int hashCode() {

        return pin;

    }

    @Override
    public boolean equals(final Object r) {
        boolean equals = false;

        if (r.getClass() == this.getClass()) {
            Room room = (Room) r;
            equals = this.pin == room.pin;
        }

        return equals;

    }
    /*
     * Author: Meaza Abera
     * Last Updated: 04/2/2022
     * 
     * restart match will restart the game again
     * by craeting new match
     * add() add new players.
     */

    public void restartMatch() {
        this.match = new Match(2000);
        for (Player p : players.values()) {
            this.match.addPlayer(p);

        }
    }

}