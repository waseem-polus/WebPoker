package poker;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.events.Event;

import cards.CardDeck;
import pokerServer.UserEvent;
import pokerServer.UserEvent.UserEventType;


public class Match {
    private CardDeck deck;
    private double bettingPool;
    private MatchRound round;
    private ArrayList<Player> activePlayers;
    private int turnNumber;
    private int currentPlayerID;

    public enum MatchRound {
        WAITING, FIRST_BETTING, DRAWING, SECOND_BETTING, SHOWDOWN;
    }

    public Match() {

    }

    public Match(double playerBalance) {

    }

    public void nextTurn(){
        turnNumber++;
        currentPlayerID=activePlayers.get(turnNumber%activePlayers.size()).getID();
    }

    public void addPlayer(Player player){
        activePlayers.add(player);
    }

    public void removePlayer(int playerID){
        for(int i=0;i<activePlayers.size();i++){
            int IDCheck=activePlayers.get(i).getID();
            if(IDCheck == playerID){
                activePlayers.remove(i);
                nextTurn();
            }
        }
    }

    public void onEvent(UserEventType event){
        UserEvent e=new UserEvent();
        switch(event){
            case EXCHANGE_CARDS:
            Integer [] indx=new Integer[e.msgArr.length];
            for(int i=0;i<e.msgArr.length;i++){
                indx[i]=(Integer)e.msgArr[i];
            }
            int[] toInt=new int[indx.length];
            for(int j=0;j<indx.length;j++){
                toInt[j]=indx[j].intValue();
            }
            onExchangeCards(e.playerID, toInt);
            break;

            case RAISE:
            Integer indxO=(Integer)e.msg;
            double toDouble=indxO.doubleValue();
            onRaise(e.playerID, toDouble);
            break;
            default:
                break;
            
        }
    }

    public void onStartMatch() {
        if(isWaiting() == false){
            for(int i=0;i<activePlayers.size();i++){
                activePlayers.get(i).setBalance(2000);
            } 
            for(int j=0;j<activePlayers.size();j++){
                activePlayers.get(j).placeBet(20);
            }
            for(int k=0;k<activePlayers.size();k++){
                activePlayers.get(k).dealHand(deck);;
            }
            round=MatchRound.FIRST_BETTING;
        }
    }

    public void bettingRound1() {
        round = MatchRound.FIRST_BETTING;
        turnNumber=0;
    }

    public void bettingRound2() {
        round = MatchRound.SECOND_BETTING;
        turnNumber=0;
    }

    public void drawRound() {
        round = MatchRound.DRAWING;
        turnNumber=0;
    }

    public void showdownRound() {
        round = MatchRound.SHOWDOWN;
        turnNumber=0;
    }

    public void onFold(int playerID) {
        removePlayer(playerID);
    }

    public void onCheck(int playerID) {
        if(round == MatchRound.FIRST_BETTING || round == MatchRound.SECOND_BETTING){
            for(int i=0;i<activePlayers.size();i++){
                int IDCheck=activePlayers.get(i).getID();
                if(IDCheck == playerID){
                    int count=0;
                    for(int j=0;j<i;j++){
                        if(activePlayers.get(j).getcurrentBet() == 0){
                            count++;
                        }
                    } 
                    if(count == i){
                        nextTurn();
                    }
                }
            }
        }
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
                    activePlayers.get(i).exchangeCards(cardIndex,deck);
                }
            }
        }
    }

    public Boolean isWaiting(){
        return this.round==MatchRound.WAITING;
    }
}

