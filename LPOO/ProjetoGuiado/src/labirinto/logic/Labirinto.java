package labirinto.logic;

import java.util.Random;

import utilitarios.Hipotese;

public class Labirinto {
	private static final char ESPACO = ' ';

	private static final char HEROI_ARMADO = 'A';

	private static final char PAREDE = 'X';

	private static final char ESPADA = 'E';

	private static final char HEROI = 'H';
	
	private static final char DRAGAO = 'D';
	
	private static final char DARDO = 'J'; //Javelin, a falta d melhor nome

	private static final char ESCUDO = 'P'; //Protec�ao, a falta d melhor nome
	
	private static final int NUMERO_DARDOS = 5;
	
	//em 10
	private static final int CHANCE_TO_SLEEP = 5;
	private static final int CHANCE_TO_WAKE = 5;

	private Tabuleiro tabuleiro;
	
	private Heroi heroi;
	private Espada espada;
	private Dardo[] dardos;
	private Escudo escudo;	
	private Dragao[] dragoes;
	
	private boolean acabou;
	private boolean perdeu;
	
	private int dimensao;
	private int numeroDragoes;
	private Estrategia estrategia;
	
	public int getDimensao() {
		return dimensao;
	}

	public boolean isPerdeu() {
		return perdeu;
	}

	public boolean isAcabou() {
		return acabou;
	}

	public Labirinto(Terreno[][] formatoTabuleiro, int dimensao, int dragoes, Estrategia estrategia)
	{
		Random rand = new Random();
		this.tabuleiro = new Tabuleiro(formatoTabuleiro, dimensao);		
		this.dimensao = dimensao;
		this.dragoes = new Dragao[dragoes];
		this.dardos = new Dardo[rand.nextInt(Labirinto.NUMERO_DARDOS) + 1];
		this.estrategia = estrategia;
		inicializarPecas();
		
	}
	
	
	
	

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

	
	
	public char getCellSymbol(int x, int y){
		
		return getCellSymbol(new Posicao(x,y));
	}
	
	
	
	
	public char getCellSymbol(Posicao posicao)
	{		
		if (heroi != null && posicao.equals(this.heroi.getPosicao())){
			if(heroi.hasShield()){
				if(heroi.hasSword()){
					return '&';
				}
				else if(heroi.hasJavelin()){
					return '$';
				}
				else return '@';
			}
			else if (heroi.hasSword())
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
				else if(dardos[i] != null && dragoes[j] != null && posicao.equals(this.dardos[i].getPosicao())){
					return 'J';
				}
			}
		}
		
		
		
		for (Dragao dragao:dragoes){
			if (dragao != null && dragao.getPosicao().equals(posicao)) return dragao.isAcordado() ? 'D' : 'd';
		}
		
		
		return tabuleiro.at(posicao).toString().charAt(0);
	}
	
	
	public void atiraDardo(Direcao dir)
	{
		for (int i = 0; i < dragoes.length; i++){
			if (isNaMira(heroi, dragoes[i], dimensao, dir))
				dragoes[i] = null;
		}
	
	}	
	
	//wasd:mover heroi
	public void move(Direcao dir)
	{
		movePeca(dir, heroi);		
		processaTurno();
		
	}

	private void processaTurno() {
		
		if ( (heroi.hasSword() || heroi.hasJavelin()) && tabuleiro.at(heroi.getPosicao()) == Terreno.SAIDA && nenhumDragao(dragoes))
			acabou = true;
		
		moverTodosOsDragoes();		
		apanharEquipamento();
	}

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
				if(heroi.getArmado()){
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
						if(!heroi.hasSword() && dragoes[i].isAcordado()){
							perdeu = true;
							acabou = true;
						}
						else if (heroi.hasSword()){
							dragoes[i] = null;
							System.out.println("Mataste um dragao!");
						}
							
						
						break;
					}				
				}
			}
		}
	}
	
	
	public boolean nenhumDragao(Dragao[] dragoes){
		for(int i = 0; i < dragoes.length; i++){
			if(dragoes[i] != null)
				return false;
		}
		return true;
	}
	
	

	private boolean movePeca(Direcao dir, Peca peca) {
		Posicao novaPosicao = peca.getPosicao().novaPosicao(dir);
		if (tabuleiro.validaPosicao(novaPosicao)){
			peca.setPosicao(novaPosicao);
			return true;
		}
		return false;
	}
	
	private boolean isNaMira(Peca agressor, Peca defensor, int alcance)
	{
		return isNaMira(agressor, defensor, alcance, Direcao.values());
	}
	
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
			else if (defensor.getPosicao().equals(posAtual))
				return true;				
		}				
		return false;
	}
	
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
	
}
