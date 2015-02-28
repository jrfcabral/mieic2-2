package labirinto;

import java.util.Random;
import java.util.Stack;

public class Tabuleiro {
	private static final char ESPACO = ' ';

	private static final char HEROI_ARMADO = 'a';

	private static final char PAREDE = 'x';

	private static final char ESPADA = 'e';

	private static final char HEROI = 'h';
	
	private static final char DRAGAO = 'd';

	private char[][] mapa;
	
	private Peca heroi;
	private Peca espada;
	private Peca dragao;
	
	private boolean done;
	private boolean perdeu;
	
	private int dimensao;
	
	public boolean isPerdeu() {
		return perdeu;
	}

	public boolean isDone() {
		return done;
	}

	
	public Tabuleiro(char[][] formatoTabuleiro, int dimensao)
	{
		this.mapa = formatoTabuleiro;		
		this.dimensao = dimensao;
		inicializarPecas();
	}
	

	private void inicializarPecas() {		
		
		heroi = new Peca(getFreeCell(), HEROI);		
		espada = new Peca(getFreeCell(), ESPADA);
		dragao = new Peca(getFreeCell(), DRAGAO);
	}

	private Posicao getFreeCell() {
		Random r = new Random();
		int randx;
		int randy;
		do
		{
			randx = r.nextInt(dimensao);
			randy = r.nextInt(dimensao);			
		}while(getCellSymbol(randx, randy) != ' ');
		
		return new Posicao(randx, randy);
	}
	
	public char getCellSymbol(int x, int y){
		
		return getCellSymbol(new Posicao(x,y));
	}
	
	public char getCellSymbol(Posicao posicao)
	{
		if (heroi != null && posicao.equals(this.heroi.getPosicao())) return heroi.getType();
		else if (espada != null && dragao != null &&posicao.equals(this.espada.getPosicao()) && posicao.equals(this.dragao.getPosicao()))
			return 'f';
		else if (espada != null && posicao.equals(this.espada.getPosicao())) return espada.getType();
		else if (dragao != null && posicao.equals(this.dragao.getPosicao())) return dragao.getType();
		else return mapa[posicao.getX()][posicao.getY()];
	}
	
	//wasd:mover heroi
	public void handleInput(char input)
	{
		if (input == '\n')
			return;
	
		Posicao novaPosicao = this.heroi.getPosicao().novaPosicao(input);
		if (this.validPosition(novaPosicao))
			heroi.setPosicao(novaPosicao);
		
		if (heroi.getPosicao().equals(espada.getPosicao())){
			heroi.setType(HEROI_ARMADO);
			espada.setType(ESPACO);
		}
		
		if (heroi.getType() == HEROI_ARMADO && heroi.getPosicao().getX() == 5 && heroi.getPosicao().getY() == 9)
			done = true;
		
		//mover dragao
		Posicao novaDragao;		
		Random r = new Random();
		do
		{
			novaDragao = dragao.getPosicao().getAdjacencias()[r.nextInt(4)]; //escolher adjacencia aleatoria			
		}while(!validPosition(novaDragao)); 
		dragao.setPosicao(novaDragao);
		
		//verificar proximidade
		if ( (dragao.getPosicao().equals(heroi.getPosicao()) ||dragao.getPosicao().isAdjacente(heroi.getPosicao())) && dragao.getType() == DRAGAO)
		{
			if (heroi.getType() == HEROI_ARMADO)
				dragao.setType(ESPACO);
			
			else{
				this.done = true;
				this.perdeu= true;
				heroi.setType(ESPACO);
			}
				
		}
			
	}
	
	private boolean validPosition(Posicao posicao)
	{
		
		if (posicao.getX() > dimensao-1 || posicao.getX() < 0 || posicao.getY() > dimensao -1 || posicao.getY() < 0)
			return false;
		if (this.mapa[posicao.getX()][posicao.getY()] == PAREDE)
			return false;
		return true;
	}
	
}
