package labirinto.logic;

import java.io.Serializable;

/**
 * Class that represents the game's javelin.
 */
public class Dardo extends Peca implements Serializable{
	
	/**
	 * Constructor for the Dardo class
	 * @param posicao Position within the maze where the instance will be placed.
	 */
	public Dardo(Posicao posicao){
		super(posicao);
	}
}
