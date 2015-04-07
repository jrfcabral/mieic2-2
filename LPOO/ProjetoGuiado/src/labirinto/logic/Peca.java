package labirinto.logic;

import java.io.Serializable;

public class Peca implements Serializable{
	private Posicao posicao;
	public Posicao getPosicao() {
		return posicao;
	}
	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	private char type;
	
	
	
	public Peca(Posicao posicao)
	{
		this.posicao = posicao;
		
	}
	public Peca(int x, int y)
	{
		this(new Posicao(x,y));		
	}
	
}
