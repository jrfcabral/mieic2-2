package labirinto.logic;

public enum Estrategia {
	SEMPRE_ACORDADO, PARADO, ALTERNADO;
	
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
	
	@Override public String toString(){
		switch(this){
		case PARADO:
			return "Sempre parados";
		case ALTERNADO:
			return "Alternar dormir com acordar";
		case SEMPRE_ACORDADO:
			return "Nunca dormir";
		default:
			throw new IllegalArgumentException();
		}
	}
}
