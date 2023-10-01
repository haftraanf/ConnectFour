import java.util.Scanner;

public class HumanPlayer extends Player {
	public HumanPlayer(char symbol, Board board, String name) {
		super(symbol, board, name);
	}

	@Override
	public void makeMove(Board board) {
		int number;
		do {
			System.out.print(name + ", please input your move: ");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner (System.in);
			number = scanner.nextInt();
		} while (board.colValidate(number) == false);
		board.checkerDrop(symbol, number);
	}
}