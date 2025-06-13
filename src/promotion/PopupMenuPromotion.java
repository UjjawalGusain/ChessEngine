package promotion;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import board.BoardFrame;
import board.PiecePosition;


public class PopupMenuPromotion extends JPopupMenu implements MouseListener {
	private static final long serialVersionUID = 1L;
	public String promote = "null";
	public BoardFrame board;
	private void setPromotion(String promotion) {
		this.promote = promotion;
	}
	
	public String getPromotion() {
		return this.promote;
	}
	
	public PopupMenuPromotion(BoardFrame board) {
		this.board = board;
		Font fo = new Font("DIALOG", Font.ITALIC, 30);
		JLabel label = new JLabel("Promote pawn to: ");
		label.setFont(fo);
		
		this.add(label);
		
		JMenuItem m1 = new JMenuItem("Queen");
        JMenuItem m2 = new JMenuItem("Rook");
        JMenuItem m3 = new JMenuItem("Bishop");
        JMenuItem m4 = new JMenuItem("Knight");
        
        this.setBorder(new EmptyBorder(10, 50, 10, 50));
        
        m1.addMouseListener(this);
        m2.addMouseListener(this);
        m3.addMouseListener(this);
        m4.addMouseListener(this);
        
        this.add(m1);
        this.add(m2);
        this.add(m3);
        this.add(m4);
	}
	
	public void mousePressed(MouseEvent e) {
		int x = board.promotionPosition[0];
		int y = board.promotionPosition[1];
		
		
		JMenuItem item = (JMenuItem) e.getComponent();
		
		if(item.getText() == "Queen") {
			setPromotion("q");
		} else if(item.getText() == "Rook") {
			setPromotion("r");
		} else if(item.getText() == "Bishop") {
			setPromotion("b");
		} else {
			setPromotion("n");
		}
		board.positions[x][y] = new PiecePosition(getPromotion(), board.positions[x][y].color, x, y);
		if(board.isGettingCheckmated(board.turn, board.positions)) {
			board.checkmate[board.turn] = true;
		}
		try {
			board.setBoard();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}
