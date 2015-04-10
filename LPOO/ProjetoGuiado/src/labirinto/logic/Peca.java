package labirinto.logic;

import java.io.Serializable;

/**
 * Class that acts as Superclass for all of the game's pieces
 * @see Heroi
 * @see Dragon
 * @see Espada
 * @see Dardo
 * @see Escudo
 *
 */
public class Peca implements Serializable{
	private Posicao posicao;
	//private char type;
	/**
	 * Getter for the posicao variable which represents the position within the maze where the piece is.
	 * @return the aforementioned position in the form of an instance of the Posicao class
	 * @see Posicao
	 */
	public Posicao getPosicao() {
		return posicao;
	}
	
	/**
	 * Setter for the posicao variable which represents the position within the maze where the piece is.
	 * @param posicao the new position where the piece is to be placed.
	 * @see Posicao
	 */
	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}
	
	/*public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}*/
	
	
	
	/**
	 * Constructor for the Peca class.
	 * @param posicao the position in which the piece is to be placed in the form of an instance of the Posicao class
	 * @see Posicao
	 */
	public Peca(Posicao posicao)
	{
		this.posicao = posicao;
		
	}
	
	/**
	 * Constructor for the Peca class.
	 * @param x the row upon which the piece will be placed
	 * @param y the column upon which the piece will be placed.
	 */
	public Peca(int x, int y)
	{
		this(new Posicao(x,y));		
	}
	
}
