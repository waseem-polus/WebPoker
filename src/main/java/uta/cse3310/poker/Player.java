package uta.cse3310.poker;

import uta.cse3310.cards.CardDeck;

public class Player {
    private String name;
    private double balance;
    private Hand hand;
    private double currentBet;
    public final int id;

    public Player(int id) {
        this.name = "";
        this.balance = 0;
        this.hand = new Hand();
        this.currentBet = 0;
        this.id = id;
    }

    public void exchangeCards(Integer[] cardIndex, CardDeck deck) {
        hand.exchangeCards(cardIndex, deck);
    }

    public void dealHand(CardDeck deck) {
        hand.dealHand(deck);
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

    public double getcurrentBet() {
        return currentBet;
    }

    public int getID() {
        return id;
    } 

    public void setName(String name) {
        this.name = name;
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
}