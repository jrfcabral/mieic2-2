package labirinto;

import java.io.IOException;
import java.util.Scanner;

public class Interface {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {	
		
		int dimensao;
		
		
		Scanner leitor = new Scanner(System.in);
		System.out.println("Insira dimensão pretendida (deve ser um numero impar):");
		dimensao = leitor.nextInt();
		
		
		
		Tabuleiro tabuleiro = new Tabuleiro(MazeGenerator.generate(dimensao), dimensao);
		char input = 0;
		do
		{
			if (input != '\n')
				printTabuleiro(tabuleiro, dimensao);
			input = Interface.getInput();
			tabuleiro.handleInput(input);
			
		}while(!tabuleiro.isDone());
		
		printTabuleiro(tabuleiro, dimensao);
		if (!tabuleiro.isPerdeu())
			System.out.println("Venceu!");
		else
			System.out.println("Perdeu! :(");
		
		leitor.close();
	}

	private static void printTabuleiro(Tabuleiro tabuleiro, int dimensao) {
		for (int x=0; x <dimensao;x++)
		{
			for (int y=0;y < dimensao; y++)
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
