package jchess.core.util;

import java.io.Serializable;

/**
 * Class representing the player in the game
 * 
 */
public class Player implements Serializable {

	private static final long	serialVersionUID	= 8990270306651014243L;

	public String							name							= null;
	public colors							color							= null;
	public boolean						goDown						= false;

	public enum colors {
		white, black, gray
	}

	public Player() {
	}

	public Player(String name, colors color) {
		this.name = name;
		this.color = color;
	}

	/**
	 * Method setting the players name
	 * 
	 * @param name
	 *          name of player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method getting the players name
	 * 
	 * @return name of player
	 */
	public String getName() {
		return this.name;
	}
}