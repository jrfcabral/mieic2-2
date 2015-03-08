package labirinto.cli;


import java.io.IOException;
import java.util.Scanner;

import labirinto.logic.*;



public class Interface {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {	
		
		int dimensao = 0;
		int dragoes = -1;
		Scanner leitor = new Scanner(System.in);
		
		
		do{
			try{
				System.out.println("Insira dimensão pretendida (deve ser um numero impar), ou -1 para o lab padrao:");
				dimensao = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Entrada não válida. Insira um número! :\\");
			}
		}while(dimensao < -1 || (dimensao % 2) == 0);
		
		
		do{
			try{
				System.out.println("Insira numero de dragoes que quer defrontar (pelo menos 1):");
				dragoes = leitor.nextInt();
			}
			catch (Exception e){
				leitor.nextLine();
				System.out.println("Entrada não válida. Insira um número! :\\");
			}
		}while(dragoes < 1);
		
		
		Labirinto lab;
		if (dimensao != -1)
			lab = new Labirinto(MazeGenerator.generate(dimensao), dimensao, dragoes);			
		else
			lab = new Labirinto(MazeGenerator.getPredef(), MazeGenerator.getPredefSize(), dragoes);		
		
		char input = 0;
		do
		{
			Direcao direcao;
			do{			
				if (input != '\n')
					printTabuleiro(lab, lab.getDimensao());
				input = Interface.getInput();
				direcao = Interface.traduzInput(input);
			}while(direcao == Direcao.NONE);
			
			lab.move(direcao);			
			
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
	
	private static Direcao traduzInput(char input)
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
