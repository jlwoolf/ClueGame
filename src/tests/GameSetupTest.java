package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

public class GameSetupTest {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		
		board.deal();
	}
	
	@Test
	//test there are 6 players, 1 human, 5 computer
	public void testPlayers() {
		int numHuman = 0, numComp = 0;
		for(Player player : board.getPlayers()) {
			if(player instanceof HumanPlayer) {
				numHuman++;
			}
			else if(player instanceof ComputerPlayer) {
				numComp++;
			}
		}
		
		assertEquals(6, board.getPlayers().length);
		assertEquals(1, numHuman);
		assertEquals(5, numComp);
	}
	
	@Test
	//test there are 6 weapon cards
	public void testDeckWeaponCards() {
		Card testCard = new Card("3D Printed Knife", CardType.WEAPON);
		Card testCard2 = new Card("3D Printed Knife", CardType.WEAPON);
		assertTrue(board.getCards().contains(testCard));
		assertTrue(board.getWeaponCards().contains(testCard));
		testCard = new Card("High Current Taser", CardType.WEAPON);
		assertTrue(board.getCards().contains(testCard));
		assertTrue(board.getWeaponCards().contains(testCard));
		
		assertEquals(6, board.getWeaponCards().size());
	}
	
	@Test
	//test there are 6 person cards
	public void testDeckPersonCards() {
		Card testCard = new Card("Chemical Engineer", CardType.PERSON);
		assertTrue(board.getCards().contains(testCard));
		assertTrue(board.getPeopleCards().contains(testCard));
		testCard = new Card("Computer Scientist", CardType.PERSON);
		assertTrue(board.getCards().contains(testCard));
		assertTrue(board.getPeopleCards().contains(testCard));
		
		assertEquals(6, board.getPeopleCards().size());
	}
	
	@Test
	//test there are 9 room cards
	public void testDeckRoomCards() {
		Card testCard = new Card("Brown Hall", CardType.ROOM);
		assertTrue(board.getCards().contains(testCard));
		assertTrue(board.getRoomCards().contains(testCard));
		testCard = new Card("Alderson Hall", CardType.ROOM);
		assertTrue(board.getCards().contains(testCard));
		assertTrue(board.getRoomCards().contains(testCard));
		
		assertEquals(11, board.getRoomCards().size());
	}
	
	@Test
	//make sure no players hand has a card from the solution
	public void testSolution() {
		assertTrue(board.getAnswer().getPerson() != null);
		assertTrue(board.getAnswer().getRoom() != null);
		assertTrue(board.getAnswer().getWeapon() != null);
		
		for(Player player : board.getPlayers()) {
			assertTrue(3 <= player.getHand().size() && player.getHand().size() <= 4);
			assertFalse(player.getHand().contains(board.getAnswer().getRoom()));
			assertFalse(player.getHand().contains(board.getAnswer().getWeapon()));
			assertFalse(player.getHand().contains(board.getAnswer().getPerson()));
		}
		
		assertEquals(0, board.getDeck().size());
	}
	
	@Test
	//make sure no cards are dealt twice
	public void testDealing() {
		for(int i = 0; i < board.getPlayers().length; i++) {
			for(int j = i + 1; j < board.getPlayers().length; j++) {
				for(Card card : board.getPlayers()[i].getHand()) {
					assertFalse(board.getPlayers()[j].getHand().contains(card));
				}
			}
		}
	}
}
