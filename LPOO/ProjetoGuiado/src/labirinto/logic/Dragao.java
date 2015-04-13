package labirinto.logic;

import java.io.Serializable;

/**
 * Class that represents the game's dragon
 *
 */
public class Dragao extends Peca implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6506962757701472451L;
	boolean acordado;
	
	/**
	 * Getter for the acordado variable which determines if a dragon is awake or asleep.
	 * @return true if the dragon is awake, false if he is not.
	 */
	public boolean isAcordado() {
		return acordado;
	}

	/**
	 * Setter for the acordado variable which determines if a dragon is awake or asleep.
	 * @param acordado value to assign the variable
	 */
	public void setAcordado(boolean acordado) {
		this.acordado = acordado;
	}
	
	/**
	 * Constructor for the Dragao class.
	 * @param posicao Position within the maze where the dragon will be placed.
	 */
	public Dragao(Posicao posicao)
	{
		super(posicao);
		acordado = true;
	}
}
