package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTests {
	public static final int NUM_ROWS = 32;
	public static final int NUM_COLUMNS = 22;
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
	
	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Brown Hall", board.getRoom('B').getName() );
		assertEquals("Rec Center", board.getRoom('R').getName() );
		assertEquals("Arthur Lakes Library", board.getRoom('L').getName() );
		assertEquals("Parking Garage", board.getRoom('P').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
	}
	
	@Test
	//tests the boards dimensions
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}
	
	@Test
	//tests that all directions are present
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(1, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(11, 2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(31, 7);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(24, 0);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(7, 10);
		assertFalse(cell.isDoorway());
	}
	
	@Test
	//tests that the number of doorways detected are valid
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		assertEquals(27, numDoors);
	}
	
	@Test
	public void testRooms() {
		//tests a normal room space
		BoardCell cell = board.getCell( 0, 0);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Parking Garage" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;
		
		//tests a room center space
		cell = board.getCell( 4, 10);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Arthur Lakes Library" ) ;
		assertFalse( cell.isLabel() );
		assertTrue( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		//tests a room label space
		cell = board.getCell( 8, 1);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Student Halls" ) ;
		assertTrue( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;
		
		//tests a secret passage space
		cell = board.getCell( 31, 16);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Alderson Hall" ) ;
		assertTrue( cell.getSecretPassage() == 'P' );

		//tests a walkway space
		cell = board.getCell(6, 0);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
		//tests and unused space
		cell = board.getCell(31, 21);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
	}

}
