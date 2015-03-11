package labirinto.logic;

import labirinto.logic.Direcao;

public class Posicao {
	private int x, y;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Posicao(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Posicao(Posicao posicao)
	{
		this.x = posicao.x;
		this.y = posicao.y;
	}
	
	public Posicao novaPosicao(Direcao direcao){
		return novaPosicao(direcao, 1);
	}
	
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
	
	public boolean equals(Object obj)
	{	if(obj instanceof Posicao){
			return ((Posicao)obj).x == this.x && ((Posicao)obj).y == this.y;
		}
		return false;		
	}
	public boolean isAdjacente(Posicao posicao)
	{
		int dx = Math.abs(this.x - posicao.x), dy = Math.abs(this.y - posicao.y);
		
		
		return (dx + dy)*(dx+dy) == 1; 		 
		
	}
	
	public Posicao[] getAdjacencias()
	{
		int x = this.x, y = this.y;

		Posicao[] adjacencias = new Posicao[] {new Posicao(x+1,y),new Posicao(x,y+1), new Posicao(x-1,y), new Posicao(x,y-1)};
		return adjacencias;
	}
	
	public String toString(){
		return "(" + this.getX() + ", " + this.getY() + ")";
	}
}
