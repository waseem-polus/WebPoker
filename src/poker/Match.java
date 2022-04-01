package poker;

import java.util.ArrayList;
import java.util.HashMap;

import cards.CardDeck;
import pokerServer.UserEvent;


public class Match {
    private CardDeck deck;
    private double bettingPool;
    private MatchRound round;
    private ArrayList<Player> activePlayers;
    private int turnNumber;
    private int currentPlayerID;

    public enum MatchRound {
        FIRST_BETTING, DRAWING, SECOND_BETTING, SHOWDOWN;
    }

    public Match() {

    }

    public Match(double playerBalance) {

    }

    public void addPlayer(Player player){

    }

    public void removerPlayer(int playerID){
        
    }

    public void onEvent(UserEvent event){

    }

    public void onStartMatch() {

    }

    public void bettingRound() {

    }

    public void drawRound() {

    }

    public void showdownRound() {

    }

    public void onFold(int playerID) {

    }

    public void onCheck(int playerID) {
        
    }

    public void onCall(int playerID) {
        if(round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING){
            for(int i=0;i<activePlayers.size();i++){
                int IDCheck=activePlayers.get(i).getID();
                if(IDCheck == playerID){
                    if(activePlayers.get(i-1).getcurrentBet() > activePlayers.get(i).getcurrentBet()){
                        double addToPool=activePlayers.get(i).placeBet((activePlayers.get(i-1).getcurrentBet()-activePlayers.get(i).getcurrentBet()));
                        bettingPool +=addToPool;
                    }
                }
            }
        }
    }

    public void onRaise(int playerID, double amount) {
        if(round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING){
            for(int i=0;i<activePlayers.size();i++){
                int IDCheck=activePlayers.get(i).getID();
                if(IDCheck == playerID){
                    double addToPool=activePlayers.get(i).placeBet(amount);
                    bettingPool +=addToPool;
                }
            }
        }
    }

    public void onExchangeCards(int playerID, int[] cardIndex){
        if(round == MatchRound.DRAWING){
            for(int i=0;i<activePlayers.size();i++){
                int IDCheck=activePlayers.get(i).getID();
                if(IDCheck == playerID){
                    activePlayers.get(i).exchangeCard(cardIndex,deck);
                }
            }
        }
    }
}
