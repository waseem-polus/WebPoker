package uta.cse3310.poker;

import uta.cse3310.cards.CardDeck;

public class Player implements Comparable<Player> {
    public String name;
    private double balance;
    public Hand hand;
    private double currentBet;
    public final int id;
    private int roomPin;
    private boolean folded;

    public Player(int id) {
        this.name = "";
        this.balance = 0;
        this.hand = new Hand();
        this.currentBet = 0;
        this.id = id;
        this.roomPin = -1;
        this.folded = false;
    }

    public String exchangeCards(Integer[] cardIndex, CardDeck deck) {
        return hand.exchangeCards(cardIndex, deck);
    }

    public void dealHand(CardDeck deck) {
        hand.dealHand(deck);
        folded = false;
    }

    public void setRoom(int pin) {
        this.roomPin = pin;
    }

    public int getRoom() {
        return this.roomPin;
    }

    public double placeBet(double amount) {
        double bet = amount;
        if (amount <= balance) {
            this.currentBet += amount;
            this.balance -= amount;
        }
        else {
            this.currentBet += this.balance;
            bet = this.balance;
            this.balance = 0;
        }

        return bet;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }

    public double getBalance() {
        return this.balance;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public void clearBet() {
        this.currentBet = 0;
    }

    public int getID() {
        return id;
    } 

    public void setName(String name) {
        this.name = name;
    }


    public void addToBalance(double amount) {
        this.balance += amount;
    }

    public void fold() {
        this.folded = true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object p) {
        boolean equals = false;
        
        if(p.getClass() == this.getClass()) {
            Player player = (Player) p;
            equals = this.id == player.id;
        }
        return equals;
    }

    /* Author: Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     *
     * compareTo(p): compare this player object to p
     * 
     * Parameters:
     *      Player p: the player to compare against
     * 
     * Returns: 
     *      1 if better hand than h
     *      0 if same hands than h
     *     -1 if worse hand than h
     */
    @Override
    public int compareTo(Player p) {
        return this.hand.compareTo(p.hand);
    }
}