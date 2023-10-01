public class Board {
	
	private final int NUM_OF_COLUMNS = 7;
	private final int NUM_OF_ROW = 6;
	private char[][] board = new char[NUM_OF_ROW][NUM_OF_COLUMNS];
	
	/* 
	 * The board object must contain the board state in some manner.
	 * You must decide how you will do this.
	 * 
	 * You may add addition private/public methods to this class is you wish.
	 * However, you should use best OO practices. That is, you should not expose
	 * how the board is being implemented to other classes. Specifically, the
	 * Player classes.
	 * 
	 * You may add private and public methods if you wish. in fact, to achieve
	 * what the assignment is asking, you'll have to
	 * 
	 */
	public int getNUM_OF_COLUMNS() {
		return NUM_OF_COLUMNS;
	}

	public int getNUM_OF_ROW() {
		return NUM_OF_ROW;
	}

	public Board() {
		for (int row = 0; row < NUM_OF_ROW; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS; col++) {
				board[row][col] = ' ';
			}
		}
	}
	
	public void printBoard() {
		System.out.println(" 1 2 3 4 5 6 7");
		System.out.println("---------------");
		for (int row = 0; row < NUM_OF_ROW; row++) {
			System.out.print("|");
			for (int col = 0; col < NUM_OF_COLUMNS; col++) {
				System.out.print(board[row][col]);
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println("---------------");
		System.out.println(" 1 2 3 4 5 6 7");
	}

	public boolean vertical() {
		for (int col = 0; col < NUM_OF_COLUMNS; col++) {
			for (int row = 0; row < NUM_OF_ROW - 3; row++) {
				if (board[row][col]     != ' '                 && 
					board[row + 1][col] != ' '                 && 
					board[row + 2][col] != ' '                 && 
					board[row + 3][col] != ' '                 &&
					board[row][col]     == board[row + 1][col] &&
					board[row + 1][col] == board[row + 2][col] &&
					board[row + 2][col] == board[row + 3][col]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean horizontal() {
		for (int row = 0; row < NUM_OF_ROW; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS - 3; col++) {
				if (board[row][col]     != ' '                 && 
					board[row][col + 1] != ' '                 && 
					board[row][col + 2] != ' '                 && 
					board[row][col + 3] != ' '                 &&
					board[row][col]     == board[row][col + 1] &&
					board[row][col + 1] == board[row][col + 2] &&
					board[row][col + 2] == board[row][col + 3]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean diagonal() {
		// downward diagonal (left-up to right-down)
		for (int row = 0; row < NUM_OF_ROW - 3; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS - 3; col++) {
				if (board[row][col]         != ' '                     &&
					board[row + 1][col + 1] != ' '                     && 
					board[row + 2][col + 2] != ' '                     && 
					board[row + 3][col + 3] != ' '                     &&
					board[row][col]         == board[row + 1][col + 1] &&
					board[row + 1][col + 1] == board[row + 2][col + 2] &&
					board[row + 2][col + 2] == board[row + 3][col + 3]) {
					return true;
				}
			}
		}
		
		// upward diagonal (left-down to right-up)
		for (int row = 3; row < NUM_OF_ROW; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS - 3; col++) {
				if (board[row][col]         != ' '                     &&
					board[row - 1][col + 1] != ' '                     && 
					board[row - 2][col + 2] != ' '                     && 
					board[row - 3][col + 3] != ' '                     &&
					board[row][col]         == board[row - 1][col + 1] &&
					board[row - 1][col + 1] == board[row - 2][col + 2] &&
					board[row - 2][col + 2] == board[row - 3][col + 3]) {
					return true;
				}
			}
		}
		return false;		
	}
	
	public boolean containsWin() {
		if (vertical() || horizontal() || diagonal()) return true;
		else return false;
	}
	
	public boolean isTie() {
		for (int row = 0; row < NUM_OF_ROW; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS; col++) {
				if (board[row][col] == ' ' || containsWin()) return false;
			}
		}
		return true;
	}
	
	public void reset() {
		for (int row = 0; row < NUM_OF_ROW; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS; col++) {
				board[row][col] = ' ';
			}
		}
	}
	
	public boolean colValidate(int col) {
		if (board[0][col - 1] != ' ') return false;
		else return true;
	}
	
	public void checkerDrop (char symbol, int col) {
		for (int row = 5; row >= 0; row--) {
			if(board[row][col - 1] == ' ') {
				board[row][col - 1] = symbol;
				break;
			}
		}
	}
		
	public char symbolDetect(int row, int col) {
		return board[row][col];
	}
	
	public boolean almostContainsWin(char symbol, int col) {
		for (int row = 5; row >= 0; row--) {
			if(board[row][col - 1] == ' ') {
				board[row][col - 1] = symbol;
				if (containsWin()) {
					board[row][col - 1] = ' ';
					return true;
				}
				else {
					board[row][col - 1] = ' ';
					break;
				}
			}
		}
		return false;
	}
	
	public int fillingWin(char symbol) {
		for (int col = 1; col < NUM_OF_COLUMNS + 1; col++) {
			if (colValidate(col)) {
				if (almostContainsWin(symbol, col)) return col;
			}
			else continue;
		}
		return 0;
	}

	public int checkerCount() {
		int count = 0;
		for (int row = 0; row < NUM_OF_ROW; row++) {
			for (int col = 0; col < NUM_OF_COLUMNS; col++) {
				if (board[row][col] != ' ') count += 1;
			}
		}
		return count;
	}
}
