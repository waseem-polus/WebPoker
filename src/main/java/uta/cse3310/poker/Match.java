package uta.cse3310.poker;

import java.util.ArrayList;
import java.util.Collections;

import uta.cse3310.cards.CardDeck;
import uta.cse3310.pokerServer.UserEvent;

public class Match {
    private transient CardDeck deck;
    private transient ArrayList<Player> activePlayers;
    private transient int numOfStartingPlayers;
    private transient int turnsInRound;
    private transient int totalTurns;
    private double bettingPool;
    private MatchRound round;
    private int currentPlayerID;
    private int[] winnerID;
    private String action;
    private double minimumBet;

    public Match() {
        this.deck = new CardDeck();
        this.bettingPool = 0;
        this.round = MatchRound.WAITING;
        this.activePlayers = new ArrayList<>();
        this.numOfStartingPlayers = 0;
        this.turnsInRound = 0;
        this.totalTurns = 0;
        this.currentPlayerID = 0;
        this.action = new String();
        this.minimumBet = 0;

        this.winnerID = new int[1];
        this.winnerID[0] = -1;
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/12/2022
     * 
     * nextTurn(): sets the current player's turn to the next player in the
     * arraylist. Moves to next round if needed.
     */
    public void nextTurn(int nextID) {
        turnsInRound++;
        totalTurns++;

        if (nextID != -1) {
            currentPlayerID = nextID;
        }

        // Debugging info for testing
        // System.out.println("[INFO] turns this round = " + turnsInRound + "\n[INFO] Current active players = " + this.activePlayers.size() + "\n[INFO] Num of starting players = " + this.numOfStartingPlayers + "\n[INFO] Next round");

        if (this.round == MatchRound.FIRST_BETTING || this.round == MatchRound.SECOND_BETTING) {
            if (turnsInRound >= this.numOfStartingPlayers && checkBets()) {
                this.round = this.round.next();
                
                this.numOfStartingPlayers = this.activePlayers.size();
                turnsInRound = 0;
                this.minimumBet = 0;
                
                for (Player p : this.activePlayers) {
                    p.clearBet();
                }
            }
        } else if (this.round == MatchRound.DRAWING && turnsInRound >= this.activePlayers.size()) {
            this.round = this.round.next();

            turnsInRound = 0;
            this.numOfStartingPlayers = this.activePlayers.size();
        }

        if ((this.round == MatchRound.SHOWDOWN
                || (this.activePlayers.size() == 1) && this.round != MatchRound.WAITING)) {
            System.out.println("[INFO] Appointing winner... Active Players = " + this.activePlayers.size());
            appointWinner();
        }
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/13/2022
     * 
     * Returns:
     * true if all bets are equal to the minimum bet (unless a player is all out)
     * false if not all bets are equal
     */
    private boolean checkBets() {
        boolean allGood = true;

        for (Player p : this.activePlayers) {
            if (p.getCurrentBet() != minimumBet && p.getBalance() > 0) {
                allGood = false;
            }
        }

        return allGood;
    }

    /*
     * Author: Victor Arowsafe
     * Last Updated: 4/1/2022
     * 
     * addPlayer(player): add the specified player to the active player arraylist
     * 
     * Parameters:
     * Player player: the player to be added from the list
     */
    public void addPlayer(Player player) {
        if (this.round == MatchRound.WAITING) {
            activePlayers.add(player);
            this.action = player.name + " joined the match";
        } else {
            System.out.println("[WARNING] Attempted to add players to match while not waiting.");
        }
    }

    /*
     * Author: Victor Arowsafe, refactored by Waseem Alkasbutrus
     * Last Updated: 4/12/2022, By Waseem Alkasbutrus
     * 
     * removePlayer(playerID): remove the specified player from the active player
     * arraylist
     * 
     * Parameters:
     * int playerID: the id of the player to be removed from the list
     */
    public void removePlayer(int playerID) {
        int nextIndex = getPlayerNextID(playerID);

        Player p = getPlayer(playerID);
        this.activePlayers.remove(p);
        if (this.activePlayers.size() > 0) {
            nextTurn(nextIndex);

            this.action = p.name + " left the match";
        }
    }

    /*
     * Author: Victor Arowsafe,
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     * 
     * onEvent(event): process the specified event
     * 
     * Parameters:
     * UserEvent event: the event to be processed
     */
    public void onEvent(UserEvent event) {
        this.action = "";
        switch (event.event) {
            case EXCHANGE_CARDS:
                Integer length = Integer.parseInt((String) event.msg[0]);
                Integer[] indx = new Integer[length];

                System.out.println("Switch {");
                for (int i = 1; i <= length; i++) {
                    indx[i - 1] = Integer.parseInt((String) event.msg[i]);
                    System.out.println(indx[i - 1]);
                }
                System.out.println("}");

                onExchangeCards(event.playerID, indx);
                break;
            case RAISE:
                Double amount = Double.parseDouble((String) event.msg[0]);
                onRaise(event.playerID, amount);
                break;
            case CALL:
                onCall(event.playerID);
                break;
            case FOLD:
                onFold(event.playerID);
                break;
            case CHECK:
                onCheck(event.playerID);
                break;
            case START_MATCH:
                onStartMatch();
                break;
            default:
                System.out.println("\n[WARNING] Match.onEvent was passed an irrelevant event");
        }
    }

    /*
     * Author: Victor Arowsafe, refactored by Waseem Alkasbutrus
     * Last Updated: 4/3/2022
     *
     * onStartMatch(): sets up the match and gives the leader the first turn.
     */
    public void onStartMatch() {
        if (this.activePlayers.size() > 1) {
            for (Player p : this.activePlayers) {
                this.bettingPool += p.placeBet(20);
                p.clearBet();
                p.dealHand(deck);
            }
            round = MatchRound.FIRST_BETTING;
            currentPlayerID = activePlayers.get(0).id;

            this.numOfStartingPlayers = this.activePlayers.size();
            this.action = "Match started";

            System.out.println("\n[INFO] Match started");
        } else {
            System.out.println("\n[WARNING] Can't start match. Only " + this.activePlayers.size() + " in match");
        }
    }

    /*
     * Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     * 
     * onFold(playerID): The specified player will be removed from the active
     * players arraylist and will not get a turn for the rest of this current match
     * 
     * Parameters:
     * int playerID: the id of the player who wishes to fold
     */
    public void onFold(int playerID) {
        Player p = getPlayer(playerID);

        p.fold();
        removePlayer(playerID);

        this.action = p.name + " folded";
    }

    /*
     * Author: Originally Victor Arowsafe, method reworked from scratch by Waseem
     * Alkasbutrus
     * Last Updated: 4/16/2022
     * 
     * onCheck(playerID): if the specified player's bet is equivalent to the highest
     * current bet, then the turn is passed to the next player
     * 
     * Parameters:
     * int playerID: the id of the player who wishes to check
     */
    public void onCheck(int playerID) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            double highestBet = getHighestBet();
            Player p = getPlayer(playerID);

            if (p.getCurrentBet() >= highestBet || p.getBalance() == 0) {
                this.action = p.name + " checked";
                nextTurn(getPlayerNextID(playerID));
            }
        }
    }

