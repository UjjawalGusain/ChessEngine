package board;

public class Move {
	public Boolean castleMove = false;
	public PiecePosition prevPosition, currPosition;
	public PiecePosition gotRemoved;
	
	public PiecePosition prevPositionLeft, prevPositionRight, currPositionLeft, currPositionRight;
	
	public Move(PiecePosition prevPosition, PiecePosition currPosition, PiecePosition gotRemoved) {
		this.prevPosition = prevPosition;
		this.currPosition = currPosition;
		this.gotRemoved = gotRemoved;
	}
	
	public Move(Boolean isCastleMove, PiecePosition prevPositionLeft, PiecePosition prevPositionRight, PiecePosition currPositionLeft, PiecePosition currPositionRight) {
		
		this.castleMove = isCastleMove;
		this.prevPositionLeft = prevPositionLeft;
		this.prevPositionRight = prevPositionRight;
		this.currPositionLeft = currPositionLeft;
		this.currPositionRight = currPositionRight;
	}
	
}
