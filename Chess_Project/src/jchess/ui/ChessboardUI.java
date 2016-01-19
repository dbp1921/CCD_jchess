package jchess.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JPanel;

import jchess.core.board.Chessboard;
import jchess.core.board.ChessboardField;
import jchess.core.board.HexagonChessFieldGraphInitializer;
import jchess.core.board.graph.GraphNode;
import jchess.core.pieces.King;
import jchess.core.pieces.Piece;
import jchess.core.util.Logging;
import jchess.core.util.Player.PlayerColor;
import jchess.ui.lang.Language;

/**
 *Class to represent chessboard. It sets the pieces wi
 *tch the owner is
 * current player on it.
 * 
 * @author Jingyi Ma
 */
public class ChessboardUI extends JPanel implements MouseListener {

	private static final long						serialVersionUID			= -1717218347823342830L;

	public static final int							top										= 0;
	public static final int							bottom								= 7;

	private Chessboard									board									= null;

	// image of chessboard
	private static Image								boardBackgroundImage	= null;

	// image of highlighted hexagon
	private static Image								sel_hexagon						= null;

	// image of square where piece can go
	private static Image								able_hexagon					= null;

	private Point												topLeft								= new Point(0, 0);
	// private float hexagon_height = 0;
	private float												hexagon_height				= 0;
	private float												hexagon_width					= 0;

	public static final int							img_width							= 850;
	public static final int							img_height						= 728;
	private float												deviation_height			= 14;
	private float												deviation_width				= 14;

	private ArrayList<ChessboardField>	moves									= null;

	private ChessboardField							m_ActiveField;

	/**
	 * Chessboard class constructor
	 * 
	 * @param settings
	 *          reference to Settings class object for this chessboard
	 * @param moves_history
	 *          reference to Moves class object for this chessboard
	 * @throws Exception 
	 */
	public ChessboardUI(GameTab gt, MoveHistoryUI moves_history) throws Exception {
		board = new Chessboard(gt, moves_history);
		board.unselect();

		this.hexagon_height = (img_height - deviation_height) / 10;
		this.hexagon_width = (img_width - deviation_width) / 13;

		this.setDoubleBuffered(true);

		sel_hexagon = Theme.getImage("sel_hexagon.png"); // change image
		able_hexagon = Theme.getImage("able_hexagon.png"); // the image should
																												// be a hexagon ??

		boardBackgroundImage = Theme.getImage("chessboard.jpg");

		// activate Mouse listener
		addMouseListener(this);

	}/*--endOf-Chessboard--*/

	public int get_width() { // get width of the whole component, the image with
														// labels
		return this.getWidth();
	}

	public int get_height() {
		return this.getHeight();
	}

	public int get_hexagon_height() {
		return (int) this.hexagon_height;
	}

	/**
	 * Method to draw Chessboard and their elements (pieces etc.)
	 */
	public void draw() {
		// draw image of chessboard
		this.getGraphics().drawImage(boardBackgroundImage, this.topLeft.x, this.topLeft.y, null);
		this.repaint();
	}/*--endOf-draw--*/

