package poker;

import java.util.HashMap;

import cards.CardDeck;


public class Match {
    private CardDeck deck;
    private double bettingPool;
    private MatchRound round;
    private HashMap<Integer, Player> players;

    public enum MatchRound {
        FIRST_BETTING, DRAWING, SECOND_BETTING, SHOWDOWN;
    }

    public Match() {

    }

    public void startMatch() {

    }

    public void bettingRound() {

    }

    public void drawRound() {

    }

    public void showdownRound() {

    }

    public void fold(int playerID) {

    }

    public void check(int playerID) {

    }

    public void call(int playerID) {

    }

    public void raise(int playerID, double amount) {

    }
}
