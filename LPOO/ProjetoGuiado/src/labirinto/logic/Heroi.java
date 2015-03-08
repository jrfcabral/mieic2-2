package labirinto.logic;

public class Heroi extends Peca {
	private boolean armado;
	private boolean hasJavelin;
	private boolean shielded;
	
	public boolean isArmado() {
		return armado;
	}

	public void setArmado(boolean armado) {
		this.armado = armado;
	}
	
	public boolean getArmado(){
		return this.armado;
	}

	public void setHasJavelin(boolean javelin){
		this.hasJavelin = javelin;
	}
	
	public boolean hasJavelin() {
		return hasJavelin;
	}
	
	public void setShielded(boolean shield){
		this.shielded = shield;
	}
	
	public boolean hasShield(){
		return this.shielded;
	}
	
	public Heroi(Posicao posicao){
		super(posicao);
		armado = false;		
	}
	

	
}
