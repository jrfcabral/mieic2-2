package labirinto.logic;

import java.io.Serializable;
import java.util.Random;

/**
 * Class responsible for 
 *
 */
public class Tabuleiro implements Serializable{
	
	private static final long serialVersionUID = 8667657502078377355L;
	private Terreno[][] mapa;
	int dimensao;
	
	/**
	 * Constructor for the Tabuleiro class.
	 * @param mapa the maze
	 * @param dimensao size of the maze's outer walls
	 */
	public Tabuleiro(Terreno[][] mapa, int dimensao)
	{
		this.mapa = mapa;		
		this.dimensao = dimensao;
	}
	
	/**
	 * Generates random coordinates until one of those corresponds to a free position within the maze.
	 * @return the new position
	 */
	public Posicao getFreeCell() {
		Random r = new Random();
		int randx;
		int randy;
		do
		{
			randx = r.nextInt(dimensao);
			randy = r.nextInt(dimensao);			
		}while(mapa[randx][randy] != Terreno.CHAO);
		
		return new Posicao(randx, randy);
	}
	
	/**
	 * Checks whether or not a position is valid.
	 * @param posicao the position to check
	 * @return true if the position is valid, false otherwise
	 */
	public boolean validaPosicao(Posicao posicao)
	{
		
		if (posicao.getX() > dimensao-1 || posicao.getX() < 0 || posicao.getY() > dimensao -1 || posicao.getY() < 0)
			return false;
		if (this.mapa[posicao.getX()][posicao.getY()] == Terreno.PAREDE)
			return false;
		return true;
	}
	

	public void set(int x, int y, Terreno terreno){
		if (x < dimensao && y < dimensao && x >= 0 && y >= 0)
			mapa[x][y] = terreno;
		else
			throw new IndexOutOfBoundsException();
	}
	
	private void set(Posicao posicao, Terreno terreno) {
		set(posicao.getX(), posicao.getY(), terreno);		
	}


	/**
	 * Determines what is in the provided position within the maze.
	 * @param x
	 * @param y
	 * @return The instance of the Terreno enum corresponding to the one at the provided position
	 */
	public Terreno at(int x, int y)
	{
		if (x < dimensao && y < dimensao && x >= 0 && y >= 0)
			return mapa[x][y];
		else
			throw new IndexOutOfBoundsException();
	}	
	
	/**
	 * Determines what is in the provided position within the maze.
	 * @param pos
	 * @return The instance of the Terreno enum corresponding to the one at the provided position
	 */
	public Terreno at(Posicao pos)
	{
		return at(pos.getX(),pos.getY());
	}
}
