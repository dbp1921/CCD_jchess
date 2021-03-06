package jchess.core.pieces;

/**
 * Class to represent a chess pawn king. King is the most important piece for
 * the game. Loose of king is the and of game. When king is in danger by the
 * opponent then it's a Checked, and when have no other escape then stay on a
 * square "in danger" by the opponent then it's a CheckedMate, and the game is
 * over.
 */
import jchess.core.board.Chessboard;
import jchess.core.board.ChessboardField;
import jchess.core.util.Player;

public class King extends Piece {

	public static final String	SYMBOL		= "King";	//$NON-NLS-1$
	public boolean							wasMotion	= false;	// maybe
																									// change
																									// to:
																									// 'wasMotioned'

	public King(Chessboard chessboard, Player player, ChessboardField field) throws Exception {
		super(chessboard, player, SYMBOL, field);
	}

	/**
	 * Method to check is the king is checked
	 * 
	 * @return bool true if king is not save, else returns false
	 */
	public boolean isChecked() {
		return isSafe(getField()) == false;
	}

	/**
	 * Method to check is the king is checked or stalemated
	 * 
	 * @return int 0 if nothing, 1 if checkmate, else returns 2
	 * @throws Exception 
	 */
	public int isCheckmatedOrStalemated() throws Exception {
		// TODO
		// if (this.allMoves().size() == 0) {
		// for (int i = 0; i < 8; ++i) {
		// for (int j = 0; j < 8; ++j) {
		// if (getChessboard().getFields()[i][j].getPiece() != null &&
		// getChessboard().getFields()[i][j].getPiece().getPlayer() ==
		// this.getPlayer()
		// && getChessboard().getFields()[i][j].getPiece().allMoves().size() != 0) {
		// return 0;
		// }
		// }
		// }
		//
		// if (this.isChecked()) {
		// return 1;
		// } else {
		// return 2;
		// }
		// } else {
		// return 0;
		// }
		return 0;
	}

	/**
	 * Method to check will the king be safe when move
	 * 
	 * @return bool true if king is save, else returns false
	 */
	public boolean isSafe(ChessboardField cbf) // A bit confusing code.
	{
		// TODO
		// Square field = (Square) cbf;
		// // Rook & Queen
		// for (int i = field.pozY + 1; i <= 7; ++i) // up
		// {
		// if (this.getChessboard().getFields()[field.pozX][i].getPiece() == null ||
		// this.getChessboard().getFields()[field.pozX][i].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if
		// (this.getChessboard().getFields()[field.pozX][i].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if
		// (this.getChessboard().getFields()[field.pozX][i].getPiece().getSymbol()
		// == Rook.SYMBOL
		// || this.getChessboard().getFields()[field.pozX][i].getPiece().getSymbol()
		// == Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// for (int i = field.pozY - 1; i >= 0; --i) // down
		// {
		// if (this.getChessboard().getFields()[field.pozX][i].getPiece() == null ||
		// this.getChessboard().getFields()[field.pozX][i].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if
		// (this.getChessboard().getFields()[field.pozX][i].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if
		// (this.getChessboard().getFields()[field.pozX][i].getPiece().getSymbol()
		// == Rook.SYMBOL
		// || this.getChessboard().getFields()[field.pozX][i].getPiece().getSymbol()
		// == Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// for (int i = field.pozX - 1; i >= 0; --i) // left
		// {
		// if (this.getChessboard().getFields()[i][field.pozY].getPiece() == null ||
		// this.getChessboard().getFields()[i][field.pozY].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if
		// (this.getChessboard().getFields()[i][field.pozY].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if
		// (this.getChessboard().getFields()[i][field.pozY].getPiece().getSymbol()
		// == Rook.SYMBOL
		// || this.getChessboard().getFields()[i][field.pozY].getPiece().getSymbol()
		// == Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// for (int i = field.pozX + 1; i <= 7; ++i) // right
		// {
		// if (this.getChessboard().getFields()[i][field.pozY].getPiece() == null ||
		// this.getChessboard().getFields()[i][field.pozY].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if
		// (this.getChessboard().getFields()[i][field.pozY].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if
		// (this.getChessboard().getFields()[i][field.pozY].getPiece().getSymbol()
		// == Rook.SYMBOL
		// || this.getChessboard().getFields()[i][field.pozY].getPiece().getSymbol()
		// == Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// // Bishop & Queen
		// for (int h = field.pozX - 1, i = field.pozY + 1; Chessboard.isout(h, i)
		// == false; --h, ++i) // left-up
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece() == null ||
		// this.getChessboard().getFields()[h][i].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if (this.getChessboard().getFields()[h][i].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Bishop.SYMBOL
		// || this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// for (int h = field.pozX - 1, i = field.pozY - 1; Chessboard.isout(h, i)
		// == false; --h, --i) // left-down
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece() == null ||
		// this.getChessboard().getFields()[h][i].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if (this.getChessboard().getFields()[h][i].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Bishop.SYMBOL
		// || this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// for (int h = field.pozX + 1, i = field.pozY + 1; Chessboard.isout(h, i)
		// == false; ++h, ++i) // right-up
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece() == null ||
		// this.getChessboard().getFields()[h][i].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if (this.getChessboard().getFields()[h][i].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Bishop.SYMBOL
		// || this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// for (int h = field.pozX + 1, i = field.pozY - 1; Chessboard.isout(h, i)
		// == false; ++h, --i) // right-down
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece() == null ||
		// this.getChessboard().getFields()[h][i].getPiece() == this) // if
		// // on
		// // this
		// // sqhuare
		// // isn't
		// // piece
		// {
		// continue;
		// } else if (this.getChessboard().getFields()[h][i].getPiece().getPlayer()
		// != this.getPlayer()) // if
		// // isn't
		// // our
		// // piece
		// {
		// if (this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Bishop.SYMBOL
		// || this.getChessboard().getFields()[h][i].getPiece().getSymbol() ==
		// Queen.SYMBOL) {
		// return false;
		// } else {
		// break;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// // Knight
		// int newX, newY;
		//
		// // 1
		// newX = field.pozX - 2;
		// newY = field.pozY + 1;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 2
		// newX = field.pozX - 1;
		// newY = field.pozY + 2;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 3
		// newX = field.pozX + 1;
		// newY = field.pozY + 2;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 4
		// newX = field.pozX + 2;
		// newY = field.pozY + 1;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 5
		// newX = field.pozX + 2;
		// newY = field.pozY - 1;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 6
		// newX = field.pozX + 1;
		// newY = field.pozY - 2;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 7
		// newX = field.pozX - 1;
		// newY = field.pozY - 2;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // 8
		// newX = field.pozX - 2;
		// newY = field.pozY - 1;
		//
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Knight.SYMBOL) {
		// return false;
		// }
		// }
		//
		// // King
		// King otherKing;
		// if (this == getChessboard().getKingForColor(Player.colors.white)) {
		// otherKing = getChessboard().getKingForColor(Player.colors.black);
		// } else {
		// otherKing = getChessboard().getKingForColor(Player.colors.white);
		// }
		// Square sq = (Square) otherKing.getField();
		// if (field.pozX <= sq.pozX + 1 && field.pozX >= sq.pozX - 1 && field.pozY
		// <= sq.pozY + 1 && field.pozY >= sq.pozY - 1) {
		// return false;
		// }
		//
		// // Pawn
		// if (this.getPlayer().isGoDown()) // check if getPlayer() "go" down or up
		// {
		// newX = field.pozX - 1;
		// newY = field.pozY + 1;
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Pawn.SYMBOL) {
		// return false;
		// }
		// }
		// newX = field.pozX + 1;
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Pawn.SYMBOL) {
		// return false;
		// }
		// }
		// } else {
		// newX = field.pozX - 1;
		// newY = field.pozY - 1;
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Pawn.SYMBOL) {
		// return false;
		// }
		// }
		// newX = field.pozX + 1;
		// if (Chessboard.isout(newX, newY) == false) {
		// if (this.getChessboard().getFields()[newX][newY].getPiece() == null) //
		// if
		// // on
		// // this
		// // sqhuare isn't
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getPlayer() ==
		// this.getPlayer()) // if
		// // is
		// // our
		// // piece
		// {
		// } else if
		// (this.getChessboard().getFields()[newX][newY].getPiece().getSymbol() ==
		// Pawn.SYMBOL) {
		// return false;
		// }
		// }
		// }

		return true;
	}

	/**
	 * Method to check will the king be safe when move
	 * 
	 * @return bool true if king is save, else returns false
	 * @throws Exception 
	 */
	public boolean willBeSafeWhenMoveOtherPiece(ChessboardField sqIsHere, ChessboardField sqWillBeThere) throws Exception // long
	// name
	// ;)
	{
		Piece tmp = sqWillBeThere.getPiece();
		sqWillBeThere.setPiece(sqIsHere.getPiece()); // move without redraw
		sqIsHere.setPiece(null);

		boolean ret = isSafe(this.getField());

		sqIsHere.setPiece(sqWillBeThere.getPiece());
		sqWillBeThere.setPiece(tmp);

		return ret;
	}

	@Override
	public IMoveBehavior createMoveBehavior() {
		return new KingMoveBehavior(getPlayer(), getChessboard(), getField(), this);
	}
}