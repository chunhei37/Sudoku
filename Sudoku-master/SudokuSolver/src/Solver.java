// A solver for a sudoku game.
// puzzle must be 9*9 large
public class Solver {
	
	public int[][] puzzle;

	//Constructor
	public Solver(int[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	public Solver() {
		
	}
	
	//Solver for the Sudoku; backTracking
	public boolean solve() {
		for(int i = 0; i < 9; i++) {
			for(int y = 0; y < 9; y++) {
				if(puzzle[i][y] != 0) {
					continue;
				}
				for(int num = 0; num < 9; num++) {
					if(checkValid(puzzle, i, y, num + 1)){
						puzzle[i][y] = num + 1;
						if(solve()) {
							return true;
						}
						puzzle[i][y] = 0;
					}
				}
				return false;
			}
		}
		return true;
	}
	
	public boolean checkSolved(int[][] puzzle) {
		for(int i = 0; i < 9; i++) {
			for(int y = 0; y < 9; y++) {
				if(puzzle[i][y] == 0) {
					return false;
				} else {
					if(!checkValid(puzzle, i, y, puzzle[i][y])) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	//this method check is the number valid to put in this cell
	public boolean checkValid(int[][] puzzle, int row, int col, int num) {
		return checkRow(puzzle, row, num) && checkCol(puzzle, col, num) && checkBox(puzzle, (row - row%3), (col - col%3), num);
	}
	
	//this method check is the number suitable for this column
	private boolean checkCol(int[][] puzzle, int col, int num) {
		for(int i = 0; i < 9; i++) {		
			if(puzzle[i][col] == num) {
				return false;
			}
		}
		return true;
	}
	
	//this method check is the number suitable for this row
	private boolean checkRow(int[][] puzzle, int row, int num) {
		for(int i = 0; i < 9; i++) {
			if(puzzle[row][i] == num) {
				return false;
			}
		}
		return true;
	}

	//This method check is the number suitable for this box
	private boolean checkBox(int[][] puzzle, int startRow, int startCol, int num) {
		for(int i = 0; i < 3; i++) {
			for(int y = 0; y < 3; y++) {
				if(puzzle[i + startRow][y + startCol] == num) {
					return false;
				}
			}
		}
		return true;
	}
}
