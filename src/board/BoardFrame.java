package board;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

import promotion.PopupMenuPromotion;
import win.PanelPopup;
import win.WinPanel;


public class BoardFrame extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	Boolean isPieceSelected = false;
	int[] selectedPosition = {-1, -1};
	Boolean[] kingChecked = {false, false};
	int playerOneColor;
	int playerTwoColor;
	public int turn = 0;
	
	JDialog jd;
	
	public int[] promotionPosition = {-1, -1};
	
	public Boolean[] checkmate = {false, false};
	
	int boardWidth = 720, boardHeight = 720;
	public Piece[][] board = new Piece[8][8];
	public PiecePosition[][] positions = new PiecePosition[8][8];
	JPanelWithBackground boardPanel;
	Boolean promotionOccured = false;
	public Boolean isToBePromoted = false;
	public BoardFrame(String title, int playerOneColor, int playerTwoColor) throws IOException {
		super(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(700,0,boardWidth,boardHeight);
		this.setLayout(new GridBagLayout());
		this.playerOneColor = playerOneColor;
		this.playerTwoColor = playerTwoColor;
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
				this.positions[i][j] = new PiecePosition("none", -1, i, j);
			}
		}
		
		for(int j = 0; j<8; j++) {
			this.positions[6][j] = new PiecePosition("p", 0, 6, j);
		}
		
		this.positions[7][0] = new PiecePosition("r", 0, 7, 0);
		this.positions[7][1] = new PiecePosition("n", 0, 7, 1);
		this.positions[7][2] = new PiecePosition("b", 0, 7, 2);
		this.positions[7][3] = new PiecePosition("q", 0, 7, 3);
		this.positions[7][4] = new PiecePosition("k", 0, 7, 4);
		this.positions[7][5] = new PiecePosition("b", 0, 7, 5);
		this.positions[7][6] = new PiecePosition("n", 0, 7, 6);
		this.positions[7][7] = new PiecePosition("r", 0, 7, 7);
		
		
