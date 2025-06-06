package board;

public class PiecePosition {
	public String name;
	public int color;
	public Boolean isSelected = false;
	public int x, y;
	
	PiecePosition(String name, int color, int x, int y) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
	}
}
