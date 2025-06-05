package board;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BoardFrame extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	Piece[][] board = new Piece[8][8];
	JPanelWithBackground boardPanel;
	public BoardFrame(String title) throws IOException {
		super(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(700,0,787,805);
		
		boardPanel = new JPanelWithBackground("images/board.png");
		
		boardPanel.setLayout(new GridLayout(8,8));
		this.add(boardPanel);
	}
	
	public void setFirstTime() {
		this.board[0][0] = new Piece("r", 1, 0, 0);
		this.board[0][1] = new Piece("n", 1, 0, 1);
		this.board[0][2] = new Piece("b", 1, 0, 2);
		this.board[0][3] = new Piece("q", 1, 0, 3);
		this.board[0][4] = new Piece("k", 1, 0, 4);
		this.board[0][5] = new Piece("b", 1, 0, 5);
		this.board[0][6] = new Piece("n", 1, 0, 6);
		this.board[0][7] = new Piece("r", 1, 0, 7);
		
		for(int j = 0; j<8; j++) {
			this.board[1][j] = new Piece("p", 1, 1, j);
		}
		
		for(int i = 2; i<6; i++) {
			for(int j = 0; j<8; j++) {
				this.board[i][j] = new Piece("none", -1, i, j);
			}
		}
		
		for(int j = 0; j<8; j++) {
			this.board[6][j] = new Piece("p", 0, 6, j);
		}
		
		this.board[7][0] = new Piece("r", 0, 0, 0);
		this.board[7][1] = new Piece("n", 0, 0, 1);
		this.board[7][2] = new Piece("b", 0, 0, 2);
		this.board[7][3] = new Piece("q", 0, 0, 3);
		this.board[7][4] = new Piece("k", 0, 0, 4);
		this.board[7][5] = new Piece("b", 0, 0, 5);
		this.board[7][6] = new Piece("n", 0, 0, 6);
		this.board[7][7] = new Piece("r", 0, 0, 7);
		
	}
	
	public void setBoard() throws IOException {

		boardPanel = new JPanelWithBackground("images/board.png");
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				board[i][j].addMouseListener(this);
				boardPanel.add(board[i][j]);
			}
		}
        this.add(boardPanel);
        this.setVisible(true);
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Piece p = (Piece)e.getComponent();
		System.out.println(p.name);
		System.out.println(p.color);
		System.out.println(p.x);
		System.out.println(p.y);
		
	}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
	
}
