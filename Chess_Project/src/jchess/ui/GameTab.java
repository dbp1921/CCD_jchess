package jchess.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jchess.core.board.Chessboard;
import jchess.core.board.ChessboardField;
import jchess.core.pieces.King;
import jchess.core.util.Game;
import jchess.core.util.Logging;
import jchess.core.util.Player;
import jchess.core.util.Player.PlayerColor;
import jchess.core.util.ReadGameError;
import jchess.core.util.Settings;
import jchess.ui.lang.Language;

/**
 * Class represent the game interface which is seen by a player and where are
 * located available for player options, current games
 * 
 * @author Jingyi Ma
 */
public class GameTab extends JPanel implements MouseListener, ComponentListener {

	private static final long			serialVersionUID	= 2028583024625710742L;

	private GameClock							gameClock					= null;
	private Game									game							= null;
	private ChessboardUI					chessboard				= null;
	private static MoveHistoryUI	moveHistory				= null;

	public GameTab() throws Exception {
		this.setLayout(null);

		moveHistory = new MoveHistoryUI(this);

		chessboard = new ChessboardUI(this, moveHistory);
		chessboard.setVisible(true);
		chessboard.setSize(ChessboardUI.img_width, ChessboardUI.img_height);
		chessboard.addMouseListener(this);
		chessboard.setLocation(new Point(0, 0));
		this.add(chessboard);

		game = new Game(chessboard.getChessboard(), this);

		gameClock = new GameClock(game);
		gameClock.setSize(new Dimension(200, 100));
		gameClock.setLocation(new Point(500, 0));
		this.add(gameClock);

		JScrollPane movesHistory = moveHistory.getScrollPane();
		movesHistory.setSize(new Dimension(190, 350));
		movesHistory.setLocation(new Point(500, 121));
		this.add(movesHistory);

		this.setLayout(null);
		this.addComponentListener(this);
		this.setDoubleBuffered(true);

		if (Settings.isTimeLimitSet()) {
			gameClock.setTime(Settings.getTimeForGame());
			gameClock.start();
		}
	}

	public void componentResized(ComponentEvent e) {
		try {
			int height = this.getHeight() >= this.getWidth() ? this.getWidth() : this.getHeight();
			int chess_height = (int) (height * 0.8 / 10);
			this.chessboard.resizeChessboard((int) chess_height);
			int chess_width = this.chessboard.getWidth();
			moveHistory.getScrollPane().setLocation(new Point(chess_width + 10, 100));
			moveHistory.getScrollPane().setSize(moveHistory.getScrollPane().getWidth(), chess_height * 9);
			this.gameClock.setLocation(new Point(chess_width + 5, 0));
		} catch (FileNotFoundException e1) {
			Logging.log(e1);
		}
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent event) {
		try {
			if (event.getButton() == MouseEvent.BUTTON1) // left button
			{

				int x = event.getX();// get X position of mouse
				int y = event.getY();// get Y position of mouse

				String id = chessboard.pixelPosition2id(x, y);

				ChessboardField field = chessboard.getField(id);
				if ((field == null || field.getPiece() == null && chessboard.getChessboard().getActiveField() == null)
						|| (this.chessboard.getChessboard().getActiveField() == null && field.getPiece() != null && field.getPiece().getPlayer() != game.getActivePlayer())) {
					return;
				}

				if (field.getPiece() != null && field.getPiece().getPlayer() == game.getActivePlayer() && field != chessboard.getChessboard().getActiveField()) {
					chessboard.getChessboard().unselect();
					chessboard.getChessboard().select(field);

					System.out.println("test");
					chessboard.drawActiveField(this.getGraphics(), id);

				} else if (chessboard.getChessboard().getActiveField() == field) // unselect
				{
					chessboard.getChessboard().unselect();
					chessboard.drawActiveField(this.getGraphics(), id);

				} else if (chessboard.getChessboard().getActiveField() != null && chessboard.getChessboard().getActiveField().getPiece() != null
						&& chessboard.getChessboard().getActiveField().getPiece().allMoves().indexOf(field) != -1) // move
				{
					chessboard.getChessboard().move(chessboard.getChessboard().getActiveField(), field);
					chessboard.getChessboard().unselect();
					chessboard.drawActiveField(this.getGraphics(), id);

					// switch player
					nextMove();

					// checkmate or stalemate
					King king;
					if (game.getActivePlayer().getColor() == PlayerColor.WHITE) {
						king = chessboard.getKingForColor(PlayerColor.WHITE);
					} else if (game.getActivePlayer().getColor() == PlayerColor.BLACK) {
						king = chessboard.getKingForColor(PlayerColor.BLACK);
					} else {
						king = chessboard.getKingForColor(PlayerColor.RED);
					}

					switch (king.isCheckmatedOrStalemated()) {
						case 1:
							game.endGame("Checkmate! " + king.getPlayer().getColor().toString() + " player lose!"); //$NON-NLS-1$ //$NON-NLS-2$
							break;
						case 2:
							game.endGame(Language.getString("Game.35")); //$NON-NLS-1$
							break;

					}
				}
			}
		} catch (Exception e) {
			// TODO Inform User
			Logging.log(e);
			chessboard.repaint();
			return;
		}
	}

