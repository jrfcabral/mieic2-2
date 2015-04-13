package labirinto.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import utilitarios.Hipotese;

/**
 * Class that represents the actual game. This class contains the functions to initialize the game process its turns.
 *
 */
public class Labirinto implements Serializable, GridQueryable<Character>{
	/*private static final char ESPACO = ' ';

	private static final char HEROI_ARMADO = 'A';

	private static final char PAREDE = 'X';

	private static final char ESPADA = 'E';

	private static final char HEROI = 'H';
	
	private static final char DRAGAO = 'D';
	
	private static final char DARDO = 'J'; //Javelin, a falta d melhor nome

	private static final char ESCUDO = 'P'; //Protec�ao, a falta d melhor nome*/
	
	private static final long serialVersionUID = -6678642295421935384L;

	private static final int NUMERO_DARDOS = 5;
	
	//em 10
	private static final int CHANCE_TO_SLEEP = 5;
	private static final int CHANCE_TO_WAKE = 5;

	private Tabuleiro tabuleiro;
	
	protected Heroi heroi;
	protected Espada espada;
	private Dardo[] dardos;
	private Escudo escudo;	
	protected Dragao[] dragoes;
	
	private boolean acabou;
	private boolean perdeu;
	
	private int dimensao;
//	private int numeroDragoes;
	private Estrategia estrategia;
	
	/**
	 * Getter for the dimensao variable which represents the size of the maze
	 */
	public int getDimensao() {		
		return dimensao;
	}
	
	/**
	 * Getter for the perdeu variable which represents whether or not the player lost the game.
	 * @return
	 */
	public boolean isPerdeu() {
		return perdeu;
	}

	/**
	 * Getter for the acabou variable which represents whether or not the game over
	 * @return 
	 */
	public boolean isAcabou() {
		return acabou;
	}
	
	/**
	 * Generates an instance of Labirinto with the predefined maze and a dragon that stands permanently still.
	 */
	public Labirinto(){
		this(MazeGenerator.getPredef(), MazeGenerator.getPredefSize(), 1, Estrategia.PARADO);
	}
	
	/**
	 * Constructor for the Labirinto class
	 * @param formatoTabuleiro layout of the maze
	 * @param dimensao size of the maze
	 * @param dragoes number of dragons to put in the maze
	 * @param estrategia strategy to apply to the dragons
	 */
	public Labirinto(Terreno[][] formatoTabuleiro, int dimensao, int dragoes, Estrategia estrategia)
	{
		Random rand = new Random();
		this.tabuleiro = new Tabuleiro(formatoTabuleiro, dimensao);		
		System.out.println("A inicializar novo labirinto, dimensao:"+dimensao);
		this.dimensao = dimensao;
		this.dragoes = new Dragao[dragoes];
		this.dardos = new Dardo[rand.nextInt(Labirinto.NUMERO_DARDOS) + 1];
		this.estrategia = estrategia;
		inicializarPecas();
		
	}
	
	/**
	 * Places the pieces in the maze.
	 */
	private void inicializarPecas() {		
		
		heroi = new Heroi(tabuleiro.getFreeCell());		
		espada = new Espada(tabuleiro.getFreeCell());
		escudo = new Escudo(tabuleiro.getFreeCell());
		
		for (int i = dragoes.length-1; i >= 0; i--){
			dragoes[i] = new Dragao(tabuleiro.getFreeCell());
		}
		
		for (int i = 0; i < dardos.length; i++){
			dardos[i] = new Dardo(tabuleiro.getFreeCell());
		}
			
		
	}	
	
	/**
	 *  Translates a cell from the Terreno enum into a character
	 */
	public Character getCellSymbol(int x, int y){
		
		return getCellSymbol(new Posicao(x,y));
	}
	
	/**
	 * Translates a cell from the Terreno enum into a character
	 */
	public Character getCellSymbol(Posicao posicao)
	{		
		if (heroi != null && posicao.equals(this.heroi.getPosicao())){
			if(heroi.hasShield()){
				if(heroi.isArmado()){
					return '&';
				}
				else if(heroi.hasJavelin()){
					return '$';
				}
				else return '@';
			}
			else if (heroi.isArmado())
				return 'A';
			else if(heroi.hasJavelin())
				return 'R';
			return 'H';
		}
	
		 if (espada != null && posicao.equals(this.espada.getPosicao())){
			for(int i = 0; i < dragoes.length; i++){
				if(dragoes[i] != null && dragoes[i].getPosicao().equals(this.espada.getPosicao())){
					return 'F';
				} 
			}
			return 'E';
		}
		 
		if(escudo != null && posicao.equals(this.escudo.getPosicao())){
			for(int i = 0; i < dragoes.length; i++){
				if(dragoes[i] != null && dragoes[i].getPosicao().equals(this.escudo.getPosicao())){
					return 'F';
				}
			}
			return 'P';
		}
			 
		for(int i = 0; i < dardos.length; i++){
			for(int j = 0; j < dragoes.length; j++){
				if(dardos[i] != null && dragoes[j] != null && dardos[i].getPosicao().equals(dragoes[j].getPosicao()) && posicao.equals(this.dardos[i].getPosicao())){
					return 'F';
				}
				else if(dardos[i] != null && posicao.equals(this.dardos[i].getPosicao())){
					return 'J';
				}
			}
		}
		
		
		
		for (Dragao dragao:dragoes){
			if (dragao != null && dragao.getPosicao().equals(posicao)) return dragao.isAcordado() ? 'D' : 'd';
		}
		
		
		return tabuleiro.at(posicao).toString().charAt(0);
	}
	
	/**
	 * "Throws" the javelin and checks if it hits
	 * @param dir direction in which the javelin is thrown
	 */
	public void atiraDardo(Direcao dir)
	{
		if (heroi.hasJavelin())
			for (int i = 0; i < dragoes.length; i++){
				if (isNaMira(heroi, dragoes[i], dimensao, dir))
					dragoes[i] = null;
			}
		
		heroi.setHasJavelin(false);
	
	}	
	
	//wasd:mover heroi
	/**
	 * Moves the hero and starts AI's turn 
	 * @param dir direction of movement for the hero
	 */
	public void move(Direcao dir)
	{
		movePeca(dir, heroi);		
		processaTurno();
		
	}
	
	/**
	 * Processes the turn for the game's AI.
	 */
	private void processaTurno() {
		
		if ( (heroi.isArmado() || heroi.hasJavelin()) && tabuleiro.at(heroi.getPosicao()) == Terreno.SAIDA && nenhumDragao())
			acabou = true;
		
		moverTodosOsDragoes();		
		apanharEquipamento();
	}
	
	/**
	 * Checks if the hero is in the same cell as a piece of equipment (i.e shield, sword or javelin) and if so, picks it up.
	 */
	private void apanharEquipamento() {
		if (espada != null && heroi.getPosicao().equals(espada.getPosicao())){
			if(heroi.hasJavelin() == true){
				heroi.setHasJavelin(false);
				for(int i = 0; i < dardos.length; i++){
					if(dardos[i] == null){
						dardos[i] = new Dardo(heroi.getPosicao());
					}
				}
			}
			heroi.setArmado(true);
			espada = null;
			return;
		}
		
		for(int i = 0; i < dardos.length; i++){
			if(dardos[i] != null && heroi.getPosicao().equals(dardos[i].getPosicao()) && heroi.hasJavelin() == false){
				heroi.setHasJavelin(true);
				if(heroi.isArmado()){
					heroi.setArmado(false);
					espada = new Espada(heroi.getPosicao());
				}
				dardos[i] = null;
				break;
			}
		}
		
		if(escudo != null && heroi.getPosicao().equals(escudo.getPosicao())){
			heroi.setShielded(true);
			escudo = null;
		}
	}
	
	/**
	 * Moves all the dragons within the array of dragons
	 */
	private void moverTodosOsDragoes() {
		for (int i = 0; i < dragoes.length; i++){
			
			if (dragoes[i] != null){
				
				if (estrategia == Estrategia.ALTERNADO){
					if (dragoes[i].isAcordado()){
						Hipotese hipotese = new Hipotese(Labirinto.CHANCE_TO_SLEEP, 10);
						if (hipotese.isSucesso())
							dragoes[i].setAcordado(false);
					}
					else{
						Hipotese hipotese = new Hipotese(Labirinto.CHANCE_TO_WAKE, 10);
						if (hipotese.isSucesso())
							dragoes[i].setAcordado(true);
					}					
				}
				
				if (dragoes[i].isAcordado() && !heroi.hasShield() && isNaMira(dragoes[i], heroi, 3))
				{
					perdeu = true;
					acabou = true;					
				}
						
				if (dragoes[i].isAcordado() && estrategia != Estrategia.PARADO)
					movePeca(Direcao.randomDirecao(), dragoes[i]);
				
				Posicao[] adjacentes = dragoes[i].getPosicao().getAdjacencias();
				
				for (Posicao adjacente: adjacentes){
					if (dragoes[i].getPosicao().equals(heroi.getPosicao()) || adjacente.equals(heroi.getPosicao())){						
						if(!heroi.isArmado() && dragoes[i].isAcordado()){
							perdeu = true;
							acabou = true;
							
						}
						else if (heroi.isArmado()){
							dragoes[i] = null;
							
						}
							
						
						break;
					}				
				}
			}
		}
	}
	
	/**
	 * Checks if there are any dragons left in the maze
	 * @return true if none are left, false otherwise
	 */
	public boolean nenhumDragao(){
		for(int i = 0; i < dragoes.length; i++){
			if(dragoes[i] != null)
				return false;
		}
		return true;
	}
	
	
	/**
	 * Moves a piece from one cell one adjacent to it depending of the provided direction
	 * @param dir direction of the movement
	 * @param peca piece to move
	 * @return true if the piece was successfully moved, false if otherwise (when it hits a wall)
	 */
	private boolean movePeca(Direcao dir, Peca peca) {
		Posicao novaPosicao = peca.getPosicao().novaPosicao(dir);
		if (tabuleiro.validaPosicao(novaPosicao)){
			peca.setPosicao(novaPosicao);
			return true;
		}
		return false;
	}
	
	/**
	 *  Checks whether a piece within striking range of an attack
	 * @param agressor the piece that attacks
	 * @param defensor the piece to check
	 * @param alcance range of the attack (in cells)
	 * @return
	 */
	private boolean isNaMira(Peca agressor, Peca defensor, int alcance)
	{
		return isNaMira(agressor, defensor, alcance, Direcao.values());
	}
	
	/**
	 * Checks whether a piece within striking range of an attack
	 * @param agressor the piece that attacks
	 * @param defensor the piece to check
	 * @param alcance range of the attack (in cells)
	 * @param dir direction(s) of the attack
	 * @return
	 */
	private boolean isNaMira(Peca agressor, Peca defensor, int alcance, Direcao dir)
	{
		for(int a = 1; a <= alcance; a++){				
			Posicao posAtual = agressor.getPosicao().novaPosicao(dir, a);
			Terreno terrenoAtual;
			
			try
			{
				terrenoAtual = tabuleiro.at(posAtual);
			}
			catch (IndexOutOfBoundsException e)
			{
				return false;
			}
			
			if (terrenoAtual == Terreno.PAREDE)
				return false;
			else if (defensor != null && defensor.getPosicao().equals(posAtual))
				return true;				
		}				
		return false;
	}
	
	/**
	 * Checks whether a piece within striking range of an attack
	 * @param agressor the piece that attacks
	 * @param defensor the piece to check
	 * @param alcance range of the attack (in cells)
	 * @param dir direction(s) of the attack
	 * @return
	 */
	private boolean isNaMira(Peca agressor, Peca defensor, int alcance, Direcao[] dir){
		
		for (int i = 0; i < dir.length; i++){
			if (dir[i] == Direcao.NONE)
				continue;
			Direcao currDir = dir[i];
			
			if (isNaMira(agressor, defensor, alcance, currDir))
				return true;
		}
		return false;
			
	}
	
	/**
	 * Special constructor used only for testing purposes
	 * @see labirinto.tests.TestLabirinto
	 * @param formatoTabuleiro the terrain of the maze
	 * @param dimensao size of the maze
	 * @param dragoes number of dragons 
	 * @param estrategia strategy to assign to the game
	 * @param heroPos position of the hero within the maze
	 * @param swordPos position of the sword within the maze
	 * @param dragonPos position of the first dragon within the maze
	 * @param shieldPos position of the shield within the maze
	 * @param javPos position of the javelin within the maze
	 */
	public Labirinto(Terreno[][] formatoTabuleiro, int dimensao, int dragoes, Estrategia estrategia, Posicao heroPos, Posicao swordPos, Posicao dragonPos, Posicao shieldPos, Posicao javPos){
		this(formatoTabuleiro, dimensao, dragoes, estrategia);
		this.heroi = new Heroi(heroPos);
		this.espada = new Espada(swordPos);
		this.escudo = new Escudo(shieldPos);
		this.dragoes[0] = new Dragao(dragonPos);
		for( int i = 0; i < dardos.length; i++){
			this.dardos[i] = null;
		}
		this.dardos[0] = new Dardo(javPos);
	}
	
	
	/**
	 * Getter for the Heroi instance of the Labirinto from which the method is called.
	 * @return 
	 */
	public Heroi getHeroi(){
		return this.heroi;
	}
	
	/**
	 * Getter for the array of dargons (Only used for testing purposes)
	 * @see labirinto.tests.TestLabirinto
	 * @return
	 */
	public Dragao[] getDragoes(){
		return this.dragoes;
	}
	
