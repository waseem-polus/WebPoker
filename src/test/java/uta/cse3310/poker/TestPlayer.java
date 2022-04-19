package uta.cse3310.poker;

import uta.cse3310.cards.CardDeck;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.ArrayList;

public class TestPlayer
{
    
    @Test
    public void test_getRoom() throws Exception
    {
        Player p1 = new Player(0);

        p1.setRoom(0);

        assertTrue(p1.getRoom() == 0);

    }

    @Test
    public void test_setName() throws Exception
    {
        Player p1 = new Player(0);

        p1.setName("TESTNAME");

        assertTrue(p1.name.equals("TESTNAME"));

    }

    @Test
    public void test_placeBet() throws Exception
    {
        Player p1 = new Player(0);

        p1.setBalance(100);
        p1.placeBet(20);

        assertTrue(p1.getCurrentBet() == 20);

    }

    @Test
    public void test_addToBalance() throws Exception
    {
        Player p1 = new Player(0);

        p1.setBalance(1000);
        p1.addToBalance(111);

        assertTrue(p1.getBalance() == 1111);

    }

}