package labirinto;

import java.util.Arrays;

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
	
	public boolean isPerdeu() {
		return perdeu;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Tabuleiro(){
		this.mapa = new char[][]
				{	{'x','x','x','x','x','x','x','x','x','x'},
				{'x',' ',' ',' ',' ',' ',' ',' ',' ','x'},
				{'x',' ','x','x',' ','x',' ','x',' ','x'},
				{'x',' ','x','x',' ','x',' ','x',' ','x'},
				{'x',' ','x','x',' ','x',' ','x',' ','x'},
				{'x',' ',' ',' ',' ',' ',' ','x',' ','s'},
				{'x',' ','x','x',' ','x',' ','x',' ','x'},
				{'x',' ','x','x',' ','x',' ','x',' ','x'},
				{'x',' ','x','x',' ',' ',' ',' ',' ','x'},
				{'x','x','x','x','x','x','x','x','x','x'},
			};
		heroi = new Peca(new Posicao(1,1), 'h');
		espada = new Peca(new Posicao(8,1), 'e');
		dragao = new Peca(new Posicao(2,4), DRAGAO);
	}
	
	public char getCellSymbol(int x, int y){
		
		return getCellSymbol(new Posicao(x,y));
	}
	
	public char getCellSymbol(Posicao posicao)
	{
		if (posicao.equals(this.heroi.getPosicao())) return heroi.getType();
		else if (posicao.equals(this.espada.getPosicao())) return espada.getType();
		else if (posicao.equals(this.dragao.getPosicao())) return dragao.getType();
		else return mapa[posicao.getX()][posicao.getY()];
	}
	
	//wasd:mover heroi
	public void handleInput(char input)
	{
		Posicao novaPosicao = this.heroi.getPosicao().novaPosicao(input);
		if (this.validPosition(novaPosicao))
			heroi.setPosicao(novaPosicao);
		
		if (heroi.getPosicao().equals(espada.getPosicao())){
			heroi.setType(HEROI_ARMADO);
			espada.setType(ESPACO);
		}
		
		if (heroi.getType() == HEROI_ARMADO && heroi.getPosicao().getX() == 5 && heroi.getPosicao().getY() == 9)
			done = true;
		
		Posicao[] adjacenciasDragao = dragao.getPosicao().getAdjacencias();
		

			
	}
	
	private boolean validPosition(Posicao posicao)
	{
		if (this.mapa[posicao.getX()][posicao.getY()] == PAREDE)
			return false;
		return true;
	}
	
}
