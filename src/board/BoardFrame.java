package board;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import Engine.Scorer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import promotion.PopupMenuPromotion;
import win.PanelPopup;
import timer.Helper;

public class BoardFrame extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;
	public Boolean isPieceSelected = false;
	public int[] selectedPosition = {-1, -1};
	public Boolean[] kingChecked = {false, false};
	int playerOneColor;
	int playerTwoColor;
	public int turn = 0;
	public int[] queenSideKingRookMoved = {-1, -1};
	public int[] kingSideKingRookMoved = {-1, -1};
	public Boolean stalemate = false;
	Popup po = null;
	public Move moves[] = new Move[2500];
	public int currMove = 0;
	Helper helper = new Helper();
	JDialog jd;
	
	
	public int[] promotionPosition = {-1, -1};
	
	public Boolean[] checkmate = {false, false};
	
	int boardWidth = 720, boardHeight = 720;
	public Piece[][] board = new Piece[8][8];
	public PiecePosition[][] positions = new PiecePosition[8][8];
	JPanelWithBackground boardPanel;
	Boolean promotionOccured = false;
	public Boolean isToBePromoted = false;
	public JButton button = new JButton("Undo");
	public JButton score = new JButton("Play Best");
	public Boolean isBoardVisible = true;
	private BufferedImage boardImage = ImageIO.read(new File("images/board.png"));
	

	public BoardFrame(String title, int playerOneColor, int playerTwoColor) throws IOException {
		super(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(700,0,boardWidth,boardHeight);
		this.setLayout(new GridBagLayout());
		this.playerOneColor = playerOneColor;
		this.playerTwoColor = playerTwoColor;
		boardPanel = new JPanelWithBackground(boardImage, boardWidth, boardHeight);
		BoardFrame bf = this;
		button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	undoMove();
            	toggleTurn();
            }
        });
		
		score.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("New Score start");
				int maxScore = Integer.MIN_VALUE;
				int x = -1, y = -1;
				bf.isBoardVisible = false;
				int xx = -1, yy = -1;
				for(int i = 0; i<8; i++) {
					for(int j = 0; j<8; j++) {
						
						PiecePosition piece = positions[i][j];
						
						int score = Helper.recursiveBoardRunner(bf, piece, 4, turn, 1);
						if(score >= maxScore && Helper.bestMoves[0] != -1) {
							x = i;
							y = j;
							
							xx = Helper.bestMoves[0];
							yy = Helper.bestMoves[1];
							maxScore = score;
							System.out.printf("Last Best move from: [%d][%d] to [%d][%d] with score: %d\n\n", x, y, xx, yy, score);
						}
						
						selectedPosition[0] = -1;
				    	selectedPosition[1] = -1;
				  

					}
				}
				bf.isBoardVisible = true;
				System.out.printf("\nMax Score: %d\n", maxScore);
				System.out.printf("Best move from: [%d][%d] to [%d][%d]\n\n", x, y, xx, yy);
				
				int[][] fm = play(turn, positions);
				selectedPosition[0] = x;
		    	selectedPosition[1] = y;
				changePosition(xx, yy, fm, positions);
				try {
					setBoard();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
			
		this.add(boardPanel);
		this.add(button);
		this.add(score);
		pack();
	}
	
	public void setIsVisible(Boolean isBoardVisible) {
		this.isBoardVisible = isBoardVisible;
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
		
		
		
		
// 		Below are just experimentations on positions to check out possible outcomes
//		this.positions[3][1] = new PiecePosition("p", 1, 3, 1);
//		this.positions[2][3] = new PiecePosition("q", 0, 2, 3);
//		this.positions[2][5] = new PiecePosition("q", 0, 2, 5);
//		
//		this.positions[0][0] = new PiecePosition("none", -1, 0, 0);
//		this.positions[0][1] = new PiecePosition("none", -1, 0, 1);
//		this.positions[0][2] = new PiecePosition("none", -1, 0, 2);
//		this.positions[0][3] = new PiecePosition("none", -1, 0, 3);
//		this.positions[0][5] = new PiecePosition("none", -1, 0, 5);
//		this.positions[0][6] = new PiecePosition("none", -1, 0, 6);
//		this.positions[0][7] = new PiecePosition("none", -1, 0, 7);
//		
//		for(int j = 0; j<8; j++) {
//			this.positions[1][j] = new PiecePosition("none", -1, 1, j);
//		}
////		
//		this.positions[1][0] = new PiecePosition("none", -1, 1, 0);
//		this.positions[4][0] = new PiecePosition("r", 0, 4, 0);
//		this.positions[0][1] = new PiecePosition("none", -1, 0, 1);
//		this.positions[0][2] = new PiecePosition("none", -1, 0, 2);
//		this.positions[0][3] = new PiecePosition("none", -1, 0, 3);
//		this.positions[0][5] = new PiecePosition("none", -1, 0, 5);
//		this.positions[0][6] = new PiecePosition("none", -1, 0, 6);
//		
//		this.positions[7][1] = new PiecePosition("none", -1, 7, 1);
//		this.positions[7][2] = new PiecePosition("none", -1, 7, 2);
//		this.positions[7][3] = new PiecePosition("none", -1, 7, 3);
//		this.positions[7][5] = new PiecePosition("none", -1, 7, 5);
//		this.positions[7][6] = new PiecePosition("none", -1, 7, 6);
//		
//		this.positions[4][2] = new PiecePosition("b", 0, 4, 2);
//		this.positions[5][5] = new PiecePosition("q", 0, 5, 5);
//		this.positions[5][2] = new PiecePosition("n", 1, 5, 2);
//		this.positions[2][7] = new PiecePosition("r", 0, 2, 7);
//		this.positions[1][2] = new PiecePosition("p", 0, 1, 2);
//		this.positions[0][2] = new PiecePosition("none", -1, 0, 2);
//		this.positions[6][2] = new PiecePosition("p", 1, 6, 2);
//		this.positions[7][2] = new PiecePosition("none", 1, 7, 2);
//		this.positions[0][3] = new PiecePosition("none", 1, 0, 3);
//		this.positions[0][0] = new PiecePosition("n", 1, 0, 0);
//		this.positions[1][1] = new PiecePosition("p", 0, 1, 1);
//		this.positions[0][1] = new PiecePosition("none", -1, 0, 1);
	}
	
	public void setBoard() throws IOException {
		
		
		
		
//		System.out.printf("Score for %d: %d\n", turn, Scorer.getScore(positions, turn));
		if(po != null && !checkmate[0] && !checkmate[1] && !stalemate) {
			po.hide();
		}
		
		this.remove(boardPanel);
		boardPanel = new JPanelWithBackground(boardImage, this.boardWidth, this.boardHeight);
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				board[i][j] = new Piece(positions[i][j].name, positions[i][j].color, i, j);
				board[i][j].addMouseListener(this);
				if(positions[i][j].name == "p" && positions[i][j].promote) {
					
					jd = new JDialog(this);
					jd.setBounds(this.getX() + boardWidth/2 - 130, this.getY() + boardHeight/2 - 130, 300, 150);
					jd.setUndecorated(true);
					promotionPosition[0] = i;
					promotionPosition[1] = j;
					PopupMenuPromotion pmp = new PopupMenuPromotion(this);
					isToBePromoted = true;
					moves[currMove - 1].isPromotion = true;
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
        if(selectedPosition[0] == -1 && (checkmate[this.turn] || stalemate)) {
        	
        	String message;
        	if(checkmate[this.turn])
        		message = this.turn == 0 ? "Congrats, black wins!" : "Congrats, white wins!"; 
        	else
        		message = "Ohh! Stalemate. Draw...<br>Yeah I dont like this either. <br>For me, you won";
        	
        	PanelPopup winPanel = new PanelPopup(message);
        	
        	PopupFactory pf = new PopupFactory();
        	int winPanelX = boardPanel.getX() + boardPanel.getWidth()/2 - 150;
        	int winPanelY = boardPanel.getY() + boardPanel.getHeight()/2;
        	po = pf.getPopup(boardPanel, winPanel, winPanelX, winPanelY);

        	po.show();
        }
        
        pack();
        this.setVisible(isBoardVisible);

        this.repaint();
    }
	
	
	void printLastMove() {
		int moveIndex = currMove - 1;
		if(moveIndex < 0) return;
		
		if(moves[moveIndex].castleMove == false) {
			System.out.printf("%s goes from %d,%d to %d,%d capturing %s\n", moves[moveIndex].prevPosition.name, moves[moveIndex].prevPosition.x, moves[moveIndex].prevPosition.y
					, moves[moveIndex].currPosition.x, moves[moveIndex].currPosition.y, moves[moveIndex].gotRemoved.name);
			return;
		}
		
		System.out.printf("Castling %s from %d,%d with %s from %d,%d to %s[%d][%d] and %s[%d][%d]\n",
				this.moves[moveIndex].prevPositionKing.name,
				this.moves[moveIndex].prevPositionKing.x,
				this.moves[moveIndex].prevPositionKing.y,
				this.moves[moveIndex].prevPositionRook.name,
				this.moves[moveIndex].prevPositionRook.x,
				this.moves[moveIndex].prevPositionRook.y,
				this.moves[moveIndex].currPositionKing.name,
				this.moves[moveIndex].currPositionKing.x,
				this.moves[moveIndex].currPositionKing.y,
				this.moves[moveIndex].currPositionRook.name,
				this.moves[moveIndex].currPositionRook.x,
				this.moves[moveIndex].currPositionRook.y
				);
		
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
			k++;
		}
		
		for(int i = 0; i<rookMoves.length; i++) {
			possibleMoves[k][0] = rookMoves[i][0];
			possibleMoves[k][1] = rookMoves[i][1];
			k++;
		}
		
		return possibleMoves;
	}
	
	
	int[][] playKing(PiecePosition piece, PiecePosition[][] positions, Boolean canBeCastled) {
		ArrayList<int[]> moves = new ArrayList<>();
		if(canBeCastled) {
			Boolean canBeKingSideCastled = true;
			canBeKingSideCastled = kingSideKingRookMoved[piece.color] == -1;
			canBeKingSideCastled = canBeKingSideCastled && positions[piece.x][piece.y + 3].name == "r" && positions[piece.x][piece.y + 3].color == piece.color;
			for(int i = 0; i<4; i++) {
				canBeKingSideCastled = canBeKingSideCastled && !isPositionGettingAttacked(piece.color, positions, piece.x, piece.y + i);
			}
			
			for(int i = 1; i<3; i++) {
				canBeKingSideCastled = canBeKingSideCastled && positions[piece.x][piece.y + i].name == "none";
			}
			if(canBeKingSideCastled) {
				int[] move = {piece.x, piece.y + 2};
				moves.add(move);
			}
			
			Boolean canBeQueenSideCastled = true;
			canBeQueenSideCastled = queenSideKingRookMoved[piece.color] == -1;
			canBeQueenSideCastled = canBeQueenSideCastled && positions[piece.x][piece.y - 4].name == "r" && positions[piece.x][piece.y - 4].color == piece.color;
			for(int i = 0; i<5; i++) {
				canBeQueenSideCastled = canBeQueenSideCastled && !isPositionGettingAttacked(piece.color, positions, piece.x, piece.y - i);
			}
			
			for(int i = 1; i<4; i++) {
				canBeQueenSideCastled = canBeQueenSideCastled && positions[piece.x][piece.y - i].name == "none";
			}
			
			if(canBeQueenSideCastled) {
				int[] move = {piece.x, piece.y - 2};
				moves.add(move);
			}
		}
		
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
		}
		return possibleMoves;
	}
	
	
	int[][] playPawn(PiecePosition piece, PiecePosition[][] positions) {
		ArrayList<int[]> moves = new ArrayList<>();
		
		// if black
		if(piece.color == 1) {
			
			// en passant
			if(piece.x == 4 && currMove > 0) {
				int leftY = piece.y - 1, rightY = piece.y + 1;
				if(leftY >= 0 && this.moves[currMove - 1].currPosition.x == piece.x && this.moves[currMove - 1].currPosition.y == leftY && this.moves[currMove - 1].prevPosition.color != this.turn && this.moves[currMove - 1].prevPosition.name == "p") {
					if(this.moves[currMove-1].prevPosition.x == piece.x + 2 && this.moves[currMove-1].prevPosition.y == leftY) {
						int[] move = {piece.x + 1, leftY};
						moves.add(move);
					}
				}

				if(rightY < 8 && this.moves[currMove - 1].currPosition.x == piece.x && this.moves[currMove - 1].currPosition.y == rightY && this.moves[currMove - 1].prevPosition.color != this.turn && this.moves[currMove - 1].prevPosition.name == "p") {
					
					if(this.moves[currMove-1].prevPosition.x == piece.x + 2 && this.moves[currMove-1].prevPosition.y == rightY) {
						int[] move = {piece.x + 1, rightY};
						moves.add(move);
					}
					
				}
				
			}
			
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
			
			// en passant
			if(piece.x == 3 && currMove > 0) {
				int leftY = piece.y - 1, rightY = piece.y + 1;
				if(leftY >= 0 && this.moves[currMove - 1].currPosition.x == piece.x && this.moves[currMove - 1].currPosition.y == leftY && this.moves[currMove - 1].currPosition.color != this.turn ) {
					if(this.moves[currMove-1].prevPosition.x == piece.x - 2 && this.moves[currMove-1].prevPosition.y == leftY) {
						int[] move = {piece.x - 1, leftY};
						moves.add(move);
					}
				}
				
				if(rightY < 8 && this.moves[currMove - 1].currPosition.x == piece.x && this.moves[currMove - 1].currPosition.y == rightY && this.moves[currMove - 1].currPosition.color != this.turn ) {
					
					if(this.moves[currMove-1].prevPosition.x == piece.x - 2 && this.moves[currMove-1].prevPosition.y == rightY) {
						int[] move = {piece.x - 1, rightY};
						moves.add(move);
					}
					
				}
				
			}
			
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
		}
		return possibleMoves;
	}
	
	public Boolean isGettingChecked(int color, PiecePosition[][] positions) {
		
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
						possibleAttacks.add(playKing(p, positions, false));
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
	
	private Boolean isPositionGettingAttacked(int color, PiecePosition[][] positions, int x, int y) {
		
		ArrayList<int[][]> possibleAttacks = new ArrayList<>();
				
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				PiecePosition p = positions[i][j];

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
						possibleAttacks.add(playKing(p, positions, false));
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
				if(attack[0] == x && attack[1] == y) 
					return true;
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
	
	public static void printBoardWithPositions(PiecePosition[][] positions) {
		
		System.out.println("Printing Positions Start");
		
		for(int i = 0; i<8; i++) {
			System.out.printf("%d -> ", i);
			for(int j = 0; j<8; j++) {
				if(positions[i][j].name == "none") {
					System.out.printf("o[%d][%d] ", positions[i][j].x, positions[i][j].y);
				} else {
					System.out.printf("%s[%d][%d] ", positions[i][j].name, positions[i][j].x, positions[i][j].y);
				}
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
			break;
		case "n":
			possibleMoves = playKnight(p, positions);
			break;
		case "b":
			possibleMoves = playBishop(p, positions);
			break;
		case "q":
			possibleMoves = playQueen(p, positions);
			break;
		case "k":
			possibleMoves = playKing(p, positions, true);
			break;
		case "p":
			possibleMoves = playPawn(p, positions);
			break;
		default:
//			System.out.println("Selected none");
			break;
		}
		
		return possibleMoves;
	}
	
	public void toggleTurn() {
		this.turn = this.turn == 1 ? 0 : 1;
	}
	
	public void undoMove() {
		if(currMove == 0) return;
//		System.out.println("Before");
//		printLastMove();
		
		currMove--;
		Move currentMove = moves[currMove];
		
		if(currentMove.castleMove) {
			int x1 = currentMove.prevPositionKing.x;
			int y1 = currentMove.prevPositionKing.y;
			
			int x2 = currentMove.prevPositionRook.x;
			int y2 = currentMove.prevPositionRook.y;
			
			positions[x1][y1] = new PiecePosition(positions[currentMove.currPositionKing.x][currentMove.currPositionKing.y]);
			positions[x2][y2] = new PiecePosition(positions[currentMove.currPositionRook.x][currentMove.currPositionRook.y]);
			
			positions[currentMove.prevPositionKing.x][currentMove.prevPositionKing.y].x = x1;
			positions[currentMove.prevPositionKing.x][currentMove.prevPositionKing.y].y = y1;
			
			positions[currentMove.prevPositionRook.x][currentMove.prevPositionRook.y].x = x2;
			positions[currentMove.prevPositionRook.x][currentMove.prevPositionRook.y].y = y2;
			
			if(currMove == kingSideKingRookMoved[currentMove.prevPositionKing.color]) {
				kingSideKingRookMoved[currentMove.prevPositionKing.color] = -1;
			} else if(currMove == queenSideKingRookMoved[currentMove.prevPositionKing.color]) {
				queenSideKingRookMoved[currentMove.prevPositionKing.color] = -1;
			}
			
			positions[currentMove.currPositionKing.x][currentMove.currPositionKing.y] = new PiecePosition("none", -1, currentMove.currPositionKing.x, currentMove.currPositionKing.y);
			positions[currentMove.currPositionRook.x][currentMove.currPositionRook.y] = new PiecePosition("none", -1, currentMove.currPositionRook.x, currentMove.currPositionRook.y);
		} else {
			
			int x1 = positions[currentMove.prevPosition.x][currentMove.prevPosition.y].x;
			int y1 = positions[currentMove.prevPosition.x][currentMove.prevPosition.y].y;
			
			int x2 = positions[currentMove.currPosition.x][currentMove.currPosition.y].x;
			int y2 = positions[currentMove.currPosition.x][currentMove.currPosition.y].y;
			
			
			int x3 = positions[currentMove.gotRemoved.x][currentMove.gotRemoved.y].x;
			int y3 = positions[currentMove.gotRemoved.x][currentMove.gotRemoved.y].y;
			
			String name = currentMove.gotRemoved.name;
			int color = currentMove.gotRemoved.color;
			
			positions[currentMove.prevPosition.x][currentMove.prevPosition.y] = new PiecePosition(positions[currentMove.currPosition.x][currentMove.currPosition.y]);
			positions[currentMove.prevPosition.x][currentMove.prevPosition.y].x = x1;
			positions[currentMove.prevPosition.x][currentMove.prevPosition.y].y = y1;
			if(currentMove.isPromotion)
				positions[currentMove.prevPosition.x][currentMove.prevPosition.y].name = "p";
			
			
			if(x3 > x2) {
				positions[x3][y3] = new PiecePosition("p", 1, x3, y3);
				positions[x2][y2] = new PiecePosition("none", -1, x2, y2);
			} else if(x3 < x2) {
				positions[x3][y3] = new PiecePosition("p", 0, x3, y3);
				positions[x2][y2] = new PiecePosition("none", -1, x2, y2);
			} else {
				positions[currentMove.currPosition.x][currentMove.currPosition.y] = new PiecePosition(name, color, x3, y3);
			}
			
			
		}
		
		
		
		moves[currMove] = null;
		
		checkmate[0] = false;
		checkmate[1] = false;
		stalemate = false;
		try {
			if(isBoardVisible) this.setBoard();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Boolean checkInFutureMoves(int[][] futureMoves, int currX, int currY, PiecePosition[][] positions) {
		
		for(int[] move : futureMoves) {
			if(currX == move[0] && currY == move[1]) {
				return true;
			}
		}
		return false;
	}
	
	public void changePosition(int currX, int currY, int[][] futureMoves, PiecePosition[][] positions) {
				
		if(positions[selectedPosition[0]][selectedPosition[1]].name == "r" && (positions[selectedPosition[0]][selectedPosition[1]].x == 7 || positions[selectedPosition[0]][selectedPosition[1]].x == 0)) { 
			
			if(positions[selectedPosition[0]][selectedPosition[1]].y == 7) {
				this.kingSideKingRookMoved[positions[selectedPosition[0]][selectedPosition[1]].color] = currMove;
			} else if(positions[selectedPosition[0]][selectedPosition[1]].y == 0) {
				this.queenSideKingRookMoved[positions[selectedPosition[0]][selectedPosition[1]].color] = currMove;
			}
			
		} 
		else if(positions[selectedPosition[0]][selectedPosition[1]].name == "k") {
			this.kingSideKingRookMoved[positions[selectedPosition[0]][selectedPosition[1]].color] = currMove;
			this.queenSideKingRookMoved[positions[selectedPosition[0]][selectedPosition[1]].color] = currMove;
		}
		
		// is being castled
		Boolean castled = false, left = false, right = false;
		if(positions[selectedPosition[0]][selectedPosition[1]].name == "k") {
			if(Math.abs(currY - selectedPosition[1]) == 2) {
				castled = true;
				if(currY - selectedPosition[1] == 2) {
					right = true;
					moves[currMove] = new Move(true, 
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]), 
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1] + 3]),
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1] + 2]),
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1] + 1]));
					positions[selectedPosition[0]][selectedPosition[1] + 2] = new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]);
					positions[selectedPosition[0]][5] = new PiecePosition(positions[selectedPosition[0]][7]);
					positions[selectedPosition[0]][5].y = 5;
					positions[selectedPosition[0]][selectedPosition[1] + 2].y = currY;
					
					
					positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, selectedPosition[0], selectedPosition[1]);
					positions[selectedPosition[0]][7] = new PiecePosition("none", -1, selectedPosition[0], 7);
					positions[selectedPosition[0]][4] = new PiecePosition("none", -1, selectedPosition[0], 4);
		
					
				} else if(selectedPosition[1] - currY == 2) {
					left = true;
					moves[currMove] = new Move(true, 
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]), 
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1] - 4]),
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1] - 2]),
							new PiecePosition(positions[selectedPosition[0]][selectedPosition[1] - 1]));
					positions[selectedPosition[0]][selectedPosition[1] - 2] = new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]);
					positions[selectedPosition[0]][3] = new PiecePosition(positions[selectedPosition[0]][0]);
					positions[selectedPosition[0]][3].y = 3;
					positions[selectedPosition[0]][selectedPosition[1] - 2].y = selectedPosition[1] - 2;
					positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, selectedPosition[0], selectedPosition[1]);
					
					positions[selectedPosition[0]][0] = new PiecePosition("none", -1, selectedPosition[0], 0);
					positions[selectedPosition[0]][4] = new PiecePosition("none", -1, selectedPosition[0], 4);
				}
				moves[currMove].prevPositionKing.name = "k";
				moves[currMove].prevPositionRook.name = "r";
			}
		}
		
		
		// en passant case
		Boolean isEnPassant = false;
		if(positions[selectedPosition[0]][selectedPosition[1]].name == "p" && positions[selectedPosition[0]][selectedPosition[1]].y != currY && positions[currX][currY].name == "none") {
			isEnPassant = true;
			if(turn == 0) {
				moves[currMove] = new Move(new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]), new PiecePosition(positions[currX][currY]), new PiecePosition(positions[currX+1][currY]));
			} else {
				moves[currMove] = new Move(new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]), new PiecePosition(positions[currX][currY]), new PiecePosition(positions[currX-1][currY]));
			}
			moves[currMove].prevPosition.name = "p";
			
			positions[currX][currY] = new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]);
			positions[currX][currY].x = currX;
			positions[currX][currY].y = currY;
			
			
			if(positions[currX][currY].color == 0) {
				positions[currX+1][currY] = new PiecePosition("none", -1, currX+1, currY);
			} else {
				positions[currX-1][currY] = new PiecePosition("none", -1, currX-1, currY);
			}
			positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, selectedPosition[0], selectedPosition[1]);
			
		} else if(!castled){
			moves[currMove] = new Move(new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]) , new PiecePosition(positions[currX][currY]), new PiecePosition(positions[currX][currY]));
			positions[currX][currY] = new PiecePosition(positions[selectedPosition[0]][selectedPosition[1]]);
			positions[currX][currY].x = currX;
			positions[currX][currY].y = currY;
			positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, selectedPosition[0], selectedPosition[1]);
		}
		
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
		
		currMove++;
		
		
		positions[currX][currY].isSelected = false;
		isPieceSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
		selectedPosition[0] = -1;
		selectedPosition[1] = -1;
		this.toggleTurn();
	}
	
	public void checkCanChangePosition(int currX, int currY, int[][] futureMoves, PiecePosition[][] positions) {
		
//		System.out.printf("currX: %d, currY: %d, s[0]: %d, s[1]: %d\n", currX, currY, selectedPosition[0], selectedPosition[1]);
		positions[currX][currY] = positions[selectedPosition[0]][selectedPosition[1]];
		positions[currX][currY].x = currX;
		positions[currX][currY].y = currY;
		positions[selectedPosition[0]][selectedPosition[1]] = new PiecePosition("none", -1, currX, currY);
		
		positions[currX][currY].isSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
	}
	
	private void checkCanChangePositionWithoutCheck(int currX, int currY, int[][] futureMoves, PiecePosition[][] positions, int[] possibleSelectedPosition) {
		
		positions[currX][currY] = positions[possibleSelectedPosition[0]][possibleSelectedPosition[1]];
		positions[currX][currY].x = currX;
		positions[currX][currY].y = currY;
		positions[possibleSelectedPosition[0]][possibleSelectedPosition[1]] = new PiecePosition("none", -1, currX, currY);
		
		positions[currX][currY].isSelected = false;
		for(int i1 = 0; i1<futureMoves.length; i1++) {
			positions[futureMoves[i1][0]][futureMoves[i1][1]].isFutureMove = false;
		}
	}
	
	public PiecePosition[][] getCopy(PiecePosition[][] positions) {
		PiecePosition[][] copy = new PiecePosition[8][8];
		
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				copy[i][j] = new PiecePosition(positions[i][j].name, positions[i][j].color, positions[i][j].x, positions[i][j].y, positions[i][j].isSelected, positions[i][j].isFutureMove);
			}
		}
		return copy;
	}
	
	public Boolean isGettingStalemate(int color, PiecePosition[][] positions) {
		
		int oppColor = color == 0 ? 1 : 0;
		ArrayList<int[][]> possibleAttacks = new ArrayList<>();
		int[][] fromPositions = new int[200][2];
		int k = 0;
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				PiecePosition p = positions[i][j];
				if(p.color != oppColor) {
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
						possibleAttacks.add(playKing(p, positions, false));
						break;
					case "p":
						possibleAttacks.add(playPawn(p, positions));
						break;
					default:
						break;
					}
					if(p.name != "none" && p.color != oppColor) {
						fromPositions[k][0] = i;
						fromPositions[k][1] = j;
						k++;
					}
					
				}
			}
		}
		
		k = 0;
		for(int[][] attacks : possibleAttacks) {
			for(int[] attack : attacks) {
				int i = attack[0], j = attack[1];
				PiecePosition[][] pc2 = getCopy(positions);
				this.checkCanChangePositionWithoutCheck(i, j, attacks, pc2, fromPositions[k]);
				
				if(!isGettingChecked(this.turn, pc2)) {
					return false;
				}
			}
			k++;
		}
		return true;
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
				moves = this.playKing(p, positions, true);
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
	
	
	
	
	public void testGame(int depth) {
		helper.setHelper(this, depth);
		helper.run();
	}
	
	
	public Boolean playBySelf(PiecePosition selectedPiece, int depth) {
		
		selectedPosition[0] = selectedPiece.x;
		selectedPosition[1] = selectedPiece.y;
		if(depth == 0) return false;
		
		int[][] futureMoves = this.play(this.turn, positions);
		
//		System.out.printf("selectedPosition[0]: %d, selectedPosition[1]: %d\n", selectedPosition[0], selectedPosition[1]);
//		System.out.printf("Length: %d\n", futureMoves.length);
		for(int[] futureMove : futureMoves) {
			
			int i = futureMove[0], j = futureMove[1];
			PiecePosition[][] pc2 = getCopy(positions);
//			System.out.printf("i: %d, j: %d\n", i, j);
			checkCanChangePosition(i, j, futureMoves, pc2);
			
			if(!isGettingChecked(this.turn, pc2)) {
				
				changePosition(i, j, futureMoves, positions);
				
				int oppTurn = this.turn == 1 ? 0 : 1;
				if(isGettingChecked(this.turn, positions)) {
					kingChecked[this.turn] = true;

					if(isGettingCheckmated(this.turn, positions)) {
						checkmate[this.turn] = true;
					}
					
				} else if(isGettingStalemate(this.turn, positions)) {
					stalemate = true;
				} else {
					kingChecked[this.turn] = false;
				}
				
				if(isGettingChecked(oppTurn, positions)) {
					kingChecked[oppTurn] = true;
				} else {
					kingChecked[oppTurn] = false;
				}
				
				try {
					if(isBoardVisible) this.setBoard();
					Helper.boardRun(depth-1, this);
					undoMove();
					toggleTurn();
					selectedPosition[0] = selectedPiece.x;
					selectedPosition[1] = selectedPiece.y;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
		return false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(isToBePromoted) return;
		Piece piece = (Piece) e.getComponent();
		int i = piece.x, j = piece.y;
		
		Boolean wasFutureMove = false;
		if((selectedPosition[0] != i || selectedPosition[1] != j) && selectedPosition[0] != -1) {
			int[][] futureMoves = this.play(this.turn, positions);
			if(this.checkInFutureMoves(futureMoves, i, j, positions)) {
				PiecePosition[][] pc2 = getCopy(positions);
				checkCanChangePosition(i, j, futureMoves, pc2);
				if(isGettingChecked(this.turn, pc2)) {
					System.out.println("Invalid move.");
					return;
				}

				wasFutureMove = true;			
				changePosition(i, j, futureMoves, positions);
				
				int oppTurn = this.turn == 1 ? 0 : 1;
				if(isGettingChecked(this.turn, positions)) {
					kingChecked[this.turn] = true;

					if(isGettingCheckmated(this.turn, positions)) {
						checkmate[this.turn] = true;
					}
					
				} else if(isGettingStalemate(this.turn, positions)) {
					stalemate = true;
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
					if(isBoardVisible) this.setBoard();
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
			if(isBoardVisible) this.setBoard();
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
