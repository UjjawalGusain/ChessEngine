package board;

public class Move {
	public Boolean castleMove = false;
	public PiecePosition prevPosition, currPosition;
	public PiecePosition gotRemoved;
	public Boolean isPromotion = false;
	public PiecePosition prevPositionKing, prevPositionRook, currPositionKing, currPositionRook;
	
	public Move(PiecePosition prevPosition, PiecePosition currPosition, PiecePosition gotRemoved) {
		this.prevPosition = prevPosition;
		this.currPosition = currPosition;
		this.gotRemoved = gotRemoved;
	}
	
	public Move(Boolean isCastleMove, PiecePosition prevPositionKing, PiecePosition prevPositionRook, PiecePosition currPositionKing, PiecePosition currPositionRook) {
		
		this.castleMove = isCastleMove;
		this.prevPositionKing = prevPositionKing;
		this.prevPositionRook = prevPositionRook;
		this.currPositionKing = currPositionKing;
		this.currPositionRook = currPositionRook;
	}
	
}
