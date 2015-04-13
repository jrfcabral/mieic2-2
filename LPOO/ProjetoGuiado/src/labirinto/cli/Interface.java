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
		
			System.out.print("1. New Game\n2. Tutorial\n3. Exit\n");
			do{
				try{
					System.out.print("Type in an option: ");
					option = leitor.nextInt();
				}
				catch(Exception e){
					System.out.print("Invalid input. Type in a number: ");
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
				System.out.println("\n\n\nType in the dimension for the maze (must be an odd number) or type in -1 for the predefined maze:");
				dimensao = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Invalid input. Please type in a number :\\");
			}
		}while(dimensao < -1 || (dimensao % 2) == 0);
		
		
		do{
			try{
				System.out.println("Type in the number of dragons you want to face (at least 1):");
				dragoes = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Invalid input. Please type in a number :\\");
			}
		}while(dragoes < 1);
		
		System.out.println("This games provides several behaviors for the dragons. These are:"
				+ "1. Dragon stands still\n2. Dragon is permanently awake\n3. Dragon is mobile and can fall asleep\n");
		
		do{
			try{
				System.out.println("Pick a behaviour:");
				estrategia = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Invalid input. Please type in a number :\\");
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
			System.out.println("You won :)");
		else
			System.out.println("You lost! :(");
		
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
	 * @see Direcao
	 * @see getInput()
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
	 * Prints the game's tutorial to the console.
	 */
	private static void tutorial(){
		System.out.print("Keys: \nW - Up\nA - Left\nS - Down\nD - Right\nT - Throw javelin (must be followed by a directional key)\n\n"
				+"The hero's (H) goal is to reach the maze's exit (S) after having slain all the dragons (D).\n" + 
				"In order to slay the dragons needs either a sword (E) or a javelin (J) and (optionally) the shield (P), which can be found scattered throughout the maze.\n"+
				"The hero can only have either a sword or a javelin at every moment. If you attempt to pick up a sword while you have a javelin, you will drop the javelin (and vice-versa)." + "If the hero gets close to a dragon "
						+ "without the shield, he will be immediately incinerated.\n\n\n");
	}
	
}
