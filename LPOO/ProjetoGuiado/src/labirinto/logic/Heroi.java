package labirinto.logic;

public class Heroi extends Peca {
	private boolean hasSword;
	private boolean hasJavelin;
	private boolean shielded;
	
	public boolean hasSword() {
		return hasSword;
	}

	public void setArmado(boolean armado) {
		this.hasSword = armado;
	}
	
	public boolean getArmado(){
		return this.hasSword;
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
		hasSword = false;		
	}
	

	
}
