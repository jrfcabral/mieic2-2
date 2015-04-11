package labirinto.logic;

/**
 * Class that represents the game's hero.
 *
 */
public class Heroi extends Peca {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1442317230032553310L;
	private boolean armado;
	private boolean hasJavelin;
	private boolean shielded;
	
	/**
	 * Getter for the armado variable, which represents whether or not the hero has the sword.
	 * @return true if he does have the sword, false otherwise.
	 */
	public boolean isArmado() {
		return armado;
	}

	/**
	 * Setter for the armado variable, which represents whether or not the hero has the sword.
	 * @param armado boolean value to assign to the variable.
	 */
	public void setArmado(boolean armado) {
		this.armado = armado;
	}	

	/**
	 * Setter for the hasJavelin variable which represents whether or not the hero has a javelin.
	 * @param javelin boolean value to assign to the variable.
	 */
	public void setHasJavelin(boolean javelin){
		this.hasJavelin = javelin;
	}
	
	/**
	 * Getter for the hasJavelin variable which represents whether or not the hero has a javelin.
	 * @return true if the hero does have a javelin, false otherwise.
	 */
	public boolean hasJavelin() {
		return hasJavelin;
	}
	
	/**
	 *  Setter for the shielded variable which represents whether or not the hero has the shield.
	 * @param shield boolean value to assign to the variable.
	 */
	public void setShielded(boolean shield){
		this.shielded = shield;
	}
	
	/**
	 *  Getter for the shielded variable which represents whether or not the hero has the shield.
	 * @return true if the hero does have the shield, false otherwise.
	 */
	public boolean hasShield(){
		return this.shielded;
	}
	
	/**
	 * Constructor for the Heroi class.
	 * @param posicao Position within the maze where the hero will be placed.
	 */
	public Heroi(Posicao posicao){
		super(posicao);
		armado = false;		
	}
	

	
}
