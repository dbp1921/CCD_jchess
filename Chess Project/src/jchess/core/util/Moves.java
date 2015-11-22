package jchess.core.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import jchess.core.board.Square;
import jchess.core.pieces.Pawn;
import jchess.core.pieces.Piece;
import jchess.ui.ChessboardUI;
import jchess.ui.GameTab;
import jchess.ui.lang.Language;

/**
 * Class representing the players moves, it's also checking that the moves taken
 * by player are correct. All moves which was taken by current player are saving
 * as List of Strings The history of moves is printing in a table
 * 
 * @param gameTab
 *          The current game
 */
public class Moves extends AbstractTableModel {

	private static final long		serialVersionUID	= -5573231695823099101L;

	private ArrayList<String>		move							= new ArrayList<String>();
	private int									columnsNum				= 3;
	private int									rowsNum						= 0;
	private String[]						names							= new String[] { Language.getString("white"), Language.getString("black") };	//$NON-NLS-1$ //$NON-NLS-2$
	private MyDefaultTableModel	tableModel				= null;
	private JScrollPane					scrollPane				= null;
	private JTable							table							= null;
	private boolean							enterBlack				= false;
	private GameTab							gameTab						= null;
	protected Stack<Move>				moveBackStack			= new Stack<Move>();
	protected Stack<Move>				moveForwardStack	= new Stack<Move>();

	public enum castling {
		none, shortCastling, longCastling
	}

	public Moves(GameTab gameTab) {
		super();
		this.tableModel = new MyDefaultTableModel();
		this.table = new JTable(this.tableModel);
		this.scrollPane = new JScrollPane(this.table);
		this.scrollPane.setMaximumSize(new Dimension(100, 100));
		this.table.setMinimumSize(new Dimension(100, 100));
		this.gameTab = gameTab;

		this.tableModel.addColumn(this.names[0]);
		this.tableModel.addColumn(this.names[1]);
		this.addTableModelListener(null);
		this.tableModel.addTableModelListener(null);
		this.scrollPane.setAutoscrolls(true);
	}

	public void draw() {
	}

	@Override
	public String getValueAt(int x, int y) {
		return this.move.get((y * 2) - 1 + (x - 1));
	}

	@Override
	public int getRowCount() {
		return this.rowsNum;
	}

	@Override
	public int getColumnCount() {
		return this.columnsNum;
	}

	protected void addRow() {
		this.tableModel.addRow(new String[2]);
	}

	protected void addCastling(String move) {
		this.move.remove(this.move.size() - 1);// remove last element (move of Rook)
		if (!this.enterBlack) {
			this.tableModel.setValueAt(move, this.tableModel.getRowCount() - 1, 1);// replace
																																							// last
																																							// value
		} else {
			this.tableModel.setValueAt(move, this.tableModel.getRowCount() - 1, 0);// replace
																																							// last
																																							// value
		}
		this.move.add(move);// add new move (O-O or O-O-O)
	}

	@Override
	public boolean isCellEditable(int a, int b) {
		return false;
	}

