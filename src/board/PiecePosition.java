package board;

public class PiecePosition {
	public String name;
	public int color;
	public Boolean isSelected = false;
	public int x, y;
	public Boolean isFutureMove = false;
	public Boolean promote = false;
	public int[] enPassant = {-1, -1};
	
	public void setEnpassant(int x, int y) {
		this.enPassant[0] = x;
		this.enPassant[1] = y;
	}
	
	public PiecePosition(String name, int color, int x, int y) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.promote = false;
	}
	
	public PiecePosition(String name, int color, int x, int y, Boolean isSelected, Boolean isFutureMove) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.isFutureMove = isFutureMove;
		this.isSelected = isSelected;
	}
	
	public PiecePosition(PiecePosition p) {
		this.name = p.name;
		this.color = p.color;
		this.x = p.x;
		this.y = p.y;
		this.isFutureMove = p.isFutureMove;
		this.isSelected = p.isSelected;
	}
	
	public static PiecePosition newInstance(PiecePosition p) {
		return new PiecePosition(p);
	}
	
}
