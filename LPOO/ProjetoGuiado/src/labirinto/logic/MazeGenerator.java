package labirinto.logic;
import java.util.Random;
import java.util.Stack;

/**
 * Class responsible for generating the random mazes used within the game.
 *
 */
public final class MazeGenerator {

	
	/** 
	 * Generates a random maze with its sides measuring whatever the side variable represents. The generated maze will always be a square.
	 * @param side size of the walls of the maze.
	 * @return A randomly generated maze.
	 */
	public static Terreno[][] generate(int side){
			 
		Terreno lab[][] = new Terreno[side][side];
		int vis = (side-1)/2;
		char visited[][] = new char[vis][vis];
		int currPos[] = new int[2];
		Stack<Integer> cellHist = new Stack<Integer>();
		
		for(int i = 0; i < side; i++){ //Filling the labyrinth with walls
			for(int j = 0; j < side; j++){
				lab[i][j] = Terreno.PAREDE;
			}
		}
		
		for(int i = 0; i < vis; i++){ //Filling the visited matrix
			for(int j = 0; j < vis; j++){
				visited[i][j] = 'U';
			}
		}
		
		
		Random rand = new Random(); 
		
		for(int i = 0; i < side; i++){ //Poking holes in the maze
			for(int j = 0; j < side; j++){
				if((i % 2 != 0) && (j % 2 != 0) && i != side-1 && j != side-1){
					lab[i][j] = Terreno.CHAO;
				}
			}
		}
		
		int guidedStartX = rand.nextInt(side-1); //randomizing placement for guided cell
		while((guidedStartX % 2) == 0){
			guidedStartX = rand.nextInt(side-1);
		}
		
		int guidedStartY = rand.nextInt(side-1);
		while((guidedStartY % 2) == 0){
			guidedStartY = rand.nextInt(side-1);
		}
		
		

		visited[(guidedStartX - 1 )/2][(guidedStartY - 1)/2] = '+';
		currPos[0] = (guidedStartX - 1 )/2;
		currPos[1] = (guidedStartY - 1)/2;
		pushCoord(cellHist, (guidedStartX - 1 )/2, (guidedStartY - 1)/2);
		
		while(!cellHist.isEmpty()){ //Generating maze
			int dir = rand.nextInt(4);

			if(dir == 0 && (currPos[0]-1 >= 0 && visited[currPos[0]-1][currPos[1]] != '+')){ //up 
					currPos[0]--;
					lab[2*currPos[0]+2][2*currPos[1]+1] = Terreno.CHAO;
			}
			else if(dir == 1 && (currPos[1]-1 >= 0 && visited[currPos[0]][currPos[1]-1] != '+')){ //left
					currPos[1]--;
					lab[2*currPos[0]+1][2*currPos[1]+2] = Terreno.CHAO;
			}
			else if(dir == 2 && (currPos[1]+1 < visited.length && visited[currPos[0]][currPos[1]+1] != '+')){ //right
					currPos[1]++;
					lab[2*currPos[0]+1][2*currPos[1]] = Terreno.CHAO;
			}
			else if(dir == 3 && (currPos[0]+1 < visited.length && visited[currPos[0]+1][currPos[1]] != '+')){ //down
					currPos[0]++;
					lab[2*currPos[0]][2*currPos[1]+1] = Terreno.CHAO;
					
			}
			else{
				if(currPos[0] - 1 < 0 || (currPos[0] - 1 >= 0 && visited[currPos[0]-1][currPos[1]] == '+')){
					if(currPos[0] + 1 >= visited.length || (currPos[0] + 1 < visited.length && visited[currPos[0]+1][currPos[1]] == '+')){
						if(currPos[1] - 1 < 0 || (currPos[1] - 1 >= 0 && visited[currPos[0]][currPos[1]-1] == '+')){
							if( currPos[1] + 1 >= visited.length || (currPos[1] + 1 < visited.length && visited[currPos[0]][currPos[1]+1] == '+')){
								currPos[0] = (int)cellHist.pop();
								currPos[1] = (int)cellHist.pop();
							}
						}
					}
				}
				continue;
			}
			pushCoord(cellHist, currPos[0], currPos[1]);
			visited[currPos[0]][currPos[1]] = '+';
		}
		
		int exitPlacement = rand.nextInt(101) + 1; //Placing exit on one of the walls
		int exitCoord = rand.nextInt(side-2) + 1;
		
		if((exitPlacement % 4) ==  0){
			if(lab[1][exitCoord] != Terreno.CHAO)
				lab[0][exitCoord+1] = Terreno.SAIDA;
			else
				lab[0][exitCoord] = Terreno.SAIDA;
		}
		else if((exitPlacement % 3) == 0){
			if(lab[exitCoord][1] != Terreno.CHAO)
				lab[exitCoord+1][0] = Terreno.SAIDA;
			else
				lab[exitCoord][0] = Terreno.SAIDA;
			
		}
		else if((exitPlacement % 2) == 0){
			if(lab[exitCoord][side-2] != Terreno.CHAO)
				lab[exitCoord+1][side-1] = Terreno.SAIDA;
			else
				lab[exitCoord][side-1] = Terreno.SAIDA;
		}
		else{
			if(lab[side-2][exitCoord] != Terreno.CHAO)
				lab[side-1][exitCoord+1] = Terreno.SAIDA;
			else
				lab[side-1][exitCoord] = Terreno.SAIDA;
		}
		
		return lab;
	}
		
	private static void pushCoord(Stack<Integer> st, int x, int y){
		st.push(y);
		st.push(x);
	}
	
	
	
	private MazeGenerator(){} 


	/**
	 * Gets the predefined maze provided in the project's first iteration.
	 * @return the aforementioned maze.
	 */
	public static Terreno[][] getPredef() {
		return  converteMapa(new char[][]
				{	{'X','X','X','X','X','X','X','X','X','X'},
				{'X',' ',' ',' ',' ',' ',' ',' ',' ','X'},
				{'X',' ','X','X',' ','X',' ','X',' ','X'},
				{'X',' ','X','X',' ','X',' ','X',' ','X'},
				{'X',' ','X','X',' ','X',' ','X',' ','X'},
				{'X',' ',' ',' ',' ',' ',' ','X',' ','S'},
				{'X',' ','X','X',' ','X',' ','X',' ','X'},
				{'X',' ','X','X',' ','X',' ','X',' ','X'},
				{'X',' ','X','X',' ',' ',' ',' ',' ','X'},
				{'X','X','X','X','X','X','X','X','X','X'},
			}, 10);		
	}
	
	/**
	 * Returns the size of the predefined maze provided on the project's first iteration.
	 * @return the size of the maze's walls (10).
	 */
	public static int getPredefSize()
	{
		return 10;	
	}
	
	/**
	 * Converts the predefined maze (implemented with an array of chars) into a standard maze implemented with the Terreno enum.
	 * @param mapa the char array to convert
	 * @param tamanho size of the maze
	 * @return Returns the converted maze.
	 * @see Terreno
	 */
	private static Terreno[][] converteMapa(char[][] mapa, int tamanho)
	{
		Terreno[][] novoMapa = new Terreno[tamanho][tamanho];
		for (int y = 0; y < tamanho; y++)
			for(int x = 0; x < tamanho; x++){
				switch(mapa[x][y]){
				case 'X':
					novoMapa[x][y] = Terreno.PAREDE;
					break;
				case ' ' :
					novoMapa[x][y] = Terreno.CHAO;
					break;
				case 'S' : 
					novoMapa[x][y] = Terreno.SAIDA;
					break;
				default: 
					break;
				}
			}
				
		
		return novoMapa;
	}
			
}
	
	

