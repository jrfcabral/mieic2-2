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
		this.tabuleiro = new Tabuleiro(formatoTabuleiro, dimensao);		
		this.dimensao = dimensao;
		this.dragoes = new Dragao[dragoes];
		inicializarPecas();
		
	}
	
	
	
	

	private void inicializarPecas() {		
		
		heroi = new Heroi(tabuleiro.getFreeCell());		
		espada = new Espada(tabuleiro.getFreeCell());
		
		for (int i = dragoes.length-1; i >= 0; i--){
			dragoes[i] = new Dragao(tabuleiro.getFreeCell());
		}
			
		
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
	
		else if (espada != null && posicao.equals(this.espada.getPosicao())) return 'E';
		
		for (Dragao dragao:dragoes){
			if (dragao != null && dragao.getPosicao().equals(posicao)) return 'D';
		}
		
		return tabuleiro.at(posicao).toString().charAt(0);
	}
	
	
	
	
	
	//wasd:mover heroi
	public void move(Direcao dir)
	{
		movePeca(dir, heroi);
		
		if (heroi.isArmado() && tabuleiro.at(heroi.getPosicao()) == Terreno.SAIDA)
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
							System.out.println("Mataste o dragao!");
						}
							
						
						break;
					}				
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
