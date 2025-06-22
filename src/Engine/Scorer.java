package Engine;

import board.PiecePosition;

public class Scorer {

	public static int getScore(PiecePosition[][] positions, int turn) {
		
		int score = 0;
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
