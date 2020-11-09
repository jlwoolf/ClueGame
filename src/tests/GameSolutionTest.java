package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {

	private static Board board;
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		
		for(Player player : board.getPlayers()) {
			player.clearHand();
		}
	}

	@Test
	public void testAccusation() {
		Solution answer = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Brown Hall", CardType.ROOM),
				new Card("Gasoline", CardType.WEAPON));
		board.setAnswer(answer);

		Solution correct = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Brown Hall", CardType.ROOM),
				new Card("Gasoline", CardType.WEAPON));
		Solution wrongPerson = new Solution(new Card("Electrical Engineer", CardType.PERSON),
				new Card("Brown Hall", CardType.ROOM),
				new Card("Gasoline", CardType.WEAPON));
		Solution wrongRoom = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Alderson Hall", CardType.ROOM),
				new Card("Gasoline", CardType.WEAPON));
		Solution wrongWeapon = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Brown Hall", CardType.ROOM),
				new Card("Sulfuric Acid", CardType.WEAPON));

		assertTrue(board.checkAccusation(correct));
		assertFalse(board.checkAccusation(wrongPerson));
		assertFalse(board.checkAccusation(wrongRoom));
		assertFalse(board.checkAccusation(wrongWeapon));
	}

	@Test
	public void testDisprove() {
		board.getPlayers()[0].updateHand(new Card("Electrical Engineer", CardType.PERSON));
		board.getPlayers()[0].updateHand(new Card("Alderson Hall", CardType.ROOM));
		board.getPlayers()[0].updateHand(new Card("Sulfuric Acid", CardType.WEAPON));

		//check if only one card
		Solution suggestion = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Brown Hall", CardType.ROOM),
				new Card("Sulfuric Acid", CardType.WEAPON));
		assertEquals(new Card("Sulfuric Acid", CardType.WEAPON), board.getPlayers()[0].disproveSuggestion(suggestion));

		//check if 50/50
		suggestion = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Alderson Hall", CardType.ROOM),
				new Card("Sulfuric Acid", CardType.WEAPON));
		int count = 0;
		for(int i = 0; i < 100; i++) {
			if(board.getPlayers()[0].disproveSuggestion(suggestion).equals(new Card("Alderson Hall", CardType.ROOM))) {
				count++;
			}
		}
		assertTrue(count > 15 && count < 85);

		//check if no cards
		suggestion = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Brown Hall", CardType.ROOM),
				new Card("Gasoline", CardType.WEAPON));
		assertEquals(null, board.getPlayers()[0].disproveSuggestion(suggestion));
	}

	@Test
	public void handleSuggestions() {
		//suggestion no one can disprove (no one has any cards in hand)
		Solution suggestion = new Solution(new Card("Mechanical Engineer", CardType.PERSON),
				new Card("Alderson Hall", CardType.ROOM),
				new Card("Sulfuric Acid", CardType.WEAPON));
		assertEquals(null,board.handleSuggestion(board.getPlayers()[0], suggestion));

		//suggestion the suggesting player could disprove
		board.getPlayers()[0].updateHand(new Card("Alderson Hall", CardType.ROOM));
		assertEquals(null,board.handleSuggestion(board.getPlayers()[0], suggestion));

		//suggestion the human player can disprove
		assertEquals(new Card("Alderson Hall", CardType.ROOM),board.handleSuggestion(board.getPlayers()[1], suggestion));

		//suggestion two players can disprove. First in the list.
		board.getPlayers()[1].updateHand(new Card("Sulfuric Acid", CardType.WEAPON));
		assertEquals(new Card("Alderson Hall", CardType.ROOM),board.handleSuggestion(board.getPlayers()[2], suggestion));
	}
}
