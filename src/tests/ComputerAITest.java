package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class ComputerAITest {
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
	public void testComputerSuggestion() {
		ComputerPlayer computerPlayer = new ComputerPlayer("testPlayer", 2, 2, Color.BLUE);
		Solution suggestion;
	
		//fill seenCards with all cards except for 4
		for(Card card : board.getCards()) {
			if(card.equals(new Card("Sulfuric Acid", CardType.WEAPON))) {
				continue;
			}
			if(card.equals(new Card("Gasoline", CardType.WEAPON))) {
				continue;
			}
			if(card.equals(new Card("Mechanical Engineer", CardType.PERSON))) {
				continue;
			}
			if(card.equals(new Card("Electrical Engineer", CardType.PERSON))) {
				continue;
			}
			computerPlayer.updateSeen(card);
		}
		
		//check if suggestion chooses random person and weapon from unseen cards
		int weaponCount = 0;
		int personCount = 0;
		for(int i = 0; i < 100; i++) {
			suggestion = computerPlayer.createSuggestion(board.getCell(computerPlayer.getRow(), computerPlayer.getCol()), board.getCards());
			
			if(suggestion.getWeapon().equals(new Card("Sulfuric Acid", CardType.WEAPON))) {
				weaponCount++;
			}
			if(suggestion.getPerson().equals(new Card("Mechanical Engineer", CardType.PERSON))) {
				personCount++;
			}
		}
		
		assertTrue(weaponCount > 15 && weaponCount < 85);
		assertTrue(personCount > 15 && personCount < 85);
		
		computerPlayer.updateSeen(new Card("Gasoline", CardType.WEAPON));
		computerPlayer.updateSeen(new Card("Electrical Engineer", CardType.PERSON));
		
		//check if suggestion choose only card unseen and room card is current room
		suggestion = computerPlayer.createSuggestion(board.getCell(computerPlayer.getRow(), computerPlayer.getCol()), board.getCards());
		assertEquals(board.getRoom('P').getName(), suggestion.getRoom().getCardName());
		assertEquals(new Card("Mechanical Engineer", CardType.PERSON), suggestion.getPerson());
		assertEquals(new Card("Sulfuric Acid", CardType.WEAPON), suggestion.getWeapon());
	}
	
	@Test
	public void testComputerTarget() {
		ComputerPlayer computerPlayer = new ComputerPlayer("testPlayer", 1, 7, Color.BLUE);
		
		//if no room, choose randomly
		board.calcTargets(board.getCell(computerPlayer.getRow(), computerPlayer.getCol()), 1);
		Set<BoardCell> targets = board.getTargets();
		
		int walkwayCount = 0;
		for(int i = 0; i < 200; i++) {
			if(computerPlayer.selectTargets(targets).equals(board.getCell(0, 7))) {
				walkwayCount++;
			}
		}
		assertTrue(walkwayCount > 15 && walkwayCount < 85);
		
		//if contains room not seen choose room
		board.calcTargets(board.getCell(computerPlayer.getRow(), computerPlayer.getCol()), 3);
		targets = board.getTargets();
		assertEquals(board.getCell(2, 2), computerPlayer.selectTargets(targets));
		
		//if contains room seen choose randomly
		computerPlayer.updateSeen(new Card("Parking Garage", CardType.ROOM));
		walkwayCount = 0;
		for(int i = 0; i < 300; i++) {
			if(computerPlayer.selectTargets(targets).equals(board.getCell(2, 2))) {
				walkwayCount++;
			}
		}
		assertTrue(walkwayCount > 15 && walkwayCount < 85);
	}
}
