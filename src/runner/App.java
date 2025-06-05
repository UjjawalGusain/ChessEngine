package runner;

import java.io.IOException;

import board.Board;
import board.BoardFrame;

public class App {

	public static void main(String[] args) throws IOException {
		Game game = new Game(0);
		game.startGame();
	}

}
