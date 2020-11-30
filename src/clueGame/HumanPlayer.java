package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player{
	private boolean unfinished;
	//constructors for the human player. Simply extensions from the Player class
	public HumanPlayer(String name) {
		super(name);
		this.unfinished = true;
	}
	public HumanPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
		this.unfinished = true;
	}

	public void setUnfinished(boolean unfinished) {
		this.unfinished = unfinished;
	}

	public boolean isUnfinished() {
		return unfinished;
	}
}
