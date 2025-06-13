package board;

public class Move {
	public PiecePosition prevPosition, currPosition;
	public PiecePosition gotRemoved;
	public Move(PiecePosition prevPosition, PiecePosition currPosition, PiecePosition gotRemoved) {
		this.prevPosition = prevPosition;
		this.currPosition = currPosition;
		this.gotRemoved = gotRemoved;
	}
}
