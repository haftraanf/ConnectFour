public class Runner {
	public static void main(String[] arg) {
		Board board = new Board();
		ConnectFour game = new ConnectFour(board);
		game.setPlayer1(new HumanPlayer('H', board, "Hadley"));
		game.setPlayer2(new AIPlayer('A', board, "AI"));
		game.playGame();
		}
}
