package labirinto.logic;

public class Dragao extends Peca {
	boolean acordado;
	
	public boolean isAcordado() {
		return acordado;
	}

	public void setAcordado(boolean acordado) {
		this.acordado = acordado;
	}

	public Dragao(Posicao posicao)
	{
		super(posicao);
		acordado = false;
	}
}