	/**
	 * Method of adding new moves to the table
	 * 
	 * @param str
	 *          String which in is saved player move
	 */
	protected void addMove2Table(String str) {
		try {
			if (!this.enterBlack) {
				this.addRow();
				this.rowsNum = this.tableModel.getRowCount() - 1;
				this.tableModel.setValueAt(str, rowsNum, 0);
			} else {
				this.tableModel.setValueAt(str, rowsNum, 1);
				this.rowsNum = this.tableModel.getRowCount() - 1;
			}
			this.enterBlack = !this.enterBlack;
			this.table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));// scroll
																																													// to
																																													// down

		} catch (java.lang.ArrayIndexOutOfBoundsException exc) {
			if (this.rowsNum > 0) {
				this.rowsNum--;
				addMove2Table(str);
			}
		}
	}

	/**
	 * Method of adding new move
	 * 
	 * @param move
	 *          String which in is capt player move
	 */
	public void addMove(String move) {
		if (isMoveCorrect(move)) {
			this.move.add(move);
			this.addMove2Table(move);
			this.moveForwardStack.clear();
		}

	}

	public void addMove(Square begin, Square end, boolean registerInHistory, castling castlingMove, boolean wasEnPassant, Piece promotedPiece) {
		String locMove = new String(begin.piece.getSymbolForMoveHistory());

		locMove += Character.toString((char) (begin.pozX + 97));// add letter of
																														// Square from
																														// which move was
																														// made
		locMove += Integer.toString(8 - begin.pozY);// add number of Square from
																								// which move was made

		if (end.piece != null) {
			locMove += "x";// take down opponent piece //$NON-NLS-1$
		} else {
			locMove += "-";// normal move //$NON-NLS-1$
		}

		locMove += Character.toString((char) (end.pozX + 97));// add letter of
																													// Square to which
																													// move was made
		locMove += Integer.toString(8 - end.pozY);// add number of Square to which
																							// move was made

		if (begin.piece.getSymbol() == Pawn.SYMBOL && begin.pozX - end.pozX != 0 && end.piece == null) {
			locMove += "(e.p)";// pawn take down opponent en passant //$NON-NLS-1$
			wasEnPassant = true;
		}
		if (((this.enterBlack == false) && this.gameTab.getChessboard().getBlackKing().isChecked())
				|| (this.enterBlack && this.gameTab.getChessboard().getWhiteKing().isChecked())) {// if
			// checked

			if ((!this.enterBlack && this.gameTab.getChessboard().getBlackKing().isCheckmatedOrStalemated() == 1)
					|| (this.enterBlack && this.gameTab.getChessboard().getWhiteKing().isCheckmatedOrStalemated() == 1)) {// check
				// if
				// checkmated
				locMove += "#";// check mate //$NON-NLS-1$
			} else {
				locMove += "+";// check //$NON-NLS-1$
			}
		}
		if (castlingMove == castling.shortCastling) {
			this.addCastling("0-0"); //$NON-NLS-1$
		} else if (castlingMove == castling.longCastling) {
			this.addCastling("0-0-0"); //$NON-NLS-1$
		} else {
			this.move.add(locMove);
			this.addMove2Table(locMove);
		}
		this.scrollPane.scrollRectToVisible(new Rectangle(0, this.scrollPane.getHeight() - 2, 1, 1));

		if (registerInHistory) {
			this.moveBackStack.add(new Move(new Square(begin), new Square(end), begin.piece, end.piece, castlingMove, wasEnPassant, promotedPiece));
		}
	}

	public void clearMoveForwardStack() {
		this.moveForwardStack.clear();
	}

	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public ArrayList<String> getMoves() {
		return this.move;
	}

	public synchronized Move getLastMoveFromHistory() {
		try {
			Move last = this.moveBackStack.get(this.moveBackStack.size() - 1);
			return last;
		} catch (java.lang.ArrayIndexOutOfBoundsException exc) {
			return null;
		}
	}

	public synchronized Move getNextMoveFromHistory() {
		try {
			Move next = this.moveForwardStack.get(this.moveForwardStack.size() - 1);
			return next;
		} catch (java.lang.ArrayIndexOutOfBoundsException exc) {
			return null;
		}

	}

	public synchronized Move undo() {
		try {
			Move last = this.moveBackStack.pop();
			if (last != null) {
				this.moveForwardStack.push(last);

				if (this.enterBlack) {
					this.tableModel.setValueAt("", this.tableModel.getRowCount() - 1, 0); //$NON-NLS-1$
					this.tableModel.removeRow(this.tableModel.getRowCount() - 1);

					if (this.rowsNum > 0) {
						this.rowsNum--;
					}
				} else {
					if (this.tableModel.getRowCount() > 0) {
						this.tableModel.setValueAt("", this.tableModel.getRowCount() - 1, 1); //$NON-NLS-1$
					}
				}
				this.move.remove(this.move.size() - 1);
				this.enterBlack = !this.enterBlack;
			}
			return last;
		} catch (java.util.EmptyStackException exc) {
			this.enterBlack = false;
			return null;
		} catch (java.lang.ArrayIndexOutOfBoundsException exc) {
			return null;
		}
	}

	public synchronized Move redo() {
		Move result = null;
		try {
			Move first = this.moveForwardStack.pop();
			this.moveBackStack.push(first);
			result = first;
		} catch (java.util.EmptyStackException exc) {
			Logging.log(exc);
			result = null;
		}
		return result;
	}

	/**
	 * Method with is checking is the move is correct
	 * 
	 * @param move
	 *          String which in is capt player move
	 * @return boolean 1 if the move is correct, else 0
	 */
	static public boolean isMoveCorrect(String move) {// TODO not correct, Symbols
																										// where changed
		if (move.equals("O-O") || move.equals("O-O-O")) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		}
		try {
			int from = 0;
			int sign = move.charAt(from);// get First
			switch (sign) // if sign of piece, get next
			{
				case 66: // B like Bishop
				case 75: // K like King
				case 78: // N like Knight
				case 81: // Q like Queen
				case 82:
					from = 1;
					break; // R like Rook
			}
			sign = move.charAt(from);
			Logging.log(sign);
			if (sign < 97 || sign > 104) // if lower than 'a' or higher than 'h'
			{
				return false;
			}
			sign = move.charAt(from + 1);
			if (sign < 49 || sign > 56) // if lower than '1' or higher than '8'
			{
				return false;
			}
			if (move.length() > 3) // if is equal to 3 or lower, than it's in short
															// notation, no more checking needed
			{
				sign = move.charAt(from + 2);
				if (sign != 45 && sign != 120) // if isn't '-' and 'x'
				{
					return false;
				}
				sign = move.charAt(from + 3);
				if (sign < 97 || sign > 104) // if lower than 'a' or higher than 'h'
				{
					return false;
				}
				sign = move.charAt(from + 4);
				if (sign < 49 || sign > 56) // if lower than '1' or higher than '8'
				{
					return false;
				}
			}
		} catch (java.lang.StringIndexOutOfBoundsException exc) {
			return false;
		}

		return true;
	}

	public void addMoves(ArrayList<String> list) {
		for (String singleMove : list) {
			if (isMoveCorrect(singleMove)) {
				this.addMove(singleMove);
			}
		}
	}

	/**
	 * Method of getting the moves in string
	 * 
	 * @return str String which in is capt player move
	 */
	public String getMovesInString() {
		int n = 1;
		int i = 0;
		String str = new String();
		for (String locMove : this.getMoves()) {
			if (i % 2 == 0) {
				str += n + ". "; //$NON-NLS-1$
				n += 1;
			}
			str += locMove + " "; //$NON-NLS-1$
			i += 1;
		}
		return str;
	}

	/**
	 * Method to set all moves from String with validation test (usefoul for
	 * network game)
	 * 
	 * @param moves
	 *          String to set in String like PGN with full-notation format
	 * @throws Exception
	 */
	public void setMoves(String moves) throws Exception {
		int from = 0;
		int to = 0;
		int n = 1;
		ArrayList<String> tempArray = new ArrayList<String>();
		int tempStrSize = moves.length() - 1;
		while (true) {
			from = moves.indexOf(" ", from); //$NON-NLS-1$
			to = moves.indexOf(" ", from + 1); //$NON-NLS-1$
			// Logging.log(from+">"+to);
			try {
				tempArray.add(moves.substring(from + 1, to).trim());
			} catch (java.lang.StringIndexOutOfBoundsException exc) {
				Logging.log(Language.getString("Moves.0"), exc); //$NON-NLS-1$
				break;
			}
			if (n % 2 == 0) {
				from = moves.indexOf(".", to); //$NON-NLS-1$
				if (from < to) {
					break;
				}
			} else {
				from = to;
			}
			n += 1;
			if (from > tempStrSize || to > tempStrSize) {
				break;
			}
		}
		for (String locMove : tempArray) // test if moves are written correctly
		{
			if (!Moves.isMoveCorrect(locMove.trim())) // if not
			{
				JOptionPane.showMessageDialog(this.gameTab, Language.getString("invalid_file_to_load") + move); //$NON-NLS-1$
				return;// show message and finish reading game
			}
		}
		boolean canMove = false;
		for (String locMove : tempArray) {
			if (locMove.equals("O-O-O") || locMove.equals("O-O")) // if castling //$NON-NLS-1$ //$NON-NLS-2$
			{
				int[] values = new int[4];
				if (locMove.equals("O-O-O")) { //$NON-NLS-1$
					if (this.gameTab.getActivePlayer().getColor() == Player.colors.black) // if
					// black
					// turn
					{
						values = new int[] { 4, 0, 2, 0 };// move value for castling (King
																							// move)
					} else {
						values = new int[] { 4, 7, 2, 7 };// move value for castling (King
																							// move)
					}
				} else if (locMove.equals("O-O")) // if short castling //$NON-NLS-1$
				{
					if (this.gameTab.getActivePlayer().getColor() == Player.colors.black) // if
					// black
					// turn
					{
						values = new int[] { 4, 0, 6, 0 };// move value for castling (King
																							// move)
					} else {
						values = new int[] { 4, 7, 6, 7 };// move value for castling (King
																							// move)
					}
				}
				canMove = this.gameTab.getChessboard().tryMove(values[0], values[1], values[2], values[3]);

				if (canMove == false) // if move is illegal
				{
					JOptionPane.showMessageDialog(this.gameTab, Language.getString("illegal_move_on") + locMove); //$NON-NLS-1$
					return;// finish reading game and show message
				}
				continue;
			}
			from = 0;
			int num = locMove.charAt(from);
			if (num <= 90 && num >= 65) {
				from = 1;
			}
			int xFrom = 9; // set to higher value than chessboard has fields, to cause
											// error if piece won't be found
			int yFrom = 9;
			int xTo = 9;
			int yTo = 9;
			boolean pieceFound = false;
			if (locMove.length() <= 3) {
				Square[][] squares = this.gameTab.getChessboard().squares;
				xTo = locMove.charAt(from) - 97;// from ASCII
				yTo = ChessboardUI.bottom - (locMove.charAt(from + 1) - 49);// from
																																		// ASCII
				for (int i = 0; i < squares.length && !pieceFound; i++) {
					for (int j = 0; j < squares[i].length && !pieceFound; j++) {
						if (squares[i][j].piece == null || this.gameTab.getActivePlayer().getColor() != squares[i][j].piece.player.getColor()) {
							continue;
						}
						ArrayList<Square> pieceMoves = squares[i][j].piece.allMoves();
						for (Object square : pieceMoves) {
							Square currSquare = (Square) square;
							if (currSquare.pozX == xTo && currSquare.pozY == yTo) {
								xFrom = squares[i][j].piece.getSquare().pozX;
								yFrom = squares[i][j].piece.getSquare().pozY;
								pieceFound = true;
							}
						}
					}
				}
			} else {
				xFrom = locMove.charAt(from) - 97;// from ASCII
				yFrom = ChessboardUI.bottom - (locMove.charAt(from + 1) - 49);// from
				// ASCII
				xTo = locMove.charAt(from + 3) - 97;// from ASCII
				yTo = ChessboardUI.bottom - (locMove.charAt(from + 4) - 49);// from
																																		// ASCII
			}
			canMove = this.gameTab.getChessboard().tryMove(xFrom, yFrom, xTo, yTo);
			if (canMove == false) // if move is illegal
			{
				JOptionPane.showMessageDialog(this.gameTab, Language.getString("illegal_move_on") + locMove); //$NON-NLS-1$
				this.gameTab.getChessboard().activeSquare = null;
				return;// finish reading game and show message
			}
		}
	}
}

class MyDefaultTableModel extends DefaultTableModel {

	private static final long	serialVersionUID	= 7349152369577914941L;

	MyDefaultTableModel() {
		super();
	}

	@Override
	public boolean isCellEditable(int a, int b) {
		return false;
	}
}