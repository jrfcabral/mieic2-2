
public class Jogo {

	public static void main(String[] args) {
		char[][] labirinto = new char[][]
				{	{'x','x','x','x','x','x','x','x','x','x'},
					{'x','h',' ',' ',' ',' ',' ',' ',' ','x'},
					{'x',' ','x','x','x','x',' ','x',' ','x'},
					{'x',' ','x','x','x','x',' ','x',' ','x'},
					{'x',' ','x','x','x','x',' ','x',' ','x'},
					{'x',' ',' ',' ',' ',' ',' ','x',' ','s'},
					{'x',' ','x','x',' ','x',' ','x',' ','x'},
					{'x',' ','x','x',' ','x',' ','x',' ','x'},
					{'x',' ','x','x',' ',' ',' ',' ',' ','x'},
					{'x','x','x','x','x','x','x','x','x','x'},
				};
		for (int i = 0; i < 10; i++)
		{
			for (int j=0;j<10; j++)
				System.out.print(labirinto[i][j]);
			System.out.print('\n');
		}
	}

}
