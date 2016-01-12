package jchess.core.board;

import java.util.Map;

import jchess.core.board.graph.DiagonalEdge;
import jchess.core.board.graph.DirectedGraphEdge.EdgeDirection;
import jchess.core.board.graph.StraightEdge;
import jchess.core.pieces.Bishop;
import jchess.core.pieces.King;
import jchess.core.pieces.Knight;
import jchess.core.pieces.Pawn;
import jchess.core.pieces.Piece;
import jchess.core.pieces.Queen;
import jchess.core.pieces.Rook;
import jchess.core.util.Constants;
import jchess.core.util.Player;
import jchess.core.util.Player.PlayerColor;

public class HexagonChessFieldGraphInitializer {

	public static void initialise(Chessboard chessboard, Map<PlayerColor, Player> player) throws Exception {

		Player white = player.get(PlayerColor.WHITE);
		Player black = player.get(PlayerColor.BLACK);
		Player red = player.get(PlayerColor.RED);

		initalizeEmptyBoard(chessboard);

		/*
		 * Add pieces for the players
		 */

		// white on top
		addPiece(chessboard, new Rook(chessboard, white, null), "A1");
		addPiece(chessboard, new Knight(chessboard, white, null), "A2");
		addPiece(chessboard, new Bishop(chessboard, white, null), "A3");
		addPiece(chessboard, new Queen(chessboard, white, null), "A4");
		addPiece(chessboard, new King(chessboard, white, null), "A5");
		addPiece(chessboard, new Bishop(chessboard, white, null), "A6");
		addPiece(chessboard, new Knight(chessboard, white, null), "A7");
		addPiece(chessboard, new Rook(chessboard, white, null), "A8");

		String[] pawnFields = new String[] { "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9" };
		for (String pawnField : pawnFields) {
			addPiece(chessboard, new Pawn(chessboard, white, null), pawnField);
		}

		// black to the left
		addPiece(chessboard, new Rook(chessboard, black, null), "F1");
		addPiece(chessboard, new Knight(chessboard, black, null), "G2");
		addPiece(chessboard, new Bishop(chessboard, black, null), "H3");
		addPiece(chessboard, new Queen(chessboard, black, null), "I4");
		addPiece(chessboard, new King(chessboard, black, null), "J5");
		addPiece(chessboard, new Bishop(chessboard, black, null), "K6");
		addPiece(chessboard, new Knight(chessboard, black, null), "L7");
		addPiece(chessboard, new Rook(chessboard, black, null), "M8");

		pawnFields = new String[] { "E1", "F2", "G3", "H4", "I5", "J6", "K7", "L8", "M9" };
		for (String pawnField : pawnFields) {
			addPiece(chessboard, new Pawn(chessboard, black, null), pawnField);
		}

		// red to the right
		addPiece(chessboard, new Rook(chessboard, red, null), "F13");
		addPiece(chessboard, new Knight(chessboard, red, null), "G13");
		addPiece(chessboard, new Bishop(chessboard, red, null), "H13");
		addPiece(chessboard, new Queen(chessboard, red, null), "I13");
		addPiece(chessboard, new King(chessboard, red, null), "J13");
		addPiece(chessboard, new Bishop(chessboard, red, null), "K13");
		addPiece(chessboard, new Knight(chessboard, red, null), "L13");
		addPiece(chessboard, new Rook(chessboard, red, null), "M13");

		pawnFields = new String[] { "E12", "F12", "G12", "H12", "I12", "J12", "K12", "L12", "M12" };
		for (String pawnField : pawnFields) {
			addPiece(chessboard, new Pawn(chessboard, red, null), pawnField);
		}
	}

	private static void initalizeEmptyBoard(Chessboard chessboard) {
		ChessboardField[][] fields = new ChessboardField[13][13];

		/*
		 * First row has 8 fields, second 9,... until number of fields = 13. Next
		 * field has 12 , 11, .. until end.
		 * 
		 * There are 13 rows.
		 */
		int numberofFields = 8;
		int start = 0;
		for (int row = 0; row < 13;) {
			for (int column = start; column < numberofFields + start; column++) {
				String identifier = getIdentifierLetter(row + 1) + (column + 1);
				ChessboardField field = new Hexagon(identifier, chessboard);
				chessboard.addNode(field);
				fields[row][column] = field;
			}
			row++;
			if (row < 6) {
				numberofFields++;
			} else {
				numberofFields--;
				start++;
			}
		}

		connectStraightEdges(fields);
		connectDiagonalEdges(fields);
	}

	private static void addPiece(Chessboard chessboard, Piece piece, String fieldIdentifier) throws Exception {
		ChessboardField field = (ChessboardField) chessboard.getNode(fieldIdentifier);
		piece.setField(field, chessboard);
		field.setPiece(piece);

		if (piece instanceof King) {
			chessboard.addKing((King) piece);
		} else if (piece instanceof Rook) {
			chessboard.addRook((Rook) piece);
		}
	}

