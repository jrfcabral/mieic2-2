package labirinto.logic;

public enum Terreno {PAREDE, CHAO, SAIDA, ERRO;
	
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
