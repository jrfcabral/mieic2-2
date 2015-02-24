package labirinto;

import java.io.IOException;

public class Interface {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {	
		Tabuleiro tabuleiro = new Tabuleiro();
		char input = 0;
		do
		{
			if (input != '\n')
				printTabuleiro(tabuleiro);
			input = Interface.getInput();
			tabuleiro.handleInput(input);
			
		}while(!tabuleiro.isDone());
		System.out.println("Venceu!");
	}

	private static void printTabuleiro(Tabuleiro tabuleiro) {
		for (int x=0; x <10;x++)
		{
			for (int y=0;y < 10; y++)
				System.out.print(tabuleiro.getCellSymbol(x, y));
			System.out.print('\n');		
		}
	}
	
	private static char getInput() throws IOException
	{
		char tmp = (char) System.in.read();
		return tmp;
	}

}
