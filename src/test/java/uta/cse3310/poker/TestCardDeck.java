package uta.cse3310.poker;

import uta.cse3310.cards.CardDeck;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestCardDeck
{

    @Test
    public void test_52_cards() throws Exception
    {
        CardDeck deck = new CardDeck();
        int inc = 0;

        try
        {
            while(inc != 52)
            {
                deck.drawCard();
                inc++;
            }
        }
        catch(Exception e)
        {
        }
        assertTrue(inc == 52);
    }
}