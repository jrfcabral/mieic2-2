package labirinto.logic;

import java.util.Random;

/**
 * Enum responsible for distinguishing between the 4 available directions.
 *
 */
public enum Direcao {
	CIMA,BAIXO,ESQUERDA,DIREITA,NONE;
	
	/**
	 * Generates a random direction.
	 * @return A randomly generated instance of the Direcao enum 
	 */
	public static Direcao randomDirecao() {
	Random r = new Random();
	Direcao dir;
	do{
		dir = Direcao.values()[r.nextInt(Direcao.values().length)];
	}while(dir == Direcao.NONE);
	return dir;
	}

}
