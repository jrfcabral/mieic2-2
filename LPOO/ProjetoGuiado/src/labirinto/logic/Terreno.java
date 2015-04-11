package labirinto.logic;

/**
 * Enum used to make the maze's terrain (everything but the pieces)
 *
 */
public enum Terreno {PAREDE, CHAO, SAIDA, ERRO;
	
	/**
	 * Converts an instance of Terreno to the corresponding character
	 */
	public String toString()
	{
		switch(this){
		case PAREDE:
			return "X";
		case CHAO:
			return " ";
		case SAIDA:
			return "S";
		default:
			return null;				
		}
		
	}
	

}
