package timer;

import java.util.TimerTask;

import board.BoardFrame;
import board.Piece;

public class Helper {
    public static int k = 0;
    public static int count = 0;
    BoardFrame boardFrame = null;
    public int depth;
    
    public void setHelper(BoardFrame boardFrame, int depth) {
    	this.boardFrame = boardFrame;
    	this.depth = depth;
    }

    public static void boardRun(int depth, BoardFrame boardFrame) {
    	if(depth == 0) {
    		count++;
    		return;
    	}
    	for(int i = 0; i<8; i++) {
    		for(int j = 0; j<8; j++) {
    			if(boardFrame.positions[i][j].color == boardFrame.turn) {
    				boardFrame.playBySelf(boardFrame.positions[i][j], depth);
    			}
    				
    		}
    	}
    }
    
    public void run()
    {
    	count = 0;
    	boardRun(depth, boardFrame);
    	System.out.println("Count: " + count);

    }
}