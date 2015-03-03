package labirinto.logic;

public class Heroi extends Peca {
	boolean armado;
	
	public boolean isArmado() {
		return armado;
	}

	public void setArmado(boolean armado) {
		this.armado = armado;
	}

	public Heroi(Posicao posicao)
	{
		super(posicao);
		armado = false;		
	}
}
