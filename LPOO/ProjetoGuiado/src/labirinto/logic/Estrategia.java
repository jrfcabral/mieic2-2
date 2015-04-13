package labirinto.logic;

/**
 * Class that represents the dragon's behavior during the game.
 * The dragon will either be permanently awake, permanently in the same place, or fully mobile and will randomly fall asleep.
 *
 */
public enum Estrategia {
	SEMPRE_ACORDADO, PARADO, ALTERNADO;
	
	/**
	 * Translates an integer into the corresponding enum.
	 */
	public static Estrategia  fromNumber(int number)
	{
		switch (number){
		case 1:
			return Estrategia.SEMPRE_ACORDADO;
		case 2:
			return Estrategia.PARADO;
		case 3:
			return Estrategia.ALTERNADO;
		default:
			throw new IllegalArgumentException("Estratégia não existe!");
		}
	}
	
	/**
	 * Translates an instance of the Estrategia enum into the corresponding integer.
	 */
	public int toNumber(){
		switch (this){
			case SEMPRE_ACORDADO:
				return 1;
			case PARADO:
				return 2;
			case ALTERNADO:
				return 3;
		}
		return 0;
	}
	
	/**
	 * Translates an instance of the Estrategia enum into a string
	 * (Overrides the default toString method)
	 */
	@Override public String toString(){
		switch(this){
		case PARADO:
			return "Dragons don't move";
		case ALTERNADO:
			return "Dragons move and ocasionally fall asleep";
		case SEMPRE_ACORDADO:
			return "Dragons move and don't sleep";
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Determines whether obj equals the Estrategia instance from which the function was called.
	 * @param obj An instance of the Estrategia enum.
	 * @return true if they are equal, false otherwise.
	 */
	public boolean equals(Estrategia obj){
		//if(obj instanceof Estrategia){
			if(this.toNumber() == obj.toNumber()){
				return true;
			}
		//}
		return false;
	}
}
