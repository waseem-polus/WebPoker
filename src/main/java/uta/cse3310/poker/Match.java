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

    public enum MatchRound {
        WAITING, FIRST_BETTING, DRAWING, SECOND_BETTING, SHOWDOWN;
    }

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
        this.startingBalance = 2000;
    }

    public void nextTurn() {
        currentPlayerID = activePlayers.get(turnNumber % activePlayers.size()).id;
        turnNumber++;
    }

    public void addPlayer(Player player) {
        activePlayers.add(player);
    }

    public void removePlayer(int playerID) {
        for (int i = 0; i < activePlayers.size(); i++) {
            int checkId = activePlayers.get(i).id;
            if (checkId == playerID) {
                activePlayers.remove(i);
            }
        }
    }

    public void onEvent(UserEvent event) {
        switch (event.event) {
            case EXCHANGE_CARDS:
                Integer[] indx = new Integer[3];
                for (int i = 0; i < 3; i++) {
                    indx[i] = Integer.parseInt((String) event.msg[i]);
                }
                onExchangeCards(event.playerID, indx);
                break;
            case RAISE:
                Double amount = Double.parseDouble((String)event.msg[0]);
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

    public void onStartMatch() {
        for (Player p : this.activePlayers) {
            p.setBalance(this.startingBalance);
            this.bettingPool += p.placeBet(20);
            p.dealHand(deck);
        }

        round = MatchRound.FIRST_BETTING;
        currentPlayerID = activePlayers.get(0).id;
    }

    public void bettingRound1() {
        round = MatchRound.FIRST_BETTING;
        turnNumber = 0;
    }

    public void bettingRound2() {
        round = MatchRound.SECOND_BETTING;
        turnNumber = 0;
    }

    public void drawRound() {
        round = MatchRound.DRAWING;
        turnNumber = 0;
    }

    public void showdownRound() {
        round = MatchRound.SHOWDOWN;
        turnNumber = 0;
    }

    public void onFold(int playerID) {
        removePlayer(playerID);
    }

    public void onCheck(int playerID) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            for (int i = 0; i < activePlayers.size(); i++) {
                int IDCheck = activePlayers.get(i).id;
                if (IDCheck == playerID) {
                    int count = 0;
                    for (int j = 0; j < i; j++) {
                        if (activePlayers.get(j).getcurrentBet() == 0) {
                            count++;
                        }
                    }
                    if (count == i) {
                        nextTurn();
                    }
                }
            }
        }
    }

    public void onCall(int playerID) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            for (int i = 0; i < activePlayers.size(); i++) {
                int IDCheck = activePlayers.get(i).id;
                if (IDCheck == playerID) {
                    if (activePlayers.get(i - 1).getcurrentBet() > activePlayers.get(i).getcurrentBet()) {
                        double addToPool = activePlayers.get(i).placeBet(
                                (activePlayers.get(i - 1).getcurrentBet() - activePlayers.get(i).getcurrentBet()));
                        bettingPool += addToPool;
                        nextTurn();
                    }
                }
            }
        }
    }

    public void onRaise(int playerID, double amount) {
        if (round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING) {
            this.bettingPool += getPlayer(playerID).placeBet(amount);
        }
    }

    public void onExchangeCards(int playerID, Integer[] cardIndex) {
        Player player = getPlayer(playerID);
        if (player != null) {
            getPlayer(playerID).exchangeCards(cardIndex, deck);
        } else {
            System.out.println("[Error] player does not exist");
        }
        nextTurn();
    }

    private Player getPlayer(int playerID) {
        Player player = null;

        for (Player p : this.activePlayers) {
            if (p.id == playerID) {
                player = p;
            }
        }

        return player;
    }

    public Boolean isWaiting() {
        return this.round == MatchRound.WAITING;
    }
}
