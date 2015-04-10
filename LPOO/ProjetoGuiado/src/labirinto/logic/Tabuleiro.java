package labirinto.logic;

import java.io.Serializable;
import java.util.Random;

public class Tabuleiro implements Serializable{
	
	private static final long serialVersionUID = 8667657502078377355L;
	private Terreno[][] mapa;
	int dimensao;
	
	public Tabuleiro(Terreno[][] mapa, int dimensao)
	{
		this.mapa = mapa;		
		this.dimensao = dimensao;
	}
	
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

	public Terreno at(int x, int y)
	{
		if (x < dimensao && y < dimensao && x >= 0 && y >= 0)
			return mapa[x][y];
		else
			throw new IndexOutOfBoundsException();
	}	
	public Terreno at(Posicao pos)
	{
		return at(pos.getX(),pos.getY());
	}
}
