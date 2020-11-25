package clueGame;

import java.awt.*;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;

	//constructor
	public Room(String name) {
		this.name = name;
	}

	//name getter and setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//center cell getter and setter
	public BoardCell getCenterCell() {
		return centerCell;
	}
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	//label cell getter and setter
	public BoardCell getLabelCell() {
		return labelCell;
	}
	public void setLabelCell(BoardCell labelCenter) {
		this.labelCell = labelCenter;
	}

	//function for drawing the label in the rooms
	public void drawLabel(Graphics g, int size, int[] wallPadding) {
		g.setColor(new Color(169,183,198));
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, size));

		String[] words = name.split(" ");
		if(!name.equals("Walkway") && !name.equals("Unused")) {
			for(int i = 1; i <= words.length; i++) {
				g.drawString(words[i-1], labelCell.getCol()*size + wallPadding[0], labelCell.getRow()*size+i*size + wallPadding[1]);
			}
		}
	}
}
