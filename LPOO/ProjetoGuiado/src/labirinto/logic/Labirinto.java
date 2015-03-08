package labirinto.logic;

import java.util.Random;

public class Labirinto {
	private static final char ESPACO = ' ';

	private static final char HEROI_ARMADO = 'A';

	private static final char PAREDE = 'X';

	private static final char ESPADA = 'E';

	private static final char HEROI = 'H';
	
	private static final char DRAGAO = 'D';
	
	private static final char DARDO = 'J'; //Javelin, a falta d melhor nome

	private static final char ESCUDO = 'P'; //Protecçao, a falta d melhor nome
	//private char[][] mapa;
	Tabuleiro tabuleiro;
	
	private Heroi heroi;
	private Espada espada;
	private Dardo[] dardos;
	private Escudo escudo;
	
	private Dragao[] dragoes;
	
	private boolean acabou;
	private boolean perdeu;
	
	private int dimensao;
	private int numeroDragoes;
	
	public int getDimensao() {
		return dimensao;
	}

	public boolean isPerdeu() {
		return perdeu;
	}

	public boolean isAcabou() {
		return acabou;
	}

	
	/*public Labirinto(Terreno[][] formatoTabuleiro, int dimensao)
	{
		this.tabuleiro = new Tabuleiro(formatoTabuleiro, dimensao);		
		this.dimensao = dimensao;
		inicializarPecas();
	}
	*/
	public Labirinto(Terreno[][] formatoTabuleiro, int dimensao, int dragoes)
	{
		Random rand = new Random();
		this.tabuleiro = new Tabuleiro(formatoTabuleiro, dimensao);		
		this.dimensao = dimensao;
		this.dragoes = new Dragao[dragoes];
		this.dardos = new Dardo[rand.nextInt(5) + 1];
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
				else if(dardos[i] != null && dragoes[j] != null && posicao.equals(this.dardos[i].getPosicao())){
					return 'J';
				}
			}
		}
		
		
		
		for (Dragao dragao:dragoes){
			if (dragao != null && dragao.getPosicao().equals(posicao)) return 'D';
		}
		
		
		return tabuleiro.at(posicao).toString().charAt(0);
	}
	
	
	
	
	
	//wasd:mover heroi
	public void move(Direcao dir)
	{
		movePeca(dir, heroi);
		
		if (heroi.isArmado() && tabuleiro.at(heroi.getPosicao()) == Terreno.SAIDA && isNoDurgons(dragoes))
			acabou = true;
		
		
		for (int i = 0; i < dragoes.length; i++){
			
			if (dragoes[i] != null){
				movePeca(Direcao.randomDirecao(), dragoes[i]);
				
				Posicao[] adjacentes = dragoes[i].getPosicao().getAdjacencias();
				
				for (Posicao adjacente: adjacentes){
					if (dragoes[i].getPosicao().equals(heroi.getPosicao()) || adjacente.equals(heroi.getPosicao())){						
						if(!heroi.isArmado()){
							perdeu = true;
							acabou = true;
						}
						else{
							dragoes[i] = null;
							System.out.println("Mataste um dragao!");
						}
							
						
						break;
					}				
				}
			}
		}
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
	
	
	public boolean isNoDurgons(Dragao[] durgons){
		for(int i = 0; i < durgons.length; i++){
			if(durgons[i] != null)
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
	
	
	
}
