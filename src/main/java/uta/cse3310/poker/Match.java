package uta.cse3310.poker;

import java.util.ArrayList;

import uta.cse3310.cards.CardDeck;
import uta.cse3310.pokerServer.UserEvent;
import uta.cse3310.pokerServer.UserEvent.UserEventType;

public class Match {
    private transient CardDeck deck;
    private transient double startingBalance;
    private transient ArrayList<Player> activePlayers;
    private transient int turnNumber;
    private double bettingPool;
    private MatchRound round;
    private int currentPlayerID;

    public Match() {
        this.deck = new CardDeck();
        this.bettingPool = 0;
        this.startingBalance = 2000;
        this.round = MatchRound.WAITING;
        this.activePlayers = new ArrayList<>();
        this.turnNumber = 0;
        this.currentPlayerID = 0;
    }

    public Match(double playerBalance) {
        this();
        this.startingBalance = playerBalance;
    }

    /* Author: Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     * 
     * nextTurn(): sets the current player's turn to the next player in the arraylist. Moves to next round if needed.
     */
    public void nextTurn() {
        turnNumber++;
        currentPlayerID = activePlayers.get(turnNumber % activePlayers.size()).id;

        if (this.round == MatchRound.FIRST_BETTING || this.round == MatchRound.FIRST_BETTING ) {
            double currentBet = this.activePlayers.get(0).getCurrentBet();
            for (Player p : this.activePlayers) {
                if (p.getCurrentBet() != currentBet) {
                    currentBet = -1;
                }
            }
    
            if (turnNumber >= activePlayers.size() && (currentBet != -1)) {
                this.round = this.round.next();
                clearPlayerBets();
                turnNumber = 0;
            }        
        } else if (this.round == MatchRound.DRAWING && turnNumber == activePlayers.size()) {
            this.round = this.round.next();
            turnNumber = 0;
        }
    }

    private void clearPlayerBets() {
        for (Player p : this.activePlayers) {
            p.clearBet();
        }
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/1/2022
     * 
     * addPlayer(player): add the specified player to the active player arraylist
     * 
     * Parameters:
     *      Player player: the player to be added from the list
     */
    public void addPlayer(Player player) {
        activePlayers.add(player);
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/2/2022
     * 
     * removePlayer(playerID): remove the specified player from the active player arraylist
     * 
     * Parameters:
     *      int playerID: the id of the player to be removed from the list
     */
    public void removePlayer(int playerID) {
        for (int i = 0; i < activePlayers.size(); i++) {
            int checkId = activePlayers.get(i).id;
            if (checkId == playerID) {
                activePlayers.remove(i);
            }
        }
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     * 
     * onEvent(event): process the specidfied event
     * 
     * Parameters:
     *      UserEvent event: the event to be processed
     */
    public void onEvent(UserEvent event) {
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
        }
    }

    /* Author: Victor Arowsafe, refactored by Waseem Alkasbutrus
     * Last Updated: 4/3/2022
     *
     * onStartMatch(): sets up the match and gives the leader the first turn.
     */
    public void onStartMatch() {
        if (this.activePlayers.size() > 1) {
            for (Player p : this.activePlayers) {
                p.setBalance(this.startingBalance);
                this.bettingPool += p.placeBet(20);
                p.dealHand(deck);
                p.clearBet();
            }
    
            round = MatchRound.FIRST_BETTING;
            currentPlayerID = activePlayers.get(0).id;
        }
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     * 
     * onFold(playerID): The specified player will be removed from the active players arraylist and will not get a turn for the rest of this current match
     * 
     * Parameters:
     *      int playerID: the id of the player who wishes to fold
    */
    public void onFold(int playerID) {
        removePlayer(playerID);
        nextTurn();
    }

    /* Author: Originally Victor Arowsafe, method reworked from scratch by Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     * 
     * onCheck(playerID): if the specified player's bet is equivalent to the highest current bet, then the turn is passed to the next player
     * 
     * Parameters:
     *      int playerID: the id of the player who wishes to check
    */
    public void onCheck(int playerID) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            double highestBet = getHighestBet();    

            if (getPlayer(playerID).getCurrentBet() == highestBet) {
                nextTurn();
            }
        }
    }

    /* Author: Originally Victor Arowsafe, method reworked from scratch by Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     * 
     * onCall(playerID): the specified player will place the same bet as the current highest bet
     * 
     * Parameters:
     *      int playerID: the id of the player who wishes to call
    */
    public void onCall(int playerID) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
           double highestBet = getHighestBet();
           getPlayer(playerID).placeBet(highestBet);

           nextTurn();
        }
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     * 
     * onRaise(playerID): The specified player will place a bet equivalent to the specified amount
     * 
     * Parameters:
     *      int playerID: the id of the player who wishes to fold
     *      double amount: the amount to raise bet by
    */
    public void onRaise(int playerID, double amount) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            this.bettingPool += getPlayer(playerID).placeBet(amount);
            nextTurn();
        }
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/4/2022, by Waseem Alkasbutrus
     *
     * onExchangeCards(playerID, cardIndex): for the specified player, replace the cards in their hand at the specified indexes
     * 
     * Parameters:
     *      playerID: the player whos hand will be edited
     */
    public void onExchangeCards(int playerID, Integer[] cardIndex) {
        Player player = getPlayer(playerID);

        if (player != null) {
            getPlayer(playerID).exchangeCards(cardIndex, deck);
        } else {
            System.out.println("[Error] player does not exist");
        }

        nextTurn();
    }

    /* Author: Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     *
     * getHighestBet(): returns the highest bet placed by an active player
     * 
     * Returns:
     *  a double that is the highest bet placed by an active player
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

    /* Author: Waseem Alkasbutrus
     * Last Updated: 4/2/2022
     * 
     * getPlayer(playerID): returns a player object that matches the specified id
     * 
     * Returns:
     *      Player object that matches the specified id
     */
    private Player getPlayer(int playerID) {
        Player player = null;

        for (Player p : this.activePlayers) {
            if (p.id == playerID) {
                player = p;
            }
        }

        return player;
    }

    /* Author: Victor Arowsafe
     * Last Updated: 4/2/2022
     *
     * isWaiting() : returns true if the match is currently waiting to begin
     * 
     * Returns:
     *  Boolean value of whether the match has begun or not
     */
    public Boolean isWaiting() {
        return this.round == MatchRound.WAITING;
    }
}
