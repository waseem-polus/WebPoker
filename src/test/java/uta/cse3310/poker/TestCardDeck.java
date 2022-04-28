package uta.cse3310.poker;

import uta.cse3310.cards.CardDeck;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestCardDeck {

    /*
	 * Author: Minh Trinh
	 * Last Updated: 4/27/2022
	 * 
	 * tests of 52 cards are created when a deck is initialized
	 */
    @Test
    public void test_52_cards() throws Exception {
        CardDeck deck = new CardDeck();
        int inc = 0;

        try {
            while (true) {
                deck.drawCard();
                inc++;
            }
        } catch (Exception e) {
        }
        assertTrue(inc == 52);
    }

    /*
	 * Author: Waseem Alkasbutrus
	 * Last Updated: 4/27/2022
	 * 
	 * Tests if shuffle deck shuffles correctly (the cards are not in the same order after being shuffled)
	 */
    @Test
    public void testShuffle() throws Exception {
        CardDeck deck = new CardDeck();
        deck.shuffleDeck();

        CardDeck CopyDeck = deck;

        boolean differentOrder = false;

        for(int i = 0; i < 52; i++) {
            if (!deck.drawCard().equals(CopyDeck.drawCard())) {
                differentOrder = true;
                break;
            }
        }

        assertTrue(differentOrder);
    }
}