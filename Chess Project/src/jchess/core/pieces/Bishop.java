package jchess.core.pieces;

import jchess.core.board.Chessboard;
import jchess.core.util.Player;

/**
 * Class to represent a chess pawn bishop
 */
public class Bishop extends Piece {

	public static final String	SYMBOL	= "Bishop"; //$NON-NLS-1$

	public Bishop(Chessboard chessboard, Player player) throws Exception {
		super(chessboard, player, SYMBOL);
	}

	@Override
	public IMoveBehavior createMoveBehavior() {
		return new BishopMoveBehavior(player, chessboard, square);
	}
}