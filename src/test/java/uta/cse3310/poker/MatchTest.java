package uta.cse3310.poker;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MatchTest {
	@Test
	public void testMatch() throws Exception {
		Match match = new Match();

		assertTrue(match != null);
	}

	/*
	 * Author: Meaza Abera, Edited by Waseem Alkasbutrus
	 * Last Updated: 4/18/2022
	 * 
	 * tests if next turn correctly moves to the next player turn
	 */
	@Test
	public void testNextTurn() throws Exception {
		Match match = new Match();

		Player p1 = new Player(0);
		Player p2 = new Player(1);
		Player p3 = new Player(2);
		Player p4 = new Player(3);
		Player p5 = new Player(4);

		match.addPlayer(p1);
		match.addPlayer(p2);
		match.addPlayer(p3);
		match.addPlayer(p4);
		match.addPlayer(p5);

		match.nextTurn();

		assertTrue(match.getCurrentPlayer() == 1);
	}
	
	@Test
	public void testAddPlayer() throws Exception {
		Match match = new Match();

        Player p1 = new Player(0);
        Player p2 = new Player(1);
       
        match.addPlayer(p1);
        match.addPlayer(p2);
       
        assertTrue(match.countActivePlayers() == 2 );

	}

	@Test
	public void testRemovePlayer() throws Exception {
		Match match = new Match();

		Player p1 = new Player(0);
		Player p2 = new Player(1);
		Player p3 = new Player(2);
		Player p4 = new Player(3);
		Player p5 = new Player(4);

		match.addPlayer(p1);
		match.addPlayer(p2);
		match.addPlayer(p3);
		match.addPlayer(p4);
		match.addPlayer(p5);

		match.removePlayer(1);

		assertTrue(match.countActivePlayers() == 4);

	}

	@Test
	public void testOnStartMatch() throws Exception {

		Match match = new Match();

		Player p1 = new Player(0);
		Player p2 = new Player(1);

		match.addPlayer(p1);
		match.addPlayer(p2);

		match.onStartMatch();
		
		//Condition changed by Waseem Alkasbutrus
		assertTrue(!match.isWaiting());
	}

	@Test
	public void testOnFold() throws Exception {
		Match match = new Match();

		Player p1 = new Player(0);
		Player p2 = new Player(1);
		Player p3 = new Player(2);
		Player p4 = new Player(3);
		Player p5 = new Player(4);

		match.addPlayer(p1);
		match.addPlayer(p2);
		match.addPlayer(p3);
		match.addPlayer(p4);
		match.addPlayer(p5);

		match.onFold(2);
		match.onFold(4);
		assertTrue(match.countActivePlayers() == 3);

	}

	@Test
	public void testIsWaiting() throws Exception {
		Match match = new Match();

		Player p1 = new Player(0);
		Player p2 = new Player(1);
		Player p3 = new Player(2);
		Player p4 = new Player(3);
		Player p5 = new Player(4);

		match.addPlayer(p1);
		match.addPlayer(p2);
		match.addPlayer(p3);
		match.addPlayer(p4);
		match.addPlayer(p5);

		//Condition tweaked by Waseem Alkasbutrus
		assertTrue(match.countActivePlayers() == 5 && match.isWaiting());
	}
}
