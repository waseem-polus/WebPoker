package poker;

import cards.CardDeck;

public class Player {
    private String name;
    private double balance;
    private Hand hand;
    private double currentBet;
    private final int id;

    public Player(int id) {
        this.name = "";
        this.balance = 2000;
        this.hand = new Hand();
        this.currentBet = 0;
        this.id = id;
    }

    public void exchangeCard(int[] cardIndex, CardDeck deck) {
        hand.exchangeCard(cardIndex, deck);
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

    public double getcurrentBet() {
        return currentBet;
    }

    public int getID() {
        return id;
    } 

    public void setName(String name) {
        this.name = name;
    }

    // public int hashCode() {
    //     return;
    // }

    public boolean equals(Player p) {
        return this.id == p.id;
    }
}