package tests;

import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import experiment.TestBoard;
import experiment.TestBoardCell;
import junit.framework.Assert;

public class BoardTestsExp {
	TestBoard testBoard;
	@BeforeEach 
	public void setup() {
		testBoard = new TestBoard();
	}
	
	//checks that adjacent cell list is correct for location (0,0) on a 4x4 grid
	//this cell is in the top left of the board
	@Test
	public void testAdjacency0() {
		TestBoardCell cell = testBoard.getCell(0, 0);
		Set<TestBoardCell> testAdjList = cell.getAdjList();
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(1, 0)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(0, 1)));
		Assert.assertEquals(2, testAdjList.size());
	}
	
	//checks that adjacent cell list is correct for location (3,3) on a 4x4 grid
	//this cell is in the bottom right of the board
	@Test
	public void testAdjacency1() {
		TestBoardCell cell = testBoard.getCell(3, 3);
		Set<TestBoardCell> testAdjList = cell.getAdjList();
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(2, 3)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(3, 2)));
		Assert.assertEquals(2, testAdjList.size());
	}
	
	//checks that adjacent cell list is correct for location (1,3) on a 4x4 grid
	//this cell is on the right edge of the board
	@Test
	public void testAdjacency2() {
		TestBoardCell cell = testBoard.getCell(1, 3);
		Set<TestBoardCell> testAdjList = cell.getAdjList();
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(0, 3)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(2, 3)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(1, 2)));
		Assert.assertEquals(3, testAdjList.size());
	}
	
	//checks that adjacent cell list is correct for location (3,1) on a 4x4 grid
	//this cell is on the left edge of the board
	@Test
	public void testAdjacency3() {
		TestBoardCell cell = testBoard.getCell(3, 1);
		Set<TestBoardCell> testAdjList = cell.getAdjList();
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(3, 0)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(3, 2)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(2, 1)));
		Assert.assertEquals(3, testAdjList.size());
	}
	
	//checks that adjacent cell list is correct for location (2,2) on a 4x4 grid
	//this cell is in the middle of the board
	@Test
	public void testAdjacency4() {
		TestBoardCell cell = testBoard.getCell(2, 2);
		Set<TestBoardCell> testAdjList = cell.getAdjList();
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(2, 1)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(2, 3)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(1, 2)));
		Assert.assertTrue(testAdjList.contains(testBoard.getCell(3, 2)));
		Assert.assertEquals(4, testAdjList.size());
	}
	
	//checks that target cells are correct for a given dice roll of 3
	//board is empty
	@Test
	public void testTargetEmpty() {
		TestBoardCell cell = testBoard.getCell(0, 0);
		testBoard.calcTargets(cell, 3);
		Set<TestBoardCell> targets = testBoard.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(testBoard.getCell(3, 0)));
		Assert.assertTrue(targets.contains(testBoard.getCell(2, 1)));
		Assert.assertTrue(targets.contains(testBoard.getCell(1, 2)));
		Assert.assertTrue(targets.contains(testBoard.getCell(0, 3)));
	}
	
	//checks that target cells are correct for a given dice roll of 3
	//board has an occupied cell
	@Test
	public void testTargetOccupied() {
		testBoard.getCell(1, 2).setOccupied(true);
		TestBoardCell cell = testBoard.getCell(0, 0);
		testBoard.calcTargets(cell, 3);
		Set<TestBoardCell> targets = testBoard.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(testBoard.getCell(3, 0)));
		Assert.assertTrue(targets.contains(testBoard.getCell(2, 1)));
		Assert.assertTrue(targets.contains(testBoard.getCell(0, 3)));
	}
	
	//checks that target cells are correct for a given dice roll of 3
	//board has a room cell
	@Test
	public void testTargetRoom() {
		testBoard.getCell(0, 2).setRoom(true);
		TestBoardCell cell = testBoard.getCell(0, 0);
		testBoard.calcTargets(cell, 3);
		Set<TestBoardCell> targets = testBoard.getTargets();
		Assert.assertEquals(5, targets.size());
		Assert.assertTrue(targets.contains(testBoard.getCell(0, 2)));
		Assert.assertTrue(targets.contains(testBoard.getCell(3, 0)));
		Assert.assertTrue(targets.contains(testBoard.getCell(2, 1)));
		Assert.assertTrue(targets.contains(testBoard.getCell(1, 2)));
		Assert.assertTrue(targets.contains(testBoard.getCell(0, 3)));
	}
}
