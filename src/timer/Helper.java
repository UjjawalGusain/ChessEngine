package timer;

import java.io.IOException;

import Engine.Scorer;
import board.BoardFrame;
import board.PiecePosition;

public class Helper {
    public static int k = 0;
    public static int count = 0;
    BoardFrame boardFrame = null;
    public int depth;
    public static int[] bestMoves = {-1, -1};
    
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
    
    public static int recursiveBoardRunner(BoardFrame boardFrame, PiecePosition selectedPiece, int depth, int turn, int deep) {
    	boardFrame.selectedPosition[0] = selectedPiece.x;
    	boardFrame.selectedPosition[1] = selectedPiece.y;
		int maxi = Integer.MIN_VALUE;
		int mini = Integer.MAX_VALUE;
		int score;
		if(depth == 0) {
			return Helper.boardRunScorer(depth, boardFrame, turn, deep);
		}
		
		int[][] futureMoves = boardFrame.play(boardFrame.turn, boardFrame.positions);
		int maxX = -1, maxY = -1, minX = -1, minY = -1;
		for(int[] futureMove : futureMoves) {
			
			int i = futureMove[0], j = futureMove[1];
			PiecePosition[][] pc2 = boardFrame.getCopy(boardFrame.positions);
			boardFrame.checkCanChangePosition(i, j, futureMoves, pc2);
			
			if(!boardFrame.isGettingChecked(boardFrame.turn, pc2)) {
				
				boardFrame.changePosition(i, j, futureMoves, boardFrame.positions);
				
				int oppTurn = boardFrame.turn == 1 ? 0 : 1;
				if(boardFrame.isGettingChecked(boardFrame.turn, boardFrame.positions)) {
					boardFrame.kingChecked[boardFrame.turn] = true;

					if(boardFrame.isGettingCheckmated(boardFrame.turn, boardFrame.positions)) {
						boardFrame.checkmate[boardFrame.turn] = true;
					}
					
				} else if(boardFrame.isGettingStalemate(boardFrame.turn, boardFrame.positions)) {
					boardFrame.stalemate = true;
				} else {
					boardFrame.kingChecked[boardFrame.turn] = false;
				}
				
				if(boardFrame.isGettingChecked(oppTurn, boardFrame.positions)) {
					boardFrame.kingChecked[oppTurn] = true;
				} else {
					boardFrame.kingChecked[oppTurn] = false;
				}
				
				int opp = turn == 0 ? 1 : 0;
				score = Helper.boardRunScorer(depth-1, boardFrame, opp, deep);
				if(score >= maxi) {
					maxi = score > maxi ? score : maxi;
					maxX = i;
					maxY = j;
					
					if(deep == 1) {
						bestMoves[0] = i;
						bestMoves[1] = j;
					}
				}
				
				if(score < mini) {
					mini = score < mini ? mini : score;
					minX = i;
					minY = j;
				}
				
				
				boardFrame.undoMove();
				boardFrame.toggleTurn();
				boardFrame.selectedPosition[0] = selectedPiece.x;
				boardFrame.selectedPosition[1] = selectedPiece.y;
			}
			
		}
		int val = turn == boardFrame.turn ? maxi : mini;
//		if(deep == 1 && boardFrame.turn == turn) {
//    		System.out.printf("[%d][%d]\n", maxX, maxY);
//    	} else if(deep == 1) {
//    		System.out.printf("[%d][%d]\n", minX, minY);
//    	}
		return val;
	}
    
    public static int boardRunScorer(int depth, BoardFrame boardFrame, int turn, int deep) {
    	if(depth == 0) {
    		int value = Scorer.getScore(boardFrame.positions, turn);
        	return value;
    	}
    	int maxi = Integer.MIN_VALUE;
    	int mini = Integer.MAX_VALUE;
    	int maxX = -1, maxY = -1, minX = -1, minY = -1;
    	for(int i = 0; i<8; i++) {
    		for(int j = 0; j<8; j++) {
    			if(boardFrame.positions[i][j].color == boardFrame.turn) {
    				int opp = turn == 0 ? 1 : 0;
    				int score = recursiveBoardRunner(boardFrame, boardFrame.positions[i][j], depth - 1, turn, deep + 1);
    				if(score > maxi) {
    					maxi = score > maxi ? score : maxi;
    					maxX = i;
    					maxY = j;
    				}
    				
    				if(score < mini) {
    					mini = score < mini ? mini : score;
    					minX = i;
    					minY = j;
    				}
    			}
    				
    		}
    	}
    	
    	int value = boardFrame.turn == turn ? maxi : mini;
    	return value;	
    }
    
    public void run()
    {
    	count = 0;
    	boardRun(depth, boardFrame);
    	boardFrame.selectedPosition[0] = -1;
    	boardFrame.selectedPosition[1] = -1;
    	System.out.println("Count: " + count);

    }
}