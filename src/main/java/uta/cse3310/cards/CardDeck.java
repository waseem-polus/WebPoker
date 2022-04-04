package uta.cse3310.cards;

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck {
    private ArrayList<Card> availablePile;

    public class OutOfCardsException extends Exception {
        public OutOfCardsException() {
            super("[Error] There are no available cards to draw from");
        }
    }
    
    public CardDeck() {
        availablePile = new ArrayList<>(52);
        
        // For each suite, create a card with all possible values and add all of them to
        // the availablePile
        for (Suite s : Suite.values()) {
            for (Value v : Value.values()) {
                availablePile.add(new Card(v, s));
            }
        }
        
        shuffleDeck();
    }
    
    /* Author: Waseem Alkasbutrus
     * Last Updated: 04/1/2022
     * 
     * shuffleDeck(): Uses Collections.shuffle to randomize the order of each card in the deck
     */
    public void shuffleDeck() {
        Collections.shuffle(availablePile);
    }

    /* Author: Waseem Alkasbutrus
     * Last Updated: 04/1/2022
     * 
     * draw(): returns a unique card from the availablePile and removes it from the
     * availablePile (to prevent duplicates in a game).
     * 
     * Returns:
     * a unique card from the availablePile
     */
    public Card drawCard() throws OutOfCardsException {
        if (availablePile.size() < 1) {
            throw new OutOfCardsException();
        }

        Card card = this.availablePile.get(0);
        availablePile.remove(0);
        
        return card;
    }

    /* Author: Waseem Alkasbutrus
     * Last Updated: 04/1/2022
     * 
     * draw(): returns a unique card from the availablePile and removes it from the
     * availablePile (to prevent duplicates in a game).
     * 
     * Returns:
     * a unique card from the availablePile
     */
    public Card[] dealHand() throws OutOfCardsException {
        if (availablePile.size() < 5) {
            throw new OutOfCardsException();
        }

        Card[] newHand = new Card[5];

        for (int i = 0; i < 5; i++) {
            newHand[i] = drawCard();
        }

        return newHand;
     }
}