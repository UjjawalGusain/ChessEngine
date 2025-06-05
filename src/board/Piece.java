package board;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Piece extends JLabel {
	private static final long serialVersionUID = 1L;
	int color;
	String name;
	int x, y;
	
	public Piece(String name, int color, int x, int y) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		
		set(90, 90);
	}
	
	public void update(String newName, int newColor, int newX, int newY) {
		this.name = newName;
		this.color = newColor;
		this.x = newX;
		this.y = newY;
	}
	
	public void set(int width, int height) {
		if(this.name == "none") {
			BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			ImageIcon transparentIcon = new ImageIcon(transparentImage);
            super.setIcon(transparentIcon);
		} else {
			ImageIcon pieceImageIcon = null;

			if(this.name == "r") {
				pieceImageIcon = this.color == 1 ? new ImageIcon("images/black-rook.png") : new ImageIcon("images/white-rook.png");
			} else if(this.name == "n") {
				pieceImageIcon = this.color == 1 ? new ImageIcon("images/black-knight.png") : new ImageIcon("images/white-knight.png");
			} else if(this.name == "b") {
				pieceImageIcon = this.color == 1 ? new ImageIcon("images/black-bishop.png") : new ImageIcon("images/white-bishop.png");
			} else if(this.name == "q") {
				pieceImageIcon = this.color == 1 ? new ImageIcon("images/black-queen.png") : new ImageIcon("images/white-queen.png");
			} else if(this.name == "k") {
				pieceImageIcon = this.color == 1 ? new ImageIcon("images/black-king.png") : new ImageIcon("images/white-king.png");
			} else if(this.name == "p") {
				pieceImageIcon = this.color == 1 ? new ImageIcon("images/black-pawn.png") : new ImageIcon("images/white-pawn.png");
			}
			
			Image pieceImage = pieceImageIcon.getImage(); 
    		Image scaledPiece = pieceImage.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);  
    		ImageIcon scaledPieceIcon = new ImageIcon(scaledPiece);
    		super.setIcon(scaledPieceIcon);
    		
		}
	}
	
}
