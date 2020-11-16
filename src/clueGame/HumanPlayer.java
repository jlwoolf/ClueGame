package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player{

	public HumanPlayer(String name) {
		super(name);
	}
	
	public HumanPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
	}

}