	private static void connectDiagonalEdges(ChessboardField[][] fields) {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				ChessboardField field = fields[i][j];
				if (field != null) {
					// left below
					int newI = i + 1;
					int newJ = j - 1;
					if ((newI < 13) && (newJ > -1)) {
						ChessboardField field2 = fields[newI][newJ];
						if (field2 != null) {
							DiagonalEdge edge = new DiagonalEdge(field, field2, EdgeDirection.LEFT_DOWN);
							field.addEdge(edge);
							edge = new DiagonalEdge(field2, field, EdgeDirection.RIGHT_UP);
							field2.addEdge(edge);
						}
					}

					// down
					newI = i + 2;
					newJ = j + 1;
					if ((newI < 13) && (newJ < 13)) {
						ChessboardField field2 = fields[newI][newJ];
						if (field2 != null) {
							DiagonalEdge edge = new DiagonalEdge(field, field2, EdgeDirection.DOWN);
							field.addEdge(edge);
							edge = new DiagonalEdge(field2, field, EdgeDirection.UP);
							field2.addEdge(edge);
						}
					}

					// right below
					newI = i + 1;
					newJ = j + 2;
					if ((newI < 13) && (newJ < 13)) {
						ChessboardField field2 = fields[newI][newJ];
						if (field2 != null) {
							DiagonalEdge edge = new DiagonalEdge(field, field2, EdgeDirection.RIGHT_DOWN);
							field.addEdge(edge);
							edge = new DiagonalEdge(field2, field, EdgeDirection.LEFT_UP);
							field2.addEdge(edge);
						}
					}
				}
			}
		}
	}

	private static void connectStraightEdges(ChessboardField[][] fields) {
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				ChessboardField field = fields[i][j];
				if (field != null) {
					// left below
					int newI = i + 1;
					int newJ = j;
					if ((newI < 13) && (newJ < 13)) {
						ChessboardField field2 = fields[newI][newJ];
						if (field2 != null) {
							StraightEdge edge = new StraightEdge(field, field2, EdgeDirection.LEFT_DOWN);
							field.addEdge(edge);
							edge = new StraightEdge(field2, field, EdgeDirection.RIGHT_UP);
							field2.addEdge(edge);
						}
					}

					// left
					newI = i;
					newJ = j + 1;
					if ((newI < 13) && (newJ < 13)) {
						ChessboardField field2 = fields[newI][newJ];
						if (field2 != null) {
							StraightEdge edge = new StraightEdge(field, field2, EdgeDirection.RIGHT);
							field.addEdge(edge);
							edge = new StraightEdge(field2, field, EdgeDirection.LEFT);
							field2.addEdge(edge);
						}
					}

					// right below
					newI = i + 1;
					newJ = j + 1;
					if ((newI < 13) && (newJ < 13)) {
						ChessboardField field2 = fields[newI][newJ];
						if (field2 != null) {
							StraightEdge edge = new StraightEdge(field, field2, EdgeDirection.RIGHT_DOWN);
							field.addEdge(edge);
							edge = new StraightEdge(field2, field, EdgeDirection.LEFT_UP);
							field2.addEdge(edge);
						}
					}
				}
			}
		}
	}

	/**
	 * TODO move to util class
	 */
	public static String getIdentifierLetter(int pos) {
		String result = Constants.EMPTY_STRING;
		switch (pos) {
			case 1:
				result = "A";
				break;
			case 2:
				result = "B";
				break;
			case 3:
				result = "C";
				break;
			case 4:
				result = "D";
				break;
			case 5:
				result = "E";
				break;
			case 6:
				result = "F";
				break;
			case 7:
				result = "G";
				break;
			case 8:
				result = "H";
				break;
			case 9:
				result = "I";
				break;
			case 10:
				result = "J";
				break;
			case 11:
				result = "K";
				break;
			case 12:
				result = "L";
				break;
			case 13:
				result = "M";
				break;

			default:
				break;
		}

		return result;
	}

	/**
	 * TODO move to util class
	 */
	public static int[] getcoordinatesFromID(String id) {

		int[] coordinates = { 0, 0 };

		char letter = id.charAt(0);
		switch (letter) {
			case 'A':
				coordinates[1] = 1;
				break;
			case 'B':
				coordinates[1] = 2;
				break;
			case 'C':
				coordinates[1] = 3;
				break;
			case 'D':
				coordinates[1] = 4;
				break;
			case 'E':
				coordinates[1] = 5;
				break;
			case 'F':
				coordinates[1] = 6;
				break;
			case 'G':
				coordinates[1] = 7;
				break;
			case 'H':
				coordinates[1] = 8;
				break;
			case 'I':
				coordinates[1] = 9;
				break;
			case 'J':
				coordinates[1] = 10;
				break;
			case 'K':
				coordinates[1] = 11;
				break;
			case 'L':
				coordinates[1] = 12;
				break;
			case 'M':
				coordinates[1] = 13;
				break;
			default:
				break;
		}
		char number = id.charAt(1);
		switch (number) {
			case '1':
				coordinates[0] = 1;
				break;
			case '2':
				coordinates[0] = 2;
				break;
			case '3':
				coordinates[0] = 3;
				break;
			case '4':
				coordinates[0] = 4;
				break;
			case '5':
				coordinates[0] = 5;
				break;
			case '6':
				coordinates[0] = 6;
				break;
			case '7':
				coordinates[0] = 7;
				break;
			case '8':
				coordinates[0] = 8;
				break;
			case '9':
				coordinates[0] = 9;
				break;

			default:
				break;
		}
		if (id.length() > 2) {
			char number2 = id.charAt(2);
			switch (number2) {
				case '0':
					coordinates[0] = 10;
					break;
				case '1':
					coordinates[0] = 11;
					break;
				case '2':
					coordinates[0] = 12;
					break;
				case '3':
					coordinates[0] = 13;
					break;
				default:
					break;
			}
		}
		return coordinates;

	}
}
