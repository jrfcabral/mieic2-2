package labirinto.logic;

import java.io.Serializable;

import labirinto.logic.Direcao;

/**
 * Class that represents a position within the maze
 *
 */
public class Posicao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6358062872403103908L;
	private int x, y;
	
	/**
	 * Getter for the x variable which represents the row of a position.
	 * @return the row of the position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter for the x variable which represents the row of a position.
	 * @param x the value to be asigned
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Getter for the y variable which represents the column of a position.
	 * @return the column of the position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter for the y variable which represents the column of a position.
	 * @param y the value to be asigned
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Constructor for the Posicao class.
	 * @param x the row that the instance shall indicate.
	 * @param y the column that the position shall indicate.
	 */
	public Posicao(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor for the Posicao class which makes a creates a new instance equal to the one provided.
	 * @param posicao the position to equal
	 */
	public Posicao(Posicao posicao)
	{
		this.x = posicao.x;
		this.y = posicao.y;
	}
	
	
	public Posicao novaPosicao(Direcao direcao){
		return novaPosicao(direcao, 1);
	}
	
	/**
	 * Re-calculates a position based on the provided direction and distance.
	 * @param direcao Direction of the movement
	 * @param distancia How many cells were traversed
	 * @return
	 */
	public Posicao novaPosicao(Direcao direcao, int distancia){
		
		Posicao posicao = new Posicao(this);
		
		switch (direcao)
		{
		case CIMA:
			posicao.x -= distancia;
			break;
		case BAIXO:
			posicao.x +=distancia;
			break;
		case ESQUERDA:
			posicao.y -=distancia;
			break;
		case DIREITA:
			posicao.y +=distancia;
			break;
		default:
			break;
		}
		
		return posicao;
	}
	
	/**
	 * Checks whether the provided object has the same coordinates as the one this method is called from
	 * @param obj object to compare
	 */
	public boolean equals(Object obj)
	{	if(obj instanceof Posicao){
			return ((Posicao)obj).x == this.x && ((Posicao)obj).y == this.y;
		}
		return false;		
	}
	
	/**
	 * Checks whether the provided position is directly adjacent to the one this method is called from
	 * @param posicao
	 * @return
	 */
	public boolean isAdjacente(Posicao posicao)
	{
		int dx = Math.abs(this.x - posicao.x), dy = Math.abs(this.y - posicao.y);
		
		
		return (dx + dy)*(dx+dy) == 1; 		 
		
	}
	
	/**
	 * Creates an array with the 4 directly adjacent positions to the one this method is called from.
	 * @return an array containing all the directly adjacent positions.
	 */
	public Posicao[] getAdjacencias()
	{
		int x = this.x, y = this.y;

		Posicao[] adjacencias = new Posicao[] {new Posicao(x+1,y),new Posicao(x,y+1), new Posicao(x-1,y), new Posicao(x,y-1)};
		return adjacencias;
	}
	
	/**
	 * Converts the instance this method is called from into a string
	 */
	public String toString(){
		return "(" + this.getX() + ", " + this.getY() + ")";
	}
}
