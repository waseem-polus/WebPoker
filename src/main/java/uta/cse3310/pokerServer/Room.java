package uta.cse3310.pokerServer;

import java.util.HashMap;

import uta.cse3310.poker.Match;
import uta.cse3310.poker.Player;

public class Room {
    private HashMap<Integer, Player> players;
    private int playerCount;
    public Match match;
    public RoomVisibility visibility;
    public final int pin;
    private int leaderId;
    public final double startingBalance;

    public Room(Player leader, RoomVisibility visibility, double startingBalance, int pin) {
        this.match = new Match();
        this.pin = pin;
        this.visibility = visibility;
        this.startingBalance = startingBalance;
        this.leaderId = leader.id;

        this.players = new HashMap<Integer, Player>();
        addPlayer(leader);
    }

    /*
     * Author: Meaza Abera
     * Last Updated: 04/9/2022
     * 
     * 
     * INITIALIZING player who start the game to be the game leader
     * put() : add new player and their id until they reach 5
     * determine that if we get player size more that five return "room is full"
     * addPLayer(): add player who is waiting in the room
     */
    public void addPlayer(Player player) {
        if (players.size() < 5) {
            players.put(player.id, player);
            player.setRoom(this.pin);
            player.setBalance(this.startingBalance);
            
            this.playerCount = this.players.size();
            
            if (this.match.isWaiting()) {
                this.match.addPlayer(player);
            }

            System.out.println("\n[INFO] Added player " + player.id + " to room " + this.pin);
        } else {
            System.out.printf("\n[WARNING] Attempted adding player to full room");
        }
    }

    /*
     * Author: Meaza Abera
     * Last Updated: 04/18/2022, by Waseem Alkasbutrus
     * 
     * remove() it removes player from room
     * in order to remove we use the player id
     */
    public void removePlayer(int id) {
        this.players.get(id).setRoom(-1);
        this.players.remove(id);
        
        if (this.match.getPlayer(id) != null) {
            this.match.removePlayer(id);
        }
        
        this.playerCount = this.players.size();
        System.out.println("\n[INFO] Removed player " + id + " from room " + this.pin + ". " + this.playerCount + " players left");
        
        if (id == this.leaderId && this.playerCount > 0) {
            for (int key : this.players.keySet()) {
                this.leaderId = key;
                break;
            }
            System.out.println("\n[INFO] Changed leader to player " + leaderId);
        }
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

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 04/9/2022
     * 
     * playerCount(): returns the number of players in this room
     * 
     * Returns:
     * int number of players in room
     */
    public int playerCount() {
        return this.players.size();
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
     * Last Updated: 04/12/2022, by Waseem Alkasbutrus
     * 
     * restart match will restart the game again
     * by creating new match
     * add() add new players.
     */
    public void restartMatch() {
        this.match = new Match();
        for (Player p : players.values()) {
            p.clearBet();
            if (p.getBalance() == 0) {
                p.setBalance(startingBalance);
            }
            this.match.addPlayer(p);
        }
        this.match.setAction("Prepared next match");
    }
}