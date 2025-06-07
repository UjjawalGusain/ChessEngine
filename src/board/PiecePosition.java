package board;

public class PiecePosition {
	public String name;
	public int color;
	public Boolean isSelected = false;
	public int x, y;
	public Boolean isFutureMove = false;
	
	PiecePosition(String name, int color, int x, int y) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
	}
}
