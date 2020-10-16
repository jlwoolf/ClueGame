package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;

	final static int COLS = 4;
	final static int ROWS = 4;

	public TestBoard() {
		grid = new TestBoardCell[ROWS][COLS];
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}
		calcAdj();
	}

	public void calcAdj() {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				if(i != 0)
					grid[i][j].addAdj(this.getCell(i-1,j));
				if(i != ROWS-1)
					grid[i][j].addAdj(this.getCell(i+1,j));
				if(j != 0)
					grid[i][j].addAdj(this.getCell(i,j-1));
				if(j != COLS-1)
					grid[i][j].addAdj(this.getCell(i,j+1));
			}
		}
	}

	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visited = new HashSet<>();
		visited.add(startCell);
		targets = recursiveTargets(startCell, pathLength);
	}
	private Set<TestBoardCell> recursiveTargets(TestBoardCell startCell, int pathLength) {
		Set<TestBoardCell> targetSet = new HashSet<>();

		for(TestBoardCell adjCell : startCell.getAdjList()) {
			if(visited.contains(adjCell) || adjCell.isOccupied()) {
				continue;
			} 
			visited.add(adjCell);
			
			if(pathLength == 1 || adjCell.isRoom()) {
				targetSet.add(adjCell);
			} else {
				targetSet.addAll(recursiveTargets(adjCell, pathLength-1));
			}
			visited.remove(adjCell);
		}

		return targetSet;
	}


	public Set<TestBoardCell> getTargets() {
		return targets;
	}

	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public static void main(String[] args) {
		TestBoard board = new TestBoard();
		board.calcTargets(board.getCell(1, 1), 3);
		board.getTargets();
	}
}
