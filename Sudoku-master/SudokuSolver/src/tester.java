
public class tester {

	public static void main(String[] args) {
		PuzzleGenerator pg = new PuzzleGenerator();
		
		for(int i = 0; i < 9 ; i++) {
			for(int y = 0; y < 9; y++) {
				System.out.print(pg.correctPuzzle[i][y] + " ");
				
			}
			System.out.println();
		}
		
		System.out.println();
		for(int i = 0; i < 9 ; i++) {
			for(int y = 0; y < 9; y++) {
				System.out.print(pg.gamePuzzle[i][y] + " ");
				
			}
			System.out.println();
		}

	}

	
}
