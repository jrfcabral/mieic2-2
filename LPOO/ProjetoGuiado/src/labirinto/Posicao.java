package labirinto;

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
	
	public Posicao novaPosicao(char direcao){
		
		Posicao posicao = new Posicao(this);
		
		switch (direcao)
		{
		case 'w':
			posicao.x -= 1;
			break;
		case 's':
			posicao.x +=1;
			break;
		case 'a':
			posicao.y -=1;
			break;
		case 'd':
			posicao.y +=1;
			break;		
		}
		
		return posicao;
	}
	
	public boolean equals(Posicao posicao)
	{
		return posicao.x == this.x && posicao.y == this.y;			
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
}