//		this.positions[4][2] = new PiecePosition("b", 0, 4, 2);
//		this.positions[5][5] = new PiecePosition("q", 0, 5, 5);
//		this.positions[6][0] = new PiecePosition("none", -1, -1, -1);
//		this.positions[5][2] = new PiecePosition("n", 1, 5, 2);
//		this.positions[6][3] = new PiecePosition("none", -1, -1, -1);
//		this.positions[2][7] = new PiecePosition("r", 0, 2, 7);
//		this.positions[6][3] = new PiecePosition("none", -1, -1, -1);
//		this.positions[6][2] = new PiecePosition("none", -1, -1, -1);
		this.positions[1][2] = new PiecePosition("p", 0, 1, 2);
		this.positions[0][2] = new PiecePosition("none", -1, 0, 2);
		this.positions[6][2] = new PiecePosition("p", 1, 6, 2);
		this.positions[7][2] = new PiecePosition("none", 1, 7, 2);
		this.positions[0][3] = new PiecePosition("none", 1, 0, 3);
	}
	
	public void setBoard() throws IOException {
//		printBoard(positions);
		this.remove(boardPanel);
		boardPanel = new JPanelWithBackground("images/board.png", this.boardWidth, this.boardHeight);
		
		Boolean hasPromotion = false;
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				board[i][j] = new Piece(positions[i][j].name, positions[i][j].color, i, j);
				board[i][j].addMouseListener(this);
				if(positions[i][j].name == "p" && positions[i][j].promote) {
					
					jd = new JDialog(this);
					jd.setBounds(this.getX() + boardWidth/2 - 130, this.getY() + boardHeight/2 - 130, 300, 200);
					promotionPosition[0] = i;
					promotionPosition[1] = j;
					PopupMenuPromotion pmp = new PopupMenuPromotion(this);
					hasPromotion = true;
					isToBePromoted = true;
					jd.add(pmp);
					pmp.setVisible(true);
					jd.setVisible(true);
				}
				
				if(positions[i][j].name == "k" && kingChecked[positions[i][j].color]) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
				} else if(positions[i][j].isSelected) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				} else if(positions[i][j].isFutureMove) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
				} else {
					board[i][j].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				}
				
				boardPanel.add(board[i][j]);
			}
		}

		if(!isToBePromoted && jd != null) {
			jd.setVisible(false);
		}
		
        this.add(boardPanel);
        if(checkmate[this.turn]) {
        	String winMessage = this.turn == 0 ? "Congrats, black wins!" : "Congrats, white wins!"; 
        	
        	PanelPopup winPanel = new PanelPopup(winMessage);
   
        	PopupFactory pf = new PopupFactory();
        	int winPanelX = boardPanel.getX() + boardPanel.getWidth()/2 - 150;
        	int winPanelY = boardPanel.getY() + boardPanel.getHeight()/2;
        	Popup po = pf.getPopup(boardPanel, winPanel, winPanelX, winPanelY);
        	
        	po.show();
        }
        
        pack();
        this.setVisible(true);
        this.repaint();
        
    }
	
	int[][] playRook(PiecePosition piece, PiecePosition[][] positions) {
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
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	int[][] playKnight(PiecePosition piece, PiecePosition[][] positions) {
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
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	int[][] playBishop(PiecePosition piece, PiecePosition[][] positions) {
		ArrayList<int[]> moves = new ArrayList<>();
		
		// top-right
		for(int k = -1; piece.x + k >= 0 && piece.y + k >= 0 && piece.x + k < 8 && piece.y + k < 8; k--) {
			int row = piece.x + k, col = piece.y + k;
			if(positions[row][col].color == piece.color) break;
			int[] move = {row, col};
			moves.add(move);
			
			if(positions[row][col].name != "none") {
				break;
			}
		}
		
		// top-left
		for(int k = -1; piece.x + k >= 0 && piece.y + Math.abs(k) >= 0 && piece.x + k < 8 && piece.y + Math.abs(k) < 8 ; k--) {
			int row = piece.x + k, col = piece.y + Math.abs(k);
			if(positions[row][col].color == piece.color) break;
			int[] move = {row, col};
			moves.add(move);
			
			if(positions[row][col].name != "none") {
				break;
			}
		}
		
		// bottom-right
		for(int k = -1; piece.x + Math.abs(k) >= 0 && piece.y + Math.abs(k) >= 0 && piece.x + Math.abs(k) < 8 && piece.y + Math.abs(k) < 8 ; k--) {
			int row = piece.x + Math.abs(k), col = piece.y + Math.abs(k);
			if(positions[row][col].color == piece.color) break;
			int[] move = {row, col};
			moves.add(move);
			
			if(positions[row][col].name != "none") {
				break;
			}
		}
		
		// bottom-left
		for(int k = -1; piece.x + Math.abs(k) >= 0 && piece.y + k >= 0 && piece.x + Math.abs(k) < 8 && piece.y + k < 8 ; k--) {
			int row = piece.x + Math.abs(k), col = piece.y + k;
			if(positions[row][col].color == piece.color) break;
			int[] move = {row, col};
			moves.add(move);
			
			if(positions[row][col].name != "none") {
				break;
			}
		}
		
		int[][] possibleMoves = new int[moves.size()][2];
		
		for(int i = 0; i<moves.size(); i++) {
			possibleMoves[i][0] = moves.get(i)[0];
			possibleMoves[i][1] = moves.get(i)[1];
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	
	int[][] playQueen(PiecePosition piece, PiecePosition[][] positions) {
		int[][] bishopMoves = this.playBishop(piece, positions);
		int[][] rookMoves = this.playRook(piece, positions);
		
		
		int[][] possibleMoves = new int[bishopMoves.length + rookMoves.length][2];
		int k = 0;
		for(int i = 0; i<bishopMoves.length; i++) {
			possibleMoves[k][0] = bishopMoves[i][0];
			possibleMoves[k][1] = bishopMoves[i][1];
//			System.out.printf("%d, %d\n", possibleMoves[k][0], possibleMoves[k][1]);
			k++;
		}
		
		for(int i = 0; i<rookMoves.length; i++) {
			possibleMoves[k][0] = rookMoves[i][0];
			possibleMoves[k][1] = rookMoves[i][1];
//			System.out.printf("%d, %d\n", possibleMoves[k][0], possibleMoves[k][1]);
			k++;
		}
		
		return possibleMoves;
	}
	
	
	int[][] playKing(PiecePosition piece, PiecePosition[][] positions) {
		ArrayList<int[]> moves = new ArrayList<>();
		
		int[] delRow = {-1, 1, 0};
		int[] delCol = {-1, 1, 0};
		
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<3; j++) {
				if(delRow[i] == 0 && delCol[j] == 0) continue;
				
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
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	
	int[][] playPawn(PiecePosition piece, PiecePosition[][] positions) {
		ArrayList<int[]> moves = new ArrayList<>();
		
		// if black
		if(piece.color == 1) {
			if(piece.x == 1 && positions[piece.x+1][piece.y].name == "none" && positions[piece.x+2][piece.y].name == "none") {
				int[] move = {piece.x + 2, piece.y};
				moves.add(move);
			} 
			
			if(piece.x + 1 < 8 && positions[piece.x+1][piece.y].name == "none") {
				int[] move = {piece.x + 1, piece.y};
				moves.add(move);
			}
			
			if(piece.x + 1 < 8 && piece.y + 1 < 8 && positions[piece.x + 1][piece.y + 1].color != piece.color && positions[piece.x + 1][piece.y + 1].name != "none") {
				int[] move = {piece.x + 1, piece.y + 1};
				moves.add(move);
			}
			
			if(piece.x + 1 < 8 && piece.y - 1 >= 0 && positions[piece.x + 1][piece.y - 1].color != piece.color && positions[piece.x + 1][piece.y - 1].name != "none") {
				int[] move = {piece.x + 1, piece.y - 1};
				moves.add(move);
			}
			
		} else if(piece.color == 0) { // white
			if(piece.x == 6 && positions[piece.x-1][piece.y].name == "none" && positions[piece.x-2][piece.y].name == "none") {
				int[] move = {piece.x - 2, piece.y};
				moves.add(move);
			} 
			
			if(piece.x - 1 >= 0 && positions[piece.x-1][piece.y].name == "none") {
				int[] move = {piece.x - 1, piece.y};
				moves.add(move);
			}
			
			if(piece.x - 1 >= 0 && piece.y - 1 >= 0 && positions[piece.x - 1][piece.y - 1].color != piece.color && positions[piece.x - 1][piece.y - 1].name != "none") {
				int[] move = {piece.x - 1, piece.y - 1};
				moves.add(move);
			}
			
			if(piece.x - 1 >= 0 && piece.y + 1 < 8 && positions[piece.x - 1][piece.y + 1].color != piece.color && positions[piece.x - 1][piece.y + 1].name != "none") {
				int[] move = {piece.x - 1, piece.y + 1};
				moves.add(move);
			}
		}
		
		int[][] possibleMoves = new int[moves.size()][2];
		
		for(int i = 0; i<moves.size(); i++) {
			possibleMoves[i][0] = moves.get(i)[0];
			possibleMoves[i][1] = moves.get(i)[1];
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	private Boolean isGettingChecked(int color, PiecePosition[][] positions) {
		
		ArrayList<int[][]> possibleAttacks = new ArrayList<>();
		int enemyKingX = -1, enemyKingY = -1;
		
		
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				PiecePosition p = positions[i][j];
				if(p.color == color && p.name == "k") {
					enemyKingX = i;
					enemyKingY = j;
				}
				if(p.color != color) {
					switch(p.name) {
					case "r":
						possibleAttacks.add(playRook(p, positions));
						break;
					case "b":
						possibleAttacks.add(playBishop(p, positions));
						break;
					case "n":
						possibleAttacks.add(playKnight(p, positions));
						break;
					case "q":
						possibleAttacks.add(playQueen(p, positions));
						break;
					case "k":
						possibleAttacks.add(playKing(p, positions));
						break;
					case "p":
						possibleAttacks.add(playPawn(p, positions));
						break;
					default:
						break;
					}
				}
			}
		}
		
		for(int[][] attacks : possibleAttacks) {
			for(int[] attack : attacks) {
				int x = attack[0], y = attack[1];
				if(x == enemyKingX && y == enemyKingY) return true;
			}
		}
		return false;
	}
	
	public static void printBoard(PiecePosition[][] positions) {
		
		System.out.println("Printing Positions Start");
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				System.out.printf("%s ", positions[i][j].name);
				if(positions[i][j].promote) {
					System.out.printf("<-promote ");
				}
			}
			System.out.printf("\n");
		}
		
		System.out.println("Printing Positions End");
	}
	
	public int[][] play(int turn, PiecePosition[][] positions) {
		
		int i = selectedPosition[0], j = selectedPosition[1];
		int[][] possibleMoves = new int[0][2];
		if(i == -1 || j == -1) return possibleMoves;
		
		PiecePosition p = positions[i][j];
		if(p.color != turn) return possibleMoves;
		
		
		
		switch(p.name) {
		case "r":
			possibleMoves = playRook(p, positions);
//			System.out.println("Here in play with rook");
			break;
		case "n":
			possibleMoves = playKnight(p, positions);
//			System.out.println("Here in play with knight");
			break;
		case "b":
			possibleMoves = playBishop(p, positions);
//			System.out.println("Here in play with bishop");
			break;
		case "q":
			possibleMoves = playQueen(p, positions);
//			System.out.println("Here in play with queen");
			break;
		case "k":
			possibleMoves = playKing(p, positions);
//			System.out.println("Here in play with king");
			break;
		case "p":
			possibleMoves = playPawn(p, positions);
//			System.out.println("Here in play with pawn");
			break;
		default:
			System.out.println("Selected none");
			break;
		}
		return possibleMoves;
	}
	
	private void toggleTurn() {
		this.turn = this.turn == 1 ? 0 : 1;
	}
	
	
	private Boolean checkInFutureMoves(int[][] futureMoves, int currX, int currY, PiecePosition[][] positions) {
		
		for(int[] move : futureMoves) {
			if(currX == move[0] && currY == move[1]) {
				return true;
			}
		}
		return false;
	}
	
	private void changePosition(int currX, int currY, int[][] futureMoves, PiecePosition[][] positions) {
		
		positions[currX][currY] = positions[selectedPosition[0]][selectedPosition[1]];
		positions[currX][currY].x = currX;
		positions[currX][currY].y = currY;
		
		
		positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, currX, currY);
		if(positions[currX][currY].name == "p") {
			if(positions[currX][currY].color == 1) {
				if(currX == 7) {
					positions[currX][currY].promote = true;
				}
			} else {
				if(currX == 0) {
					positions[currX][currY].promote = true;
				}
			}
		}
		
		positions[currX][currY].isSelected = false;
		isPieceSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
		selectedPosition[0] = -1;
		selectedPosition[1] = -1;
		this.toggleTurn();
	}
	
	private void checkCanChangePosition(int currX, int currY, int[][] futureMoves, PiecePosition[][] positions) {
		
		positions[currX][currY] = positions[selectedPosition[0]][selectedPosition[1]];
		positions[currX][currY].x = currX;
		positions[currX][currY].y = currY;
		positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, currX, currY);
		
		positions[currX][currY].isSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
	}
	
	
	private PiecePosition[][] getCopy(PiecePosition[][] positions) {
		PiecePosition[][] copy = new PiecePosition[8][8];
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				copy[i][j] = new PiecePosition(positions[i][j].name, positions[i][j].color, positions[i][j].x, positions[i][j].y, positions[i][j].isSelected, positions[i][j].isFutureMove);
			}
		}
		return copy;
	}
	
	private Boolean changeAndCheckForCheckmate(int currX, int currY, int prevX, int prevY, PiecePosition[][] positions) {
		
		positions[currX][currY] = positions[prevX][prevY];
		positions[currX][currY].x = currX;
		positions[currX][currY].y = currY;
		positions[prevX][prevY] = new PiecePosition("none", -1, currX, currY);
		
		if(isGettingChecked(this.turn, positions)) {
			return true;
		} 
		return false;
	}
	
	private int checkCheckmateForPiece(int turn, PiecePosition[][] positions, int i, int j) {
		
		int count = 0;
		if(positions[i][j].color == turn) {
			PiecePosition p = positions[i][j];
			
			int[][] moves = new int[0][2];
			
			switch(positions[i][j].name) {
			case "r":
				moves = this.playRook(p, positions);
				break;
			case "n":
				moves = this.playKnight(p, positions);
				break;
			case "b":
				moves = this.playBishop(p, positions);
				break;
			case "q":
				moves = this.playQueen(p, positions);
				break;
			case "k":
				moves = this.playKing(p, positions);
				break;
			case "p":
				moves = this.playPawn(p, positions);
				break;
			default:
				break;
			}
			
			for(int[] move : moves) {
				PiecePosition[][] pc = getCopy(positions);
				
				if(!changeAndCheckForCheckmate(move[0], move[1], p.x, p.y, pc)) {
					count++;
				} 
			}
		}	
		
		return count;
	}
	
	public Boolean isGettingCheckmated(int turn, PiecePosition[][] positions) {
		
		int count = 0;
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				
				count += checkCheckmateForPiece(turn, positions, i, j);
				if(count > 0) return false;
			}
		}
		return true;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(isToBePromoted) return;
		Piece piece = (Piece)e.getComponent();
		int i = piece.x, j = piece.y;
		
		Boolean wasFutureMove = false;
		if((selectedPosition[0] != i || selectedPosition[1] != j) && selectedPosition[0] != -1) {
			int[][] futureMoves = this.play(this.turn, positions);
			if(this.checkInFutureMoves(futureMoves, i, j, positions)) {
				PiecePosition[][] pc = getCopy(positions);
				checkCanChangePosition(i, j, futureMoves, pc);
				if(isGettingChecked(this.turn, pc)) {
					System.out.println("Invalid move.");
					return;
				}

				wasFutureMove = true;			
				changePosition(i, j, futureMoves, positions);
				
				int oppTurn = this.turn == 1 ? 0 : 1;
				if(isGettingChecked(this.turn, positions)) {
					kingChecked[this.turn] = true;
//					System.out.printf("%d is checked right now\n", this.turn);
					
					// Go through every possible move of this.turn, and check if for any possible possible isGettingChecked fails
					if(isGettingCheckmated(this.turn, positions)) {
//						System.out.println("Checkmate");
						checkmate[this.turn] = true;
					}
					
				} else {
					kingChecked[this.turn] = false;
				}
				
				if(isGettingChecked(oppTurn, positions)) {
					kingChecked[oppTurn] = true;
				} else {
					kingChecked[oppTurn] = false;
				}
				
			} else {
				try {
					this.setBoard();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
			
		}
		
		int tempTurn = this.turn;
		if(!wasFutureMove) {
			selectedPosition[0] = i;
			selectedPosition[1] = j;
		} else {
			tempTurn = tempTurn == 1 ? 0 : 1; 
		}

		int[][] futureMoves = this.play(tempTurn, positions);
		if(!isPieceSelected && !wasFutureMove) {
			positions[i][j].isSelected = true;
			isPieceSelected = true;

			for(int i1 = 0; i1<futureMoves.length; i1++) {
				positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = true;
			}
			
		} else if(!wasFutureMove) {	
			positions[i][j].isSelected = false;
			isPieceSelected = false;
			
			
			for(int i1 = 0; i1<futureMoves.length; i1++) {
				positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
			}
			selectedPosition[0] = -1;
			selectedPosition[1] = -1;
		}
		
		try {
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