	/**
	 * Annotations to superclass Game updating and painting the chessboard
	 */
	@Override
	public void update(Graphics g) {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(boardBackgroundImage, topLeft.x, topLeft.y, null);
		// drawing image of chessboard

		for (GraphNode field : board.getAllNodes()) {
			if (field instanceof ChessboardField) {
				ChessboardField bf = (ChessboardField) field;
				if (bf.getPiece() != null) {
					try {
						drawPieceImage(g, bf.getPiece());
					} catch (FileNotFoundException e) {
						Logging.log(e);
					}
				}
			}
		}
		drawActiveField(g, board);
		// TODO add perform for mouse listener
		/**if some hexagon has been entered(clicked) by mouse, this hexagon gonna be active,
		 * draw this active hexagon red and give 
		 */
		/*
		 * if (board.getActiveField() != null) // if some hexagon is active { String
		 * id = board.getActiveField().getIdentifier(); int[] coordinate =
		 * HexagonChessFieldGraphInitializer.getcoordinatesFromID(id); int y = 1;
		 * int x = 1; x = coordinate[1]; y = coordinate[0]; if (y < 7) { x = (int)
		 * ((7 - y) * hexagon_width / 2 + (x - 1) * hexagon_width + deviation_width
		 * - 0.25 * hexagon_width); } if (y >= 7) { x = (int) ((y - 5) *
		 * hexagon_width / 2 + (x - (y - 6) - 1) * hexagon_width + deviation_width -
		 * 0.25 * hexagon_width); }
		 * 
		 * if (y == 1) { y = (int) (0.5 * hexagon_height + deviation_height - 0.25 *
		 * hexagon_height); } else { y = (int) ((y - 1) * 0.75 * hexagon_height +
		 * 0.5 * hexagon_height + deviation_height - 0.25 * hexagon_height); }
		 * 
		 * g2d.drawImage(sel_hexagon, x, y, null);
		 * 
		 * }
		 **/
		this.repaint();
	}

