package board;

public class PiecePosition {
	public String name;
	public int color;
	public Boolean isSelected = false;
	public int x, y;
	public Boolean isFutureMove = false;
	public Boolean promote = false;
	
	PiecePosition(String name, int color, int x, int y) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.promote = false;
	}
	
	PiecePosition(String name, int color, int x, int y, Boolean isSelected, Boolean isFutureMove) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.isFutureMove = isFutureMove;
		this.isSelected = isSelected;
	}
	
}
