package runner;

import java.io.IOException;

import board.BoardFrame;

public class Game {
	int playerColor, aiColor;
	int turn;
	
	public Game(int playerColor) {
		turn = 0; // 0 for white
		this.playerColor = playerColor; // 0 for white, 1 for black
		this.aiColor = playerColor == 1 ? 0 : 1;
	}
	
	public void startGame() throws IOException {
		BoardFrame board = new BoardFrame("Chess Game");
        board.setFirstTime();
        board.setBoard();
        board.play(turn);
        
//        while(!board.didWin()) {
//        	board.play();
//        }
        
	}
}
