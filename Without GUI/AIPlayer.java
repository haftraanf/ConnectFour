import java.util.Random;

public class AIPlayer extends Player {

	public AIPlayer(char symbol, Board board, String name) {
		super(symbol, board, name);
	}

	@Override
	public void makeMove(Board board) {
		int count = board.checkerCount();
		int initial = count;
		// winning move
		if (board.fillingWin(this.symbol) != 0) {
			board.checkerDrop(this.symbol, board.fillingWin(this.symbol));
			count += 1;
		}
		// blocking move
		else {
			for (int row = 0; row < board.getNUM_OF_ROW(); row++) {
				for (int col = 0; col < board.getNUM_OF_COLUMNS(); col++) {
					char currentSymbol = board.symbolDetect(row, col);
						if (currentSymbol != this.symbol && currentSymbol != ' ') {
							if (board.fillingWin(currentSymbol) != 0) {
								board.checkerDrop (this.symbol, board.fillingWin(currentSymbol));
								count += 1;
								break;
							}
						}
				}
			}
		}
		//randomly chosen move if there is no possible winning or blocking move
		if (initial == count) {
			Random random = new Random();
			int max = 7;
			int number = random.nextInt(max) + 1;
			board.checkerDrop(this.symbol, number);
		}
	}
}
