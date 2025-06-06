package board;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;


public class BoardFrame extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	Boolean isPieceSelected = false;
	int[] selectedPosition = {-1, -1};
	
	int boardWidth = 720, boardHeight = 720;
	Piece[][] board = new Piece[8][8];
	PiecePosition[][] positions = new PiecePosition[8][8];
	JPanelWithBackground boardPanel;
	public BoardFrame(String title) throws IOException {
		super(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(700,0,boardWidth,boardHeight);
		this.setLayout(new GridBagLayout());
		
		boardPanel = new JPanelWithBackground("images/board.png", boardWidth, boardHeight);
		
		this.add(boardPanel);
		pack();
	}
	
	public void setFirstTime() {
		this.positions[0][0] = new PiecePosition("r", 1, 0, 0);
		this.positions[0][1] = new PiecePosition("n", 1, 0, 1);
		this.positions[0][2] = new PiecePosition("b", 1, 0, 2);
		this.positions[0][3] = new PiecePosition("q", 1, 0, 3);
		this.positions[0][4] = new PiecePosition("k", 1, 0, 4);
		this.positions[0][5] = new PiecePosition("b", 1, 0, 5);
		this.positions[0][6] = new PiecePosition("n", 1, 0, 6);
		this.positions[0][7] = new PiecePosition("r", 1, 0, 7);
		
		for(int j = 0; j<8; j++) {
			this.positions[1][j] = new PiecePosition("p", 1, 1, j);
		}
		
		for(int i = 2; i<6; i++) {
			for(int j = 0; j<8; j++) {
				this.positions[i][j] = new PiecePosition("none", -1, -1, -1);
			}
		}
		
		for(int j = 0; j<8; j++) {
			this.positions[6][j] = new PiecePosition("p", 0, 6, j);
		}
//		this.positions[6][0] = new PiecePosition("none", -1, -1, -1);
//		this.positions[5][2] = new PiecePosition("n", 1, 5, 2);
		
		this.positions[7][0] = new PiecePosition("r", 0, 7, 0);
		this.positions[7][1] = new PiecePosition("n", 0, 7, 1);
		this.positions[7][2] = new PiecePosition("b", 0, 7, 2);
		this.positions[7][3] = new PiecePosition("q", 0, 7, 3);
		this.positions[7][4] = new PiecePosition("k", 0, 7, 4);
		this.positions[7][5] = new PiecePosition("b", 0, 7, 5);
		this.positions[7][6] = new PiecePosition("n", 0, 7, 6);
		this.positions[7][7] = new PiecePosition("r", 0, 7, 7);
		
	}
	
	public void setBoard() throws IOException {
		this.remove(boardPanel);
		boardPanel = new JPanelWithBackground("images/board.png", this.boardWidth, this.boardHeight);
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				board[i][j] = new Piece(positions[i][j].name, positions[i][j].color, i, j);
				board[i][j].addMouseListener(this);
				
				if(positions[i][j].isSelected) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
					System.out.println("in setBoard but in selected");
				} else {
					board[i][j].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				}
				
				boardPanel.add(board[i][j]);
			}
		}
		System.out.println("in setBoard");
        this.add(boardPanel);
        pack();
        this.setVisible(true);
        this.repaint();
    }
	
	int[][] playRook(PiecePosition piece) {
		ArrayList<int[]> moves = new ArrayList<>();
		
		// upwards
		int i;
		for(i = piece.x - 1; i>=0 && positions[i][piece.y].name == "none"; i--) {
			int[] pos = {i, piece.y};
			moves.add(pos);
		}
		// if we can take something
		if(i >= 0 && positions[i][piece.y].color != piece.color) {
			int[] pos = {i, piece.y};
			moves.add(pos);
		}
		
		// downwards
		for(i = piece.x + 1; i<8 && positions[i][piece.y].name == "none"; i++) {
			int[] pos = {i, piece.y};
			moves.add(pos);
		}
		if(i < 8 && positions[i][piece.y].color != piece.color) {
			int[] pos = {i, piece.y};
			moves.add(pos);
		}
		
		// leftwards
		int j;
		for(j = piece.y - 1; j>=0 && positions[piece.x][j].name == "none"; j--) {
			int[] pos = {piece.x, j};
			moves.add(pos);
		}
		if(j >= 0 && positions[piece.x][j].color != piece.color) {
			int[] pos = {piece.x, j};
			moves.add(pos);
		}
		
		// rightwards
		for(j = piece.y + 1; j<8 && positions[piece.x][j].name == "none"; j++) {
			int[] pos = {piece.x, j};
			moves.add(pos);
		}
		if(j < 8 && positions[piece.x][j].color != piece.color) {
			int[] pos = {piece.x, j};
			moves.add(pos);
		}
		
		int[][] possibleMoves = new int[moves.size()][2];
		
		for(i = 0; i<moves.size(); i++) {
			possibleMoves[i][0] = moves.get(i)[0];
			possibleMoves[i][1] = moves.get(i)[1];
			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	int[][] playKnight(PiecePosition piece) {
		ArrayList<int[]> moves = new ArrayList<>();
		
		int[] delRow = {-1, 1, -2, 2};
		int[] delCol = {-1, 1, -2, 2};
		
		for(int i = 0; i<4; i++) {
			for(int j = 0; j<4; j++) {
				if(Math.abs(delRow[i]) == Math.abs(delCol[j])) continue;
				int row = piece.x + delRow[i], col = piece.y + delCol[j];
				if(row < 0 || row >= 8 || col < 0 || col >= 8) continue;
				if(positions[row][col].color == piece.color) continue;
				int[] move = {row, col};
				moves.add(move);
			}
		}
		
		
		int[][] possibleMoves = new int[moves.size()][2];
		
		for(int i = 0; i<moves.size(); i++) {
			possibleMoves[i][0] = moves.get(i)[0];
			possibleMoves[i][1] = moves.get(i)[1];
			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	public void play(int turn) {
		
		int i = selectedPosition[0], j = selectedPosition[1];
		if(i == -1 || j == -1) return;
		
		PiecePosition p = positions[i][j];
		if(p.color != turn) return;
		System.out.println("Here in play");
		int[][] possibleMoves;
		
		switch(p.name) {
		case "r":
			possibleMoves = playRook(p);
			System.out.println("Here in play with rook");
			break;
		case "n":
			possibleMoves = playKnight(p);
			System.out.println("Here in play with knight");
			break;
		case "b":
			
			break;
		case "q":
			
			break;
		case "k":
			
			break;
		case "p":
			
			break;
		default:
			System.out.println("Some error");
			break;
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Piece piece = (Piece)e.getComponent();
		if(piece.name == "none") return;
		int i = piece.x, j = piece.y;
		if(isPieceSelected && !positions[i][j].isSelected) return;
		
		if(!isPieceSelected) {
			positions[i][j].isSelected = true;
			selectedPosition[0] = i;
			selectedPosition[1] = j;
			isPieceSelected = true;
		} else {
			positions[i][j].isSelected = false;
			isPieceSelected = false;
			selectedPosition[0] = -1;
			selectedPosition[1] = -1;
		}
		try {
			this.play(0);
			this.setBoard();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.repaint();
	}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
	
}
