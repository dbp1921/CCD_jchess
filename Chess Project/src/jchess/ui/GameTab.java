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
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jchess.JChessApp;
import jchess.core.board.Chessboard;
import jchess.core.board.Square;
import jchess.core.pieces.King;
import jchess.core.util.Constants;
import jchess.core.util.Game;
import jchess.core.util.Logging;
import jchess.core.util.Moves;
import jchess.core.util.Player;
import jchess.core.util.ReadGameError;
import jchess.core.util.Settings;
import jchess.ui.lang.Language;

/**
 * Class representing the game interface which is seen by a player and where are
 * located available for player options, current games and where can he start a
 * new game (load it or save it)
 */
public class GameTab extends JPanel implements MouseListener, ComponentListener {

	private static final long	serialVersionUID	= 2028583024625710742L;

	private GameClock					gameClock					= null;
	private static Game				game							= null;
	private ChessboardUI			chessboard				= null;
	private static Moves			moveHistory				= null;

	public GameTab() throws Exception {
		this.setLayout(null);

		moveHistory = new Moves(this);

		chessboard = new ChessboardUI(this, moveHistory);
		chessboard.setVisible(true);
		chessboard.setSize(ChessboardUI.img_height, ChessboardUI.img_widht);
		chessboard.addMouseListener(this);
		chessboard.setLocation(new Point(0, 0));
		this.add(chessboard);

		game = new Game(chessboard.getChessboard(), this);

		gameClock = new GameClock(game);
		gameClock.setSize(new Dimension(200, 100));
		gameClock.setLocation(new Point(500, 0));
		this.add(gameClock);

		JScrollPane movesHistory = moveHistory.getScrollPane();
		movesHistory.setSize(new Dimension(180, 350));
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
			int chess_height = (int) Math.round((height * 0.8) / 8) * 8;
			this.chessboard.resizeChessboard((int) chess_height);
			chess_height = this.chessboard.getHeight();
			moveHistory.getScrollPane().setLocation(new Point(chess_height + 5, 100));
			moveHistory.getScrollPane().setSize(moveHistory.getScrollPane().getWidth(), chess_height - 100);
			this.gameClock.setLocation(new Point(chess_height + 5, 0));
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

				Square sq = chessboard.getSquare(x, y);
				if ((sq == null || sq.piece == null && chessboard.getChessboard().activeSquare == null)
						|| (this.chessboard.getChessboard().activeSquare == null && sq.piece != null && sq.piece.player != game.getActivePlayer())) {
					return;
				}

				if (sq.piece != null && sq.piece.player == game.getActivePlayer() && sq != chessboard.getChessboard().activeSquare) {
					chessboard.getChessboard().unselect();
					chessboard.getChessboard().select(sq);
				} else if (chessboard.getChessboard().activeSquare == sq) // unselect
				{
					chessboard.getChessboard().unselect();
				} else if (chessboard.getChessboard().activeSquare != null && chessboard.getChessboard().activeSquare.piece != null
						&& chessboard.getChessboard().activeSquare.piece.allMoves().indexOf(sq) != -1) // move
				{
					chessboard.getChessboard().move(chessboard.getChessboard().activeSquare, sq);
					chessboard.getChessboard().unselect();

					// switch player
					nextMove();

					// checkmate or stalemate
					King king;
					if (game.getActivePlayer() == game.getWhitePlayer()) {
						king = chessboard.getWhiteKing();
					} else {
						king = chessboard.getBlackKing();
					}

					switch (king.isCheckmatedOrStalemated()) {
						case 1:
							game.endGame("Checkmate! " + king.player.getColor().toString() + " player lose!"); //$NON-NLS-1$ //$NON-NLS-2$
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
		File file = path;
		FileWriter fileW = null;
		try {
			fileW = new FileWriter(file);
		} catch (java.io.IOException exc) {
			Logging.log(Language.getString("Game.0"), exc); //$NON-NLS-1$
			JOptionPane.showMessageDialog(this, Language.getString("Game.1") + ": " + exc); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		Calendar cal = Calendar.getInstance();
		String str = new String(""); //$NON-NLS-1$
		String info = new String("[Event \"Game\"]\n[Date \"" + cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "\"]\n" + "[White \"" + game.getWhitePlayer().getName() + "\"]\n[Black \"" + game.getBlackPlayer().getName() + "\"]\n\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		str += info;
		str += moveHistory.getMovesInString();
		try {
			fileW.write(str);
			fileW.flush();
			fileW.close();
		} catch (java.io.IOException exc) {
			Logging.log(Language.getString("Game.11"), exc); //$NON-NLS-1$
			JOptionPane.showMessageDialog(this, Language.getString(Language.getString("Game.12")) + ": " + exc); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		JOptionPane.showMessageDialog(this, Language.getString("game_saved_properly")); //$NON-NLS-1$
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
		FileReader fileR = null;
		try {
			fileR = new FileReader(file);
		} catch (java.io.IOException exc) {
			Logging.log(Language.getString("Game.2"), exc); //$NON-NLS-1$
			return;
		}
		BufferedReader br = new BufferedReader(fileR);
		String tempStr = new String();
		String blackName, whiteName;
		try {
			tempStr = getLineWithVar(br, new String("[White")); //$NON-NLS-1$
			whiteName = getValue(tempStr);
			tempStr = getLineWithVar(br, new String("[Black")); //$NON-NLS-1$
			blackName = getValue(tempStr);
			tempStr = getLineWithVar(br, new String("1.")); //$NON-NLS-1$
		} catch (ReadGameError err) {
			Logging.log(Language.getString("Game.19"), err); //$NON-NLS-1$
			return;
		}

		GameTab newGUI = JChessApp.jcv.addNewTab(whiteName + " vs. " + blackName); //$NON-NLS-1$

		Player playerWhite = new Player(Constants.EMPTY_STRING, Player.colors.white);
		Player playerBlack = new Player(Constants.EMPTY_STRING, Player.colors.black);

		playerBlack.setName(blackName);
		playerWhite.setName(whiteName);

		game.setWhitePlayer(playerWhite);
		game.setBlackPlayer(playerBlack);

		Settings.setBlackPlayersName(blackName);
		Settings.setWhitePlayersName(whiteName);
		Settings.setGameMode(Settings.gameModes.loadGame);

		newGUI.newGame();
		moveHistory.setMoves(tempStr);
		newGUI.chessboard.repaint();
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
	 * Method to get value from loaded txt line
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
		game.newGame();

		GameTab activeGame = JChessApp.jcv.getActiveTabGame();
		if (activeGame != null && JChessApp.jcv.getNumberOfOpenedTabs() == 0) {
			activeGame.chessboard.resizeChessboard(activeGame.chessboard.get_height(false));
			activeGame.chessboard.repaint();
			activeGame.repaint();
		}
		chessboard.repaint();
		this.repaint();
	}

	/**
	 * Method to switch active players after move
	 */
	public void switchActive() {
		game.switchActive();
		this.gameClock.switch_clocks();
	}

	/**
	 * Method to go to next move (checks if game is local/network etc.)
	 */
	public void nextMove() {
		switchActive();
		Logging.log("next move, active player: " + game.getActivePlayer().getName() + ", color: " + game.getActivePlayer().getColor().name()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

	public Player getWhitePlayer() {
		return game.getWhitePlayer();
	}

	public Player getBlackPlayer() {
		return game.getBlackPlayer();
	}
}