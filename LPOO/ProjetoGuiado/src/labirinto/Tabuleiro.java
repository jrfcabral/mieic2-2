package labirinto;

import java.util.Arrays;
import java.util.Random;

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
		heroi = new Peca(new Posicao(1,1), HEROI);
		espada = new Peca(new Posicao(8,1), ESPADA);
		dragao = new Peca(new Posicao(2,4), DRAGAO);
	}
	
	public char getCellSymbol(int x, int y){
		
		return getCellSymbol(new Posicao(x,y));
	}
	
	public char getCellSymbol(Posicao posicao)
	{
		if (posicao.equals(this.heroi.getPosicao())) return heroi.getType();
		else if (posicao.equals(this.espada.getPosicao()) && posicao.equals(this.dragao.getPosicao()))
			return espada.getType();
		else if (posicao.equals(this.espada.getPosicao())) return espada.getType();
		else if (posicao.equals(this.dragao.getPosicao())) return dragao.getType();
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
		if (this.mapa[posicao.getX()][posicao.getY()] == PAREDE)
			return false;
		else if (posicao.getX() > 9 || posicao.getX() < 0 || posicao.getY() > 9 || posicao.getY() < 0)
			return false;
		return true;
	}
	
}
