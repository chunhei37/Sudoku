//Game level 5 = normal, 4=hard,  3= harder
public class PuzzleGenerator {
	
	public int[][] correctPuzzle; //A solved sudoku puzzle, but sudoku might have multiple. A well-formed sudoku should just have one!
	public int[][] gamePuzzle;   // An unfinished sudoku puzzle
	private final int size = 9;  // sudoku size
	public int blanks;
	
	//Constructor
	public PuzzleGenerator() {
		correctPuzzle = generatePuzzle();
		int level = 8; // deafult 3 
		gamePuzzle = createPlayablePuzzle(level);
	}
	
	//Make a 2D array copy
	public int[][] copyArray(int[][] src) {
		int[][] dist = new int[src.length][src.length];
		for(int i = 0; i < 9; i++) {
			for(int y = 0; y < 9; y++) {
				dist[i][y] = src[i][y];
			}
		}
		return dist;
	}
	
	//Removing some cell for player to play on
	private int[][] createPlayablePuzzle(int level) {
		int[][] puzzle = copyArray(correctPuzzle);
		blanks = 0;
		
		for(int i = 0; i < 9; i++) {
			for(int y = 0; y < 9; y++) {
				if((int)(Math.random() * 10) > level) {
					puzzle[i][y] = 0;
					blanks++;
				}
			}
		}
		return puzzle;
	}
	
	//Generate a solved puzzle
	private int[][] generatePuzzle() {
		int[][] puzzle = new int[size][size];
		int[] random = {0, 0, 0, 0, 0, 0, 0, 0, 0}; 
		int col = 0;
		while(col < 9) {
			int num = (int)(Math.random() * 9);
			if(random[num] == 0) {
				puzzle[0][col] = num;
				random[num] = 1;
				col++;
			}
		}
		Solver check = new Solver(puzzle);
		if(!check.solve()) {
			generatePuzzle();
		} 
		return check.puzzle;
	}
}