	/**
	 * Serializes the instance of Labirinto from which the method is called and puts into a file with the specified name.
	 * @param filename Name of the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveState(String filename) throws FileNotFoundException, IOException{

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
		try{
			out.writeObject(this);
		}
		catch(Exception e){
			System.out.print(e.getMessage());
		}
		finally{
			if(out != null){ out.close();}
		}
		
		
	}
	
	/**
	 * Loads a game by de-serializing the objects contained in the specified file
	 * @param filename name of the file from which to load
	 * @return An instance of the labirinto class that ws serialized within the file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Labirinto loadState(String filename) throws FileNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
		Object obj = null;
		try{
			obj = in.readObject();
		}
		catch(Exception e){
			System.out.print(e.getMessage());
		}
		finally{
			if(in != null){in.close();}
		}
		return (Labirinto) obj;
	}
	
	
	
	/**
	 * Sets the terrain at the Tabuleiro's position to the specified type of terrain
	 * @param terreno terrain type to be set
	 * @param pos position to edit
	 * @see Posicao
	 * @see Tabuleiro
	 * @see Terreno
	 */
	public void setTerreno(Terreno terreno, Posicao pos){
		this.tabuleiro.set(pos.getX(), pos.getY(), terreno);
	}
	
	/**
	 * Sets the terrain at the Tabuleiro's position to the specified type of terrain
	 * @param terreno terrain type to be set
	 * @param x x coordinate of the position
	 * @param y y coordinate of the position
	 * @see Posicao
	 * @see Tabuleiro
	 * @see Terreno
	 */
	public void setTerreno(Terreno terreno, int x, int y){
		setTerreno(terreno, new Posicao(x,y));
	}
	/**
	 * Moves the sword to the x and y coordinates provided
	 * @param x x coordinate of the new sword position
	 * @param y y coordinate of the new sword position
	 * @see Espada
	 */
	public void setEspadaPosicao(int x, int y) {
		if (tabuleiro.at(x, y) == Terreno.CHAO && espada != null)
			this.espada.setPosicao(new Posicao(x,y));		
	}
	/**
	 * Moves the hero to the x and y coordinates provided
	 * @param x x coordinate of the new sword position
	 * @param y y coordinate of the new sword position
	 * @see Heroi
	 */
	public void setHeroiPosicao(int x, int y) {
		if (tabuleiro.at(x, y) == Terreno.CHAO)
			this.heroi.setPosicao(new Posicao(x,y));		
	}
	
	
	/**
	 * Toggles the existence of a dragon at the provided x and y coordinates
	 * @param x x coordinate of the position to toggle
	 * @param y y coordinate of the position to toggle
	 * @see Dragao
	 */
	public void toggleDragon(int x, int y){
		if (tabuleiro.at(x, y) != Terreno.CHAO)
			return;
		
		for (int i = 0; i < dragoes.length; i++){
			if (dragoes[i] != null){
				if (dragoes[i].getPosicao().equals(new Posicao(x,y))){
					dragoes[i] = null;
					return;
				}
			}						
		}
		dragoes = Arrays.copyOf(dragoes, dragoes.length+1);
		dragoes[dragoes.length-1] = new Dragao(new Posicao(x,y));
	}
	/**
	 * Toggles the existence of a javelin at the provided x and y coordinates
	 * @param x x coordinate of the position to toggle
	 * @param y y coordinate of the position to toggle
	 * @see Dardo
	 */
	public void toggleJavelin(int x, int y){
		if (tabuleiro.at(x, y) != Terreno.CHAO)
			return;
		
		for (int i = 0; i < dardos.length; i++){
			if (dardos[i] != null){
				if (dardos[i].getPosicao().equals(new Posicao(x,y))){
					dardos[i] = null;
					return;
				}
			}						
		}
		dardos = Arrays.copyOf(dardos, dardos.length+1);
		dardos[dardos.length-1] = new Dardo(new Posicao(x,y));
	}

	 /** Moves the shield to the x and y coordinates provided
	 * @param x x coordinate of the new sword position
	 * @param y y coordinate of the new sword position
	 * @see Escudo
	 */
	public void setShieldPosition(int x, int y) {
		if (tabuleiro.at(x, y) == Terreno.CHAO && escudo != null)
			this.escudo.setPosicao(new Posicao(x,y));		
	}
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Labirinto) || obj == null)
			return false;
		Labirinto lab = (Labirinto)obj;
		if (lab.getDimensao() != this.getDimensao())
			return false;
		
		for(int i = 0; i != lab.getDimensao(); i++)
			for (int j = 0; j != lab.getDimensao(); j++)
				if (lab.getCellSymbol(i, j) != this.getCellSymbol(i, j))
					return false;
		return true;
	}
}