	// MouseListener:
	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Method to save actual state of game
	 * 
	 * @param path
	 *          address of place where game will be saved
	 */
	public void saveGame(File path) {

		throw new UnsupportedOperationException("Saving of game not supported by now.");
	}

	public boolean undo() throws Exception {
		boolean status = false;

		status = chessboard.getChessboard().undo();
		if (status) {
			game.switchActive();
		} else {
			chessboard.repaint();// repaint for sure
		}
		return status;
	}

	public boolean rewindToBegin() throws Exception {
		boolean result = false;

		while (chessboard.getChessboard().undo()) {
			result = true;
		}

		return result;
	}

	public boolean rewindToEnd() throws Exception {
		boolean result = false;

		while (chessboard.getChessboard().redo()) {
			result = true;
		}

		return result;
	}

	public boolean redo() throws Exception {
		boolean status = chessboard.getChessboard().redo();
		if (status) {
			nextMove();
		} else {
			chessboard.repaint();// repaint for sure
		}

		return status;
	}

	/**
	 * Loading game method(loading game state from the earlier saved file)
	 * 
	 * @param file
	 *          File where is saved game
	 * @throws Exception
	 */

	public static void loadGame(File file) throws Exception {

		throw new UnsupportedOperationException("Load Game not implemented by now.");
	}

	/**
	 * Method checking in with of line there is an error
	 * 
	 * @param br
	 *          BufferedReader class object to operate on
	 * @param srcStr
	 *          String class object with text which variable you want to get in
	 *          file
	 * @return String with searched variable in file (whole line)
	 * @throws ReadGameError
	 *           class object when something goes wrong when reading file
	 */
	public static String getLineWithVar(BufferedReader br, String srcStr) throws ReadGameError {
		String str = new String();
		while (true) {
			try {
				str = br.readLine();
			} catch (java.io.IOException exc) {
				Logging.log(Language.getString("Game.21"), exc); //$NON-NLS-1$
			}
			if (str == null) {
				throw new ReadGameError();
			}
			if (str.startsWith(srcStr)) {
				return str;
			}
		}
	}

	/**
	 * Method to get value from loaded text line
	 * 
	 * @param line
	 *          Line which is readed
	 * @return result String with loaded value
	 * @throws ReadGameError
	 *           object class when something goes wrong
	 */
	static public String getValue(String line) throws ReadGameError {
		int from = line.indexOf("\""); //$NON-NLS-1$
		int to = line.lastIndexOf("\""); //$NON-NLS-1$
		int size = line.length() - 1;
		String result = new String();
		if (to < from || from > size || to > size || to < 0 || from < 0) {
			throw new ReadGameError();
		}
		try {
			result = line.substring(from + 1, to);
		} catch (java.lang.StringIndexOutOfBoundsException exc) {
			Logging.log(Language.getString("Game.24"), exc); //$NON-NLS-1$
			return "none"; //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * Method to Start new game
	 * 
	 * @throws Exception
	 * 
	 */
	public void newGame() throws Exception {
		game.startNewGame();

		GameTab activeGame = JChessView.getInstance().getActiveTabGame();
		if (activeGame != null && JChessView.getInstance().getNumberOfOpenedTabs() == 0) {
			activeGame.chessboard.resizeChessboard(activeGame.chessboard.get_height());
			activeGame.chessboard.repaint();
			activeGame.repaint();
		}
		chessboard.repaint();
		this.repaint();
	}

	/**
	 * Method to switch active players after move
	 * @throws Exception 
	 */
	public void switchActive() throws Exception {
		game.switchActive();
	}

	/**
	 * Method to go to next move (checks if game is local/network etc.)
	 * @throws Exception 
	 */
	public void nextMove() throws Exception {
		switchActive();
		Logging.log("next move, active player: " + game.getActivePlayer().getName() + ", color: " + game.getActivePlayer().getColor().name()); //$NON-NLS-1$ //$NON-NLS-2$
																																																																						// //$NON-NLS-3$
	}

	public Chessboard getChessboard() {
		return game.getChessboard();
	}

	public Player getActivePlayer() {
		return game.getActivePlayer();
	}

	public Component getChessboardUI() {
		return chessboard;
	}

	/**
	 * Method selecting piece in chessboard
	 * 
	 * @param field
	 *          chess board field to select (when clicked))
	 */
	public Game getActiveGame() {
		return this.game;
	}
}