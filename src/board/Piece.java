package board;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Piece extends JLabel {
	private static final long serialVersionUID = 1L;
	public int color;
	public String name;
	int x, y;
	Boolean selected = false;

	// Static image cache
	private static final Map<String, BufferedImage> pieceImages = new HashMap<>();

	static {
		try {
			pieceImages.put("white-p", ImageIO.read(new File("images/white-pawn.png")));
			pieceImages.put("black-p", ImageIO.read(new File("images/black-pawn.png")));
			pieceImages.put("white-r", ImageIO.read(new File("images/white-rook.png")));
			pieceImages.put("black-r", ImageIO.read(new File("images/black-rook.png")));
			pieceImages.put("white-n", ImageIO.read(new File("images/white-knight.png")));
			pieceImages.put("black-n", ImageIO.read(new File("images/black-knight.png")));
			pieceImages.put("white-b", ImageIO.read(new File("images/white-bishop.png")));
			pieceImages.put("black-b", ImageIO.read(new File("images/black-bishop.png")));
			pieceImages.put("white-q", ImageIO.read(new File("images/white-queen.png")));
			pieceImages.put("black-q", ImageIO.read(new File("images/black-queen.png")));
			pieceImages.put("white-k", ImageIO.read(new File("images/white-king.png")));
			pieceImages.put("black-k", ImageIO.read(new File("images/black-king.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Piece(String name, int color, int x, int y) {
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;

		set(90, 90);
	}

	public void toggleSelected() {
		selected = !selected;
	}

	public void update(String newName, int newColor, int newX, int newY) {
		this.name = newName;
		this.color = newColor;
		this.x = newX;
		this.y = newY;
	}

	public void set(int width, int height) {
		if (this.name.equals("none")) {
			BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			super.setIcon(new ImageIcon(transparentImage));
			return;
		}

		String key = (color == 1 ? "black-" : "white-") + name;
		BufferedImage rawImage = pieceImages.get(key);

		if (rawImage != null) {
			Image scaledPiece = rawImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			super.setIcon(new ImageIcon(scaledPiece));
		}
	}
}
