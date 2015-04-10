package labirinto.logic;

public class Dragao extends Peca {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6506962757701472451L;
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
		acordado = true;
	}
}
