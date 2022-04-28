package uta.cse3310.poker;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import uta.cse3310.cards.Card;
import uta.cse3310.cards.CardDeck;
import uta.cse3310.cards.Suite;
import uta.cse3310.cards.Value;

public class TestHand {
    /*
	 * Author: Waseem Alkasbutrus
	 * Last Updated: 4/27/2022
	 * 
	 * tests if two hands are correctly ranked
	 */
    @Test
    public void testCompareHands() throws Exception {
        Card[] hand1Cards = {
            new Card(Value.ACE, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.ACE, Suite.HEARTS, "TestHand.testCompareHands()"),
            new Card(Value.ACE, Suite.DIAMONDS, "TestHand.testCompareHands()"),
            new Card(Value.TWO, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.FIVE, Suite.SPADES, "TestHand.testCompareHands()"),
        };
        Hand hand1 = new Hand(hand1Cards);
        
        Card[] hand2Cards = {
            new Card(Value.TEN, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.TEN, Suite.HEARTS, "TestHand.testCompareHands()"),
            new Card(Value.TEN, Suite.DIAMONDS, "TestHand.testCompareHands()"),
            new Card(Value.TWO, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.FIVE, Suite.SPADES, "TestHand.testCompareHands()"),
        };
        Hand hand2 = new Hand(hand2Cards);

        Card[] hand3Cards = {
            new Card(Value.ACE, Suite.HEARTS, "TestHand.testCompareHands()"),
            new Card(Value.ACE, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.ACE, Suite.SPADES, "TestHand.testCompareHands()"),
            new Card(Value.TWO, Suite.DIAMONDS, "TestHand.testCompareHands()"),
            new Card(Value.FIVE, Suite.CLUBS, "TestHand.testCompareHands()"),
        };
        Hand hand3 = new Hand(hand3Cards);

        Card[] hand4Cards = {
            new Card(Value.ACE, Suite.HEARTS, "TestHand.testCompareHands()"),
            new Card(Value.QUEEN, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.NINE, Suite.SPADES, "TestHand.testCompareHands()"),
            new Card(Value.TWO, Suite.DIAMONDS, "TestHand.testCompareHands()"),
            new Card(Value.FIVE, Suite.CLUBS, "TestHand.testCompareHands()"),
        };
        Hand hand4 = new Hand(hand4Cards);
    
        Card[] hand5Cards = {
            new Card(Value.KING, Suite.HEARTS, "TestHand.testCompareHands()"),
            new Card(Value.TEN, Suite.CLUBS, "TestHand.testCompareHands()"),
            new Card(Value.EIGHT, Suite.SPADES, "TestHand.testCompareHands()"),
            new Card(Value.THREE, Suite.DIAMONDS, "TestHand.testCompareHands()"),
            new Card(Value.SIX, Suite.CLUBS, "TestHand.testCompareHands()"),
        };
        Hand hand5 = new Hand(hand5Cards);

        assertTrue(hand1.compareTo(hand2) > 0);
        assertTrue(hand2.compareTo(hand1) < 0);
        assertTrue(hand1.compareTo(hand3) == 0);
        assertTrue(hand4.compareTo(hand5) > 0);
    }
}
