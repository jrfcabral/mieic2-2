package labirinto.cli;


import java.io.IOException;
import java.util.Scanner;

import labirinto.logic.*;



public class Interface {

	private enum InputType {
		DIRECIONAL, ATIRA_DARDO, INSIGNIFICANTE, DESCONHECIDA;
		
		public static InputType getType(char c)
		{
			switch (c){
			case 't': return ATIRA_DARDO;
			case 'w': case 'd': case 'a': case 's': return DIRECIONAL; 
			case '\n': return INSIGNIFICANTE;
			default : return DESCONHECIDA;			
			}
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {	
		
		int dimensao = 0;
		int dragoes = -1;
		int estrategia = 0;
		Scanner leitor = new Scanner(System.in);
				
		
		
		do{
			try{
				System.out.println("Insira dimensão pretendida (deve ser um numero impar), ou -1 para o lab padrao:");
				dimensao = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Entrada não vãlida. Insira um número! :\\");
			}
		}while(dimensao < -1 || (dimensao % 2) == 0);
		
		
		do{
			try{
				System.out.println("Insira numero de dragães que quer defrontar (pelo menos 1):");
				dragoes = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Entrada não válida. Insira um número! :\\");
			}
		}while(dragoes < 1);
		
		
		do{
			try{
				System.out.println("Insira a estratégia a usar:");
				estrategia = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Entrada não válida. Insira um número! :\\");
			}
		}while(estrategia < 1 || estrategia > 3);
		
		
		Labirinto lab;
		if (dimensao != -1)
			lab = new Labirinto(MazeGenerator.generate(dimensao), dimensao, dragoes, Estrategia.fromNumber(estrategia));			
		else{
			lab = new Labirinto(MazeGenerator.getPredef(), MazeGenerator.getPredefSize(), dragoes, Estrategia.fromNumber(estrategia));
			dimensao = MazeGenerator.getPredefSize();
		}
		char input = 0;
		InputType tipo;
		printTabuleiro(lab, lab.getDimensao());
		do
		{
			
			input = getInput();
			
			switch (InputType.getType(input)){
			case DIRECIONAL:
				lab.move(traduzDirecao(input));
				break;
			case ATIRA_DARDO:
				input = getInput(InputType.DIRECIONAL);
				lab.atiraDardo(traduzDirecao(input));				
				break;
			case INSIGNIFICANTE:
				break;
			default:
				System.out.println("Entrada não reconhecida ou inutil...");
				break;
			}
			
			if (InputType.getType(input) != InputType.INSIGNIFICANTE)
				printTabuleiro(lab, dimensao);			
			
		}while(!lab.isAcabou());
		
		printTabuleiro(lab, dimensao);
		if (!lab.isPerdeu())
			System.out.println("Venceu! :)");
		else
			System.out.println("Perdeu! :(");
		
		leitor.close();
	}

	private static void printTabuleiro(Labirinto lab, int dimensao) {
		for (int x=0; x <dimensao;x++)
		{
			for (int y=0;y < dimensao; y++){
				System.out.print(lab.getCellSymbol(x, y));
				System.out.print(' ');
				//System.out.print(' ');
			}
			System.out.print('\n');		
		}
	}
	
	private static char getInput() throws IOException
	{
		char tmp = (char) System.in.read();
		return tmp;
	}
	private static char getInput(InputType tipo) throws IOException
	{
		char tmp = getInput();
		if (InputType.getType(tmp) == tipo)
			return tmp;
		else
		return	getInput(tipo);
	}
	
	
	private static Direcao traduzDirecao(char input)
	{
		switch (input)
		{
		case 'w': return Direcao.CIMA;
		case 'a': return Direcao.ESQUERDA;
		case 's': return Direcao.BAIXO;
		case 'd': return Direcao.DIREITA;
		default : return Direcao.NONE;
		}
	}
}
