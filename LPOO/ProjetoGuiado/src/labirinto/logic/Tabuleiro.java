package labirinto.logic;

import java.util.Random;

public class Tabuleiro {
	private Terreno[][] mapa;
	int dimensao;
	
	public Tabuleiro(Terreno[][] mapa, int dimensao)
	{
		this.mapa = mapa;
		
		MazeGenerator.printRandomMaze(11);
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
