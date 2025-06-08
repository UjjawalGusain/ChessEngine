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
	Boolean[] kingChecked = {false, false};
	int playerOneColor;
	int playerTwoColor;
	int turn = 0;
	
	int boardWidth = 720, boardHeight = 720;
	Piece[][] board = new Piece[8][8];
	PiecePosition[][] positions = new PiecePosition[8][8];
	JPanelWithBackground boardPanel;
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
		
//		this.positions[6][0] = new PiecePosition("none", -1, -1, -1);
//		this.positions[5][2] = new PiecePosition("n", 1, 5, 2);
//		this.positions[6][3] = new PiecePosition("none", -1, -1, -1);
//		this.positions[2][7] = new PiecePosition("r", 0, 2, 7);
//		this.positions[6][3] = new PiecePosition("none", -1, -1, -1);
//		this.positions[6][2] = new PiecePosition("none", -1, -1, -1);
//		this.positions[6][5] = new PiecePosition("none", -1, -1, -1);
//		this.positions[5][4] = new PiecePosition("p", 1, 5, 4);
		
		
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
				
				if(positions[i][j].name == "k" && kingChecked[positions[i][j].color]) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
				} else if(positions[i][j].isSelected) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
//					System.out.println("in setBoard but in selected");
				} else if(positions[i][j].isFutureMove) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
				} else {
					board[i][j].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				}
				
				boardPanel.add(board[i][j]);
			}
		}
//		System.out.println("in setBoard");
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
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
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
//			System.out.printf("%d, %d\n", possibleMoves[i][0], possibleMoves[i][1]);
		}
		return possibleMoves;
	}
	
	int[][] playBishop(PiecePosition piece) {
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
	
	
	int[][] playQueen(PiecePosition piece) {
		int[][] bishopMoves = this.playBishop(piece);
		int[][] rookMoves = this.playRook(piece);
		
		
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
	
	
	int[][] playKing(PiecePosition piece) {
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
	
	
	int[][] playPawn(PiecePosition piece) {
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
	
	private Boolean isGettingChecked(int color) {
		
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
						possibleAttacks.add(playRook(p));
						break;
					case "b":
						possibleAttacks.add(playBishop(p));
						break;
					case "n":
						possibleAttacks.add(playKnight(p));
						break;
					case "q":
						possibleAttacks.add(playQueen(p));
						break;
					case "k":
						possibleAttacks.add(playKing(p));
						break;
					case "p":
						possibleAttacks.add(playPawn(p));
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
		
		private Boolean isGettingChecked(int color, PiecePosition[][] futurePossiblePositions) {
			
			ArrayList<int[][]> possibleAttacks = new ArrayList<>();
			int enemyKingX = -1, enemyKingY = -1;
			
			
			
			for(int i = 0; i<8; i++) {
				for(int j = 0; j<8; j++) {
					PiecePosition p = futurePossiblePositions[i][j];
					if(p.color == color && p.name == "k") {
						enemyKingX = i;
						enemyKingY = j;
					}
					if(p.color != color) {
						switch(p.name) {
						case "r":
							possibleAttacks.add(playRook(p));
							break;
						case "b":
							possibleAttacks.add(playBishop(p));
							break;
						case "n":
							possibleAttacks.add(playKnight(p));
							break;
						case "q":
							possibleAttacks.add(playQueen(p));
							break;
						case "k":
							possibleAttacks.add(playKing(p));
							break;
						case "p":
							possibleAttacks.add(playPawn(p));
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
	
	public int[][] play(int turn) {
		
		int i = selectedPosition[0], j = selectedPosition[1];
		int[][] possibleMoves = new int[0][2];
		if(i == -1 || j == -1) return possibleMoves;
		
		PiecePosition p = positions[i][j];
		if(p.color != turn) return possibleMoves;
		
		
		
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
			possibleMoves = playBishop(p);
			System.out.println("Here in play with bishop");
			break;
		case "q":
			possibleMoves = playQueen(p);
			System.out.println("Here in play with queen");
			break;
		case "k":
			possibleMoves = playKing(p);
			System.out.println("Here in play with king");
			break;
		case "p":
			possibleMoves = playPawn(p);
			System.out.println("Here in play with pawn");
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
	
	
	private Boolean checkInFutureMoves(int[][] futureMoves, int currX, int currY) {
		
		for(int[] move : futureMoves) {
			if(currX == move[0] && currY == move[1]) {
				return true;
			}
		}
		return false;
	}
	
	private void changePosition(int currX, int currY, int[][] futureMoves) {
		
		positions[currX][currY] = positions[selectedPosition[0]][selectedPosition[1]];
		positions[currX][currY].x = currX;
		positions[currX][currY].y = currY;
		positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, currX, currY);
		
		positions[currX][currY].isSelected = false;
		isPieceSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
		selectedPosition[0] = -1;
		selectedPosition[1] = -1;
		this.toggleTurn();
	}
	
	private PiecePosition[][] changePosition(int currX, int currY, int[][] futureMoves, Boolean copy) {
		
		PiecePosition[][] futurePossibleBoard = new PiecePosition[8][8];
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				futurePossibleBoard[i][j] = positions[i][j];
			}
		}

		futurePossibleBoard[currX][currY] = futurePossibleBoard[selectedPosition[0]][selectedPosition[1]];
		futurePossibleBoard[currX][currY].x = currX;
		futurePossibleBoard[currX][currY].y = currY;
		futurePossibleBoard[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, currX, currY);
		
		futurePossibleBoard[currX][currY].isSelected = false;
//		isPieceSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			futurePossibleBoard[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
		return futurePossibleBoard;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Piece piece = (Piece)e.getComponent();
		int i = piece.x, j = piece.y;
		
		Boolean wasFutureMove = false;
		if((selectedPosition[0] != i || selectedPosition[1] != j) && selectedPosition[0] != -1) {
			int[][] futureMoves = this.play(this.turn);
			
			if(this.checkInFutureMoves(futureMoves, i, j)) {
				System.out.printf("Turn 1: %d\n", this.turn);
				
				if(isGettingChecked(this.turn)) {
					PiecePosition[][] pc = this.changePosition(i, j, futureMoves, true);
					if(isGettingChecked(this.turn, pc)) {
						System.out.println("Invalid move.");
						return;
					}
				}
				
				wasFutureMove = true;			
				this.changePosition(i, j, futureMoves);
				
				System.out.printf("Turn 2: %d\n", this.turn);
				int oppTurn = this.turn == 1 ? 0 : 1;
				if(isGettingChecked(this.turn)) {
					kingChecked[this.turn] = true;
				} else {
					kingChecked[this.turn] = false;
				}
				
				if(isGettingChecked(oppTurn)) {
					kingChecked[oppTurn] = true;
				} else {
					kingChecked[oppTurn] = false;
				}
				
			} else {
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

		int[][] futureMoves = this.play(tempTurn);
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
