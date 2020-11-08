package clueGame;

import java.util.Set;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String name) {
		super(name);
	}

	public Solution createSuggestion(BoardCell room, Set<Card> deck) {
		return null;
	}
	public BoardCell selectTargets(Set<BoardCell> targets) {
		return null;
	}
}
