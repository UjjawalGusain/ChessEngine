package Engine;

import board.BoardFrame;
import board.PiecePosition;

public class Scorer {

	public static int getScore(BoardFrame boardFrame, PiecePosition[][] positions, int turn) {
		
		int score = 0;
		
		PiecePosition[][] pc2 = boardFrame.getCopy(positions);
		int opp = turn == 0 ? 1 : 0;
		if(boardFrame.isGettingCheckmated(opp, pc2)) {
			return turn == boardFrame.turn ? -10000 : 10000;
		}
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				
				PiecePosition piece = positions[i][j];
				
				int reward = 0;
				
				switch(piece.name) {
				case "k":
					reward = 1000;
					break;
				case "q":
					reward = 9;
					break;
				case "r":
					reward = 5;
					break;
				case "b":
					reward = 3;
					break;
				case "n":
					reward = 3;
					break;
				case "p":
					reward = 1;
					break;
				default:
					break;
				}
				
				if(piece.color == turn) {
					score += reward;
				} else {
					score -= reward;
				}
				
			}
		}
		return score;
	}
	
}
