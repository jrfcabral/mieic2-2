package labirinto.logic;

import java.util.Random;

public enum Direcao {
	CIMA,BAIXO,ESQUERDA,DIREITA,NONE;
	
	public static Direcao randomDirecao() {
	Random r = new Random();
	Direcao dir;
	do{
		dir = Direcao.values()[r.nextInt(Direcao.values().length)];
	}while(dir == Direcao.NONE);
	return dir;
	}

}
