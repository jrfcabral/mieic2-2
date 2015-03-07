package labirinto.logic;

import java.util.Random;


public class Labirinto {
	private static final char ESPACO = ' ';

	private static final char HEROI_ARMADO = 'A';

	private static final char PAREDE = 'X';

	private static final char ESPADA = 'E';

	private static final char HEROI = 'H';
	
	private static final char DRAGAO = 'D';

	//private char[][] mapa;
	Tabuleiro tabuleiro;
	
	private Heroi heroi;
	private Espada espada;
	private Dragao dragao;
	
	private boolean acabou;
	private boolean perdeu;
	
	private int dimensao;
	
	public int getDimensao() {
		return dimensao;
	}

	public boolean isPerdeu() {
		return perdeu;
	}

	public boolean isAcabou() {
		return acabou;
	}

	
	public Labirinto(Terreno[][] formatoTabuleiro, int dimensao)
	{
		this.tabuleiro = new Tabuleiro(formatoTabuleiro, dimensao);		
		this.dimensao = dimensao;
		inicializarPecas();
	}
	

	private void inicializarPecas() {		
		
		heroi = new Heroi(tabuleiro.getFreeCell());		
		espada = new Espada(tabuleiro.getFreeCell());
		dragao = new Dragao(tabuleiro.getFreeCell());
	}

	
	
	public char getCellSymbol(int x, int y){
		
		return getCellSymbol(new Posicao(x,y));
	}
	
	public char getCellSymbol(Posicao posicao)
	{		
		if (heroi != null && posicao.equals(this.heroi.getPosicao())){
			if (heroi.isArmado())
				return 'A';
			return 'H';
		}
		else if (espada != null && dragao != null &&posicao.equals(this.espada.getPosicao()) && posicao.equals(this.dragao.getPosicao()))
			return 'F';
		else if (espada != null && posicao.equals(this.espada.getPosicao())) return 'E';
		else if (dragao != null && posicao.equals(this.dragao.getPosicao())) return 'D';
		else return tabuleiro.at(posicao).toString().charAt(0);
	}
	
	//wasd:mover heroi
	public void move(Direcao dir)
	{
		movePeca(dir, heroi);
		
		if (heroi.isArmado() && tabuleiro.at(heroi.getPosicao()) == Terreno.SAIDA)
			acabou = true;
		
		if (dragao != null){
			movePeca(Direcao.randomDirecao(), dragao);
			
			Posicao[] adjacentes = dragao.getPosicao().getAdjacencias();
			
			for (Posicao adjacente: adjacentes)
			{
				if (adjacente.equals(heroi.getPosicao())){
					
					if(!heroi.isArmado()){
						perdeu = true;
						acabou = true;
					}
					else
						dragao = null;
					
					
					break;
				}				
			}
		}
		
		if (espada != null && heroi.getPosicao().equals(espada.getPosicao())){
			heroi.setArmado(true);
			espada = null;
		}
		
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
