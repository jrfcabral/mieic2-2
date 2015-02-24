package labirinto;

public class Peca {
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
	
	
	
	public Peca(Posicao posicao, char type)
	{
		this.posicao = posicao;
		this.type = type;
	}
	public Peca(int x, int y, char type)
	{
		this.posicao = new Posicao(x,y);
		this.type = type;
	}
	
}
