package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	@Test
	public void testAdjacenciesRooms()
	{
		Set<BoardCell> testList = board.getAdjList(2, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(1, 6)));
		assertTrue(testList.contains(board.getCell(4, 0)));
		assertTrue(testList.contains(board.getCell(5, 0)));

		testList = board.getAdjList(19, 10);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(17, 10)));
		assertTrue(testList.contains(board.getCell(17, 11)));
		assertTrue(testList.contains(board.getCell(20, 14)));
		assertTrue(testList.contains(board.getCell(20, 14)));

		testList = board.getAdjList(19, 18);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(18, 15)));
	}

	@Test
	public void testAdjacencyDoor() {
		Set<BoardCell> testList = board.getAdjList(31, 5);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(27, 2)));
		assertTrue(testList.contains(board.getCell(31, 6)));

		testList = board.getAdjList(6, 2);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 1)));
		assertTrue(testList.contains(board.getCell(6, 3)));
		assertTrue(testList.contains(board.getCell(5, 2)));
		assertTrue(testList.contains(board.getCell(8, 2)));

		testList = board.getAdjList(7, 19);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(7, 18)));
		assertTrue(testList.contains(board.getCell(7, 20)));
		assertTrue(testList.contains(board.getCell(6, 19)));
		assertTrue(testList.contains(board.getCell(11, 18)));
	}

	@Test
	public void testAdjacencyWalkways() {
		Set<BoardCell> testList = board.getAdjList(24, 21);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(23, 21)));

		testList = board.getAdjList(24, 10);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(23, 10)));
		assertTrue(testList.contains(board.getCell(25, 10)));

		testList = board.getAdjList(10, 7);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(10, 8)));
		assertTrue(testList.contains(board.getCell(10, 6)));
		assertTrue(testList.contains(board.getCell(9, 7)));
		assertTrue(testList.contains(board.getCell(11, 7)));

		testList = board.getAdjList(17,20);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 19)));
		assertTrue(testList.contains(board.getCell(17, 21)));
		assertTrue(testList.contains(board.getCell(16, 20)));
	}

	@Test
	public void testTargetsInChauvenetHall() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 18), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(27, 2)));
		assertTrue(targets.contains(board.getCell(2, 15)));	

		// test a roll of 3
		board.calcTargets(board.getCell(2, 18), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(4, 15)));
		assertTrue(targets.contains(board.getCell(3, 14)));	
		assertTrue(targets.contains(board.getCell(1, 14)));
		assertTrue(targets.contains(board.getCell(27, 2)));	

		// test a roll of 4
		board.calcTargets(board.getCell(12, 20), 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(5, 15)));
		assertTrue(targets.contains(board.getCell(3, 15)));	
		assertTrue(targets.contains(board.getCell(1, 15)));
		assertTrue(targets.contains(board.getCell(27, 2)));	
	}

	@Test
	public void testTargetsInCoorsTech() {
		// test a roll of 1
		board.calcTargets(board.getCell(19, 18), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(18, 15)));	

		// test a roll of 3
		board.calcTargets(board.getCell(19, 18), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(19, 14)));
		assertTrue(targets.contains(board.getCell(17, 14)));	
		assertTrue(targets.contains(board.getCell(16, 15)));
		assertTrue(targets.contains(board.getCell(20, 15)));	

		// test a roll of 4
		board.calcTargets(board.getCell(12, 20), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(19, 15)));
		assertTrue(targets.contains(board.getCell(21, 15)));	
		assertTrue(targets.contains(board.getCell(17, 15)));
		assertTrue(targets.contains(board.getCell(15, 15)));	
	}
	
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(16, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(11, 18)));
		assertTrue(targets.contains(board.getCell(16, 18)));	
		assertTrue(targets.contains(board.getCell(16, 20)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(16, 19), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(11, 18)));
		assertTrue(targets.contains(board.getCell(16, 21)));
		assertTrue(targets.contains(board.getCell(16, 17)));	
		assertTrue(targets.contains(board.getCell(17, 18)));
		assertTrue(targets.contains(board.getCell(17, 20)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(16, 19), 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(11, 18)));
		assertTrue(targets.contains(board.getCell(16, 18)));	
		assertTrue(targets.contains(board.getCell(16, 20)));	
		assertTrue(targets.contains(board.getCell(16, 16)));
		assertTrue(targets.contains(board.getCell(17, 21)));	
	}
	
	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(31, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(31, 5)));
		assertTrue(targets.contains(board.getCell(31, 7)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(31, 6), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(27, 2)));
		assertTrue(targets.contains(board.getCell(27, 10)));
		assertTrue(targets.contains(board.getCell(28, 6)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(31, 6), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(29, 6)));
		assertTrue(targets.contains(board.getCell(27, 6)));
		assertTrue(targets.contains(board.getCell(27, 2)));
		assertTrue(targets.contains(board.getCell(27, 10)));
	}
	
	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(11, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(11, 15)));
		assertTrue(targets.contains(board.getCell(11, 13)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(11, 14), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(11, 15)));
		assertTrue(targets.contains(board.getCell(11, 13)));
		assertTrue(targets.contains(board.getCell(9, 15)));
		assertTrue(targets.contains(board.getCell(8, 14)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(11, 14), 4);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(11, 18)));
		assertTrue(targets.contains(board.getCell(14, 15)));
		assertTrue(targets.contains(board.getCell(15, 14)));
		assertTrue(targets.contains(board.getCell(12, 15)));
	}
	
	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(6, 15).setOccupied(true);
		board.calcTargets(board.getCell(4, 15), 4);
		board.getCell(6, 15).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(2, 18)));
		assertTrue(targets.contains(board.getCell(15, 9)));
		assertTrue(targets.contains(board.getCell(2, 15)));	
		assertFalse( targets.contains( board.getCell(8, 15))) ;
		assertFalse( targets.contains( board.getCell(7, 16))) ;
		assertFalse( targets.contains( board.getCell(6, 17))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(23, 18).setOccupied(true);
		board.getCell(26, 18).setOccupied(true);
		board.calcTargets(board.getCell(22, 20), 3);
		board.getCell(23, 18).setOccupied(false);
		board.getCell(26, 18).setOccupied(false);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(22, 21)));	
		assertTrue(targets.contains(board.getCell(26, 18)));	
		assertTrue(targets.contains(board.getCell(22, 17)));
		assertFalse( targets.contains( board.getCell(23, 18))) ;
		
		// check leaving a room with a blocked doorway
		board.getCell(23, 8).setOccupied(true);
		board.calcTargets(board.getCell(27, 10), 3);
		board.getCell(23, 8).setOccupied(false);
		targets= board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(24, 7)));
		assertTrue(targets.contains(board.getCell(22, 7)));	
		assertTrue(targets.contains(board.getCell(23, 10)));
	}
}
