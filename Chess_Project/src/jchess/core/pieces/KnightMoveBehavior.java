package jchess.core.pieces;

import java.util.ArrayList;

import jchess.core.board.Chessboard;
import jchess.core.board.ChessboardField;
import jchess.core.util.Player;

/**
 * Class for creating a behavior of Knight.
 * Calculates allowed fields for movement of this piece.
 * 
 * @author Serhii
 */
public class KnightMoveBehavior extends MoveBehavior {

	public KnightMoveBehavior(Player player, Chessboard m_Chessboard, ChessboardField field) {
		super(player, m_Chessboard, field);
	}

	/**
	 * This method calculates allowed fields for movement of the classic chess Knight
	 * 
	 * @author Serhii
	 * 
	 * @param allMoves array list of allowed fields
	 * @throws Exception 
	 */
	@Override
	public ArrayList<ChessboardField> allMoves() throws Exception {
		ArrayList<ChessboardField> allMoves = new ArrayList<ChessboardField>();

		// adding all jump fields with straight offset = 1 and diagonal offset = 1
		for (ChessboardField StraightPlusDiagonalField : this.m_Chessboard.getJumpStraightPlusDiagonalFields(this.m_Field, this.m_Player.getColor(), 1, 1)) {

			if (this.m_Chessboard.getKingForColor(this.m_Player.getColor()).willBeSafeWhenMoveOtherPiece(this.m_Field, StraightPlusDiagonalField)) {
				allMoves.add(StraightPlusDiagonalField);
			} else {
				continue;
			}
		}

		return allMoves;
	}
}