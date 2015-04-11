package labirinto.cli;


import java.io.IOException;
import java.util.Scanner;

import labirinto.logic.*;

/**
 * Class responsible for the command line interface of the game.
 *
 */

public class Interface {
	
	/**
	 * Enum responsible for translating user input
	 */
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
	 * This function represents most of the command line interface.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {	
		
		int dimensao = 0;
		int dragoes = -1;
		int estrategia = 0;
		int option = 0;
		@SuppressWarnings("resource")
		Scanner leitor = new Scanner(System.in);
				
		
		printTitle();
		System.out.print("\n\nPress ENTER to continue");
		leitor.nextLine();
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
		
		while(option != 1){
		
			System.out.print("1. Come�ar jogo\n2. Tutorial\n3. Sair\n");
			do{
				try{
					System.out.print("Insira uma op��o: ");
					option = leitor.nextInt();
				}
				catch(Exception e){
					System.out.print("Entrada n�o v�lida. Insira um n�mero.");
				}
			}while(option < 1 || option > 3);
			
			if(option == 3)
				return;
			else if(option == 2){
				tutorial();
			}
		}
		
		
		do{
			try{
				System.out.println("\n\n\nInsira dimensão pretendida para o labirinto (deve ser um numero impar), ou -1 para o labirinto padrao:");
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
		
		System.out.println("Este jogo tem � sua disposi��o v�rios modos relativos ao comportamento dos dragoes. S�o eles: \n"
				+ "1. Drag�o sempre acordado\n2. Drag�o sempre parado\n3. Drag�o adormece ocasionalmente\n");
		
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
	//	InputType tipo;
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
				break;
			}
			
			if (InputType.getType(input) == InputType.INSIGNIFICANTE)
				printTabuleiro(lab, dimensao);			
			
		}while(!lab.isAcabou());
		
		printTabuleiro(lab, dimensao);
		if (!lab.isPerdeu())
			System.out.println("Venceu! :)");
		else
			System.out.println("Perdeu! :(");
		
		leitor.close();
	}

	/**
	 * Prints the labyrinth on the console
	 * @param lab The labyrinth to print
	 * @param dimensao Size of the labyrinth walls (labyrinth is a square).
	 */
	public static void printTabuleiro(Labirinto lab, int dimensao) {
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
	
	/**
	 * Reads input from the keyboard
	 * @return The input that was read.
	 * @throws IOException
	 */
	private static char getInput() throws IOException
	{
		char tmp = (char) System.in.read();
		return tmp;
	}
	
	/**
	 * Reads input from the keyboard continuously until the type of input matches the one specified in tipo.
	 * @param tipo Type of input desired
	 * @return The input that was read
	 * @throws IOException
	 */
	private static char getInput(InputType tipo) throws IOException
	{
		char tmp = getInput();
		if (InputType.getType(tmp) == tipo)
			return tmp;
		else
		return	getInput(tipo);
	}
	
	/**
	 * Translates input previously determined to be of the directional type into one direction.
	 * @param input The input to translate
	 * @return An instance of the Direcao enum corresponding to the direction that was provided.
	 */
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
	
	/**
	 * Prints the title of the game to the console.
	 */
	private static void printTitle(){
		System.out.print("_________          _______    _______  _______  _______  _______ \n");
		System.out.print("\\__   __/|\\     /|(  ____ \\  (       )(  ___  )/ ___   )(  ____ \\\n");
		System.out.print("   ) (   | )   ( || (    \\/  | () () || (   ) |\\/   )  || (    \\/\n");
		System.out.print("   | |   | (___) || (__      | || || || (___) |    /   )| (__    \n");
		System.out.print("   | |   |  ___  ||  __)     | |(_)| ||  ___  |   /   / |  __)   \n");
		System.out.print("   | |   | (   ) || (        | |   | || (   ) |  /   /  | (      \n");
		System.out.print("   | |   | )   ( || (____/\\  | )   ( || )   ( | /   (_/\\| (____/\\\n");
		System.out.print("   )_(   |/     \\|(_______/  |/     \\||/     \\|(_______/(_______/\n");
	}
	
	/**
	 * Prints the game's tutorial into the console.
	 */
	private static void tutorial(){
		System.out.print("Comandos: \nW - Cima\nA - Esquerda\nS - Baixo\nD - Direita\nT - Atirar dardo (deve ser seguido de uma das direc��es acima)\n\n"
				+"O objectivo do her�i (H) � chegar � sa�da do labirinto (S) ap�s ter matado todos os drag�es (D).\n" + 
				"Para matar drag�es, o her�i necessita de uma espada (E) ou dardo (J) e do escudo (P), que podem ser encontrados espalhados pelo labirinto.\n"+
				"O he�i s� pode ter um dardo ou uma espada em qualquer momento." + "Se o her�i chegar perto de um drag�o sem o escudo, ser� instantaneamente esturricado.\n\n\n");
	}
	
}