    /*
     * Author: Originally Victor Arowsafe, method reworked from scratch by Waseem
     * Alkasbutrus
     * Last Updated: 4/7/2022
     * 
     * onCall(playerID): increase the current bet of the specified player to match
     * the current highest bet
     * highest bet
     * 
     * Parameters:
     * int playerID: the id of the player who wishes to call
     */
    public void onCall(int playerID) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            double highestBet = getHighestBet();
            Player p = getPlayer(playerID);
            if (p.getCurrentBet() < highestBet) {
                this.bettingPool += p.placeBet(highestBet - p.getCurrentBet());

                this.action = p.name + " called";
                nextTurn(getPlayerNextID(playerID));
            }
        }
    }

    /*
     * Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     * 
     * onRaise(playerID): The specified player will place a bet equivalent to the
     * specified amount
     * 
     * Parameters:
     * int playerID: the id of the player who wishes to fold
     * double amount: the amount to raise bet by
     */
    public void onRaise(int playerID, double amount) {
        Player p = getPlayer(playerID);
        if ((round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING)
                && (amount + p.getCurrentBet() > this.getHighestBet())) {
            this.bettingPool += p.placeBet(amount);
            this.minimumBet = p.getCurrentBet();
            this.action = p.name + " raised to $" + this.minimumBet;
            nextTurn(getPlayerNextID(playerID));
        }
    }

    /*
     * Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     *
     * onExchangeCards(playerID, cardIndex): for the specified player, replace the
     * cards in their hand at the specified indexes
     * 
     * Parameters:
     * playerID: the player whos hand will be edited
     */
    public void onExchangeCards(int playerID, Integer[] cardIndex) {
        Player player = getPlayer(playerID);

        if (player != null) {
            String discardedCards = getPlayer(playerID).exchangeCards(cardIndex, deck);

            this.action = player.name + " exchanged " + discardedCards;
            nextTurn(getPlayerNextID(playerID));
        } else {
            System.out.println("[Error] player does not exist");
        }

    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated 4/4/2022
     * 
     * getWinner(): sets the winner ID as the player with the best hand
     */
    public int[] appointWinner() {
        Collections.sort(this.activePlayers, Collections.reverseOrder());
        System.out.println(this.activePlayers);

        int winnerCount = 1;

        for (int i = 1; (i < this.activePlayers.size()) && (this.activePlayers.size() > 1); i++) {
            Player prev = this.activePlayers.get(i - 1);
            Player current = this.activePlayers.get(i);

            if (prev.hand.equals(current.hand)) {
                winnerCount++;
            }
        }

        this.winnerID = new int[winnerCount + 1];
        this.winnerID[0] = winnerCount;

        this.action = "";
        for (int i = 0; i < winnerCount; i++) {
            this.winnerID[i + 1] = activePlayers.get(i).id;
            activePlayers.get(i).addToBalance(this.bettingPool / winnerCount);

            this.action += activePlayers.get(i).name;
            if (winnerCount - i != 0) {
                this.action += " and ";
            }
        }
        this.action += "won $" + this.bettingPool / winnerCount;

        return this.winnerID;
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     *
     * getHighestBet(): returns the highest bet placed by an active player
     * 
     * Returns:
     * a double that is the highest bet placed by an active player
     */
    private double getHighestBet() {
        double highestBet = 0;

        for (Player p : this.activePlayers) {
            if (highestBet < p.getCurrentBet()) {
                highestBet = p.getCurrentBet();
            }
        }

        return highestBet;
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/18/2022
     * 
     * getPlayer(playerID): returns a player object that matches the specified id
     * 
     * Returns:
     * Player object that matches the specified id
     */
    public Player getPlayer(int playerID) {
        Player player = null;

        for (Player p : this.activePlayers) {
            if (p.id == playerID) {
                player = p;
            }
        }

        return player;
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/27/2022
     * 
     * getPlayer(playerID): returns the id the player following the specified player
     * 
     * Returns:
     * int id of the following player in the active player arraylist
     */
    public int getPlayerNextID(int CurrentPlayerID) {
        int currentIndex = -1;
        int nextIndex;

        currentIndex = this.activePlayers.indexOf(getPlayer(currentPlayerID));

        if (currentIndex >= 0) {
            nextIndex = (currentIndex + 1) % this.activePlayers.size();
            return this.activePlayers.get(nextIndex).id;
        } else {
            System.out.println("[ERROR] attempted to get next player id based on nonexistent player.");
            return -1;
        }
    }

    /*
     * Author: Victor Arowsafe
     * Last Updated: 4/2/2022
     *
     * isWaiting() : returns true if the match is currently waiting to begin
     * 
     * Returns:
     * Boolean value of whether the match has begun or not
     */
    public Boolean isWaiting() {
        return this.round == MatchRound.WAITING;
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/17/2022
     * 
     * setAction(action): sets the action string to match the provided action
     * 
     * Parameters:
     * String action: what the action should be set to
     */
    public void setAction(String action) {
        this.action = action;
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/18/2022
     * 
     * countActivePlayers(): returns the number of players in the activePlayers
     * arraylist
     * 
     * Returns:
     * int count of players in activePlayers
     */
    public int countActivePlayers() {
        return this.activePlayers.size();
    }

    /*
     * Author: Waseem Alkasbutrus
     * Last Updated: 4/18/2022
     * 
     * getCurrentPlayer(): returns the id of the current player
     * 
     * Returns:
     * int id of current player
     */
    public int getCurrentPlayer() {
        return this.currentPlayerID;
    }
}