	private void drawActiveField(Graphics g, Chessboard b) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (b.getActiveField() != null) // if some hexagon is active
		{
			String id = b.getActiveField().getIdentifier();
			int[] coordinate = HexagonChessFieldGraphInitializer.getcoordinatesFromID(id);
			int y = 1;
			int x = 1;
			x = coordinate[1];
			y = coordinate[0];
			if (y < 7) {
				x = (int) ((7 - y) * hexagon_width / 2 + (x - 1) * hexagon_width + deviation_width - 0.25 * hexagon_width);
			}
			if (y >= 7) {
				x = (int) ((y - 5) * hexagon_width / 2 + (x - (y - 6) - 1) * hexagon_width + deviation_width - 0.25 * hexagon_width);

			}

			y = (int) ((y - 1) * 0.75 * hexagon_height + 0.5 * hexagon_height + deviation_height - 0.25 * hexagon_height);

			g2d.drawImage(sel_hexagon, x, y, null);

		}

	}

	private String pixelPosition2id(int x, int y) {

		int[] coordinate = { 1, 2 };
		coordinate[0] = (int) ((y + 0.25 * hexagon_height - deviation_height - 0.5 * hexagon_height) / (0.75 * hexagon_height)) + 1;
		if (coordinate[0] < 7) {
			coordinate[1] = (int) ((x + 0.25 * hexagon_width - deviation_width - (7 - coordinate[0]) * hexagon_width / 2) / hexagon_width) + 1;
		} else {
			coordinate[1] = (int) ((x + 0.25 * hexagon_width - deviation_width - (coordinate[0] - 5) * hexagon_width / 2) / hexagon_width) + 1;
		}
		char letter = 'A';
		switch (coordinate[0]) {
			case 1:
				letter = 'A';
				break;
			case 2:
				letter = 'B';
				break;
			case 3:
				letter = 'C';
			case 4:
				letter = 'D';
				break;
			case 5:
				letter = 'E';
				break;
			case 6:
				letter = 'F';
				break;
			case 7:
				letter = 'G';
				break;
			case 8:
				letter = 'H';
				break;
			case 9:
				letter = 'I';
				break;
			case 10:
				letter = 'J';
				break;
			case 11:
				letter = 'K';
				break;
			case 12:
				letter = 'L';
				break;
			case 13:
				letter = 'M';
				break;
			default:
				break;
		}
		String id = letter + String.valueOf(coordinate[1]);
		return id;
	}

	private void drawPieceImage(Graphics g, Piece piece) throws FileNotFoundException {
		try {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			String id = piece.getField().getIdentifier();

			int y = 1;
			int x = 1;
			int[] coordinate = HexagonChessFieldGraphInitializer.getcoordinatesFromID(id);

			x = coordinate[1];
			y = coordinate[0];
			if (y < 7) {
				x = (int) ((7 - y) * hexagon_width / 2 + (x - 1) * hexagon_width + deviation_width - 0.25 * hexagon_width);
			}
			if (y >= 7) {
				x = (int) ((y - 5) * hexagon_width / 2 + (x - (y - 6) - 1) * hexagon_width + deviation_width - 0.25 * hexagon_width);
			}

			if (y == 1) {
				y = (int) (0.5 * hexagon_height + deviation_height - 0.25 * hexagon_height);
			} else {
				y = (int) ((y - 1) * 0.75 * hexagon_height + 0.5 * hexagon_height + deviation_height - 0.25 * hexagon_height);
			}

			if (g != null) {
				Image tempImage = Theme.getImageForPiece(piece.getPlayer().getColor(), piece.getSymbol());
				BufferedImage resized = new BufferedImage((int) (0.5 * hexagon_width), (int) (0.5 * hexagon_height), BufferedImage.TYPE_INT_ARGB_PRE);
				Graphics2D imageGr = (Graphics2D) resized.createGraphics();
				imageGr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				imageGr.drawImage(tempImage, 0, 0, (int) (0.5 * hexagon_width), (int) (0.5 * hexagon_height), null);
				imageGr.dispose();
				Image img = resized;
				g2d.drawImage(img, x, y, null);
			} else {
				Logging.logError(Language.getString("ChessboardUI.7")); //$NON-NLS-1$
			}
		}

		catch (java.lang.NullPointerException exc) {
			Logging.log(Language.getString("ChessboardUI.8"), exc); //$NON-NLS-1$
		}
	}

	public void resizeChessboard(int height) throws FileNotFoundException {

		BufferedImage resized = new BufferedImage(img_width, img_height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = resized.createGraphics();
		g.drawImage(Theme.getImage("chessboard.jpg"), 0, 0, img_width, img_height, null); //$NON-NLS-1$
		g.dispose();
		boardBackgroundImage = resized.getScaledInstance(img_width, img_height, 0);
		this.setSize(img_width, img_height);

		resized = new BufferedImage((int) hexagon_width, (int) hexagon_height, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(able_hexagon, 0, 0, (int) hexagon_width, (int) hexagon_height, null);
		g.dispose();
		able_hexagon = resized.getScaledInstance((int) hexagon_width, (int) hexagon_height, 0);

		resized = new BufferedImage((int) hexagon_width, (int) hexagon_height, BufferedImage.TYPE_INT_ARGB_PRE);
		g = resized.createGraphics();
		g.drawImage(sel_hexagon, 0, 0, (int) hexagon_width, (int) hexagon_height, null);
		g.dispose();
		sel_hexagon = resized.getScaledInstance((int) hexagon_width, (int) hexagon_height, 0);

	}

	public void componentMoved(ComponentEvent e) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	public void componentShown(ComponentEvent e) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	public void componentHidden(ComponentEvent e) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	public Chessboard getChessboard() {
		return board;
	}

	public King getKingForColor(PlayerColor color) {
		return board.getKingForColor(color);
	}

	public ChessboardField getField(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Click on x: " + e.getX() + " Y: " + e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		this.setToolTipText("Please Click!");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing
		int x;
		int y;
		x = e.getX();
		y = e.getY();
		pixelPosition2id(x, y);

		// Graphics2D g;
		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// drawPieceImage();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Do nothing
	}

	/**
	 * Method selecting piece in chessboard
	 * 
	 *  TODO move to ChessboardUI after ui implementation finished
	 * 
	 * @param field
	 *          chess board field to select (when clicked))
	 */

	private GameTab m_GameUI = null;

	public void select(ChessboardField field) {
		this.m_ActiveField = field;
		m_GameUI.getChessboardUI().repaint();
	}

	/**
	 * Deselects the currently selected filed, if one is selected.
	 * 
	 *  TODO move to ChessboardUI after ui implementation finished
	 */
	public void unselect() {
		this.m_ActiveField = null;
		if (m_GameUI.getChessboardUI() != null) {
			m_GameUI.getChessboardUI().repaint();
		}
	}
}
