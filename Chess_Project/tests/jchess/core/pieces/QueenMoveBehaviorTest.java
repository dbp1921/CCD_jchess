package jchess.core.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.junit.Test;

import jchess.core.board.Chessboard;
import jchess.core.board.ChessboardField;
import jchess.core.util.Player;
import jchess.core.util.Player.PlayerColor;
import junit.framework.Assert;

public class QueenMoveBehaviorTest {

	@Test
	public void testAllMoves() throws Exception {

		// Queen in the center - F3
		Player playerl = new Player("player1", PlayerColor.BLACK);
		Player player2 = new Player("player2", PlayerColor.WHITE);
		Chessboard board = new Chessboard(null, null);

		// adding the obstacles
		// friend pawns on the way
		Pawn p1 = new Pawn(board, playerl, null);
		board.getField("G5").setPiece(p1);
		Pawn p6 = new Pawn(board, playerl, null);
		board.getField("F5").setPiece(p6);
		Pawn p7 = new Pawn(board, playerl, null);
		board.getField("I6").setPiece(p7);
		// friend pawn blocks only 1 of 2 straight fields on the way
		Pawn p2 = new Pawn(board, playerl, null);
		board.getField("E5").setPiece(p2);
		// two foe pawns on the way block both straight fields on the way
		Pawn p3 = new Pawn(board, player2, null);
		board.getField("C1").setPiece(p3);
		Pawn p4 = new Pawn(board, player2, null);
		board.getField("C2").setPiece(p4);
		// foe pawns on the way
		Pawn p5 = new Pawn(board, player2, null);
		board.getField("B7").setPiece(p5);
		Pawn p8 = new Pawn(board, player2, null);
		board.getField("B3").setPiece(p8);

		// calculating allowed moves
		QueenMoveBehavior qmb = new QueenMoveBehavior(playerl, board, board.getField("F3"));

		// creating array list of expected fields
		String[] expectedIdentifiers = new String[] { "F2", "F1", "G3", "H3", "F4", "E3", "D3", "C3", "B3", "E2", "D1", "G2", "H4", "J5", "E4", "D5", "C6", "B7",
				"D2", "E1" };
		ArrayList<ChessboardField> expectedFields = new ArrayList<ChessboardField>();
		for (String expID : expectedIdentifiers) {
			expectedFields.add(board.getField(expID));
		}

		// sorting of both array lists
		Collections.sort(expectedFields, new Comparator<ChessboardField>() {
			public int compare(ChessboardField field1, ChessboardField field2) {
				return field1.getIdentifier().compareTo(field2.getIdentifier());
			}
		});
		Collections.sort(qmb.allMoves(), new Comparator<ChessboardField>() {
			public int compare(ChessboardField field1, ChessboardField field2) {
				return field1.getIdentifier().compareTo(field2.getIdentifier());
			}
		});

		// comparison
		Assert.assertEquals(expectedFields, qmb.allMoves());
		;
	}

}
