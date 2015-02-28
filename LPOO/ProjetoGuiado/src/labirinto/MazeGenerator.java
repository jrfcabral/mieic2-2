package labirinto;
import java.util.Random;
import java.util.Stack;


final class MazeGenerator {
	
	public static void printRandomMaze(String[] args){
		char test[][] = generate(31);
		
		for(int i = 0; i < 31; i++){
			for(int j = 0; j < 31; j++){
				System.out.print(test[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	
	
	public static char[][] generate(int side){
			 
		char lab[][] = new char[side][side];
		int vis = (side-1)/2;
		char visited[][] = new char[vis][vis];
		int currPos[] = new int[2];
		Stack<Integer> cellHist = new Stack<Integer>();
		
		for(int i = 0; i < side; i++){ //Filling the labyrinth with walls
			for(int j = 0; j < side; j++){
				lab[i][j] = 'x';
			}
		}
		
		for(int i = 0; i < vis; i++){ //Filling the visited matrix
			for(int j = 0; j < vis; j++){
				visited[i][j] = 'U';
			}
		}
		
		
		Random rand = new Random(); 
		int exitPlacement = rand.nextInt(101) + 1; //Placing exit on one of the walls
		int exitCoord = rand.nextInt(side-2) + 1;
		
		if((exitPlacement % 4) ==  0){
			lab[0][exitCoord] = 'S';
		}
		else if((exitPlacement % 3) == 0){
			lab[exitCoord][0] = 'S';
		}
		else if((exitPlacement % 2) == 0){
			lab[exitCoord][side-1] = 'S';
		}
		else{
			lab[side-1][exitCoord] = 'S';
		}
		
		for(int i = 0; i < side; i++){ //Poking holes in the maze
			for(int j = 0; j < side; j++){
				if((i % 2 != 0) && (j % 2 != 0) && i != side-1 && j != side-1){
					lab[i][j] = ' ';
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
					lab[2*currPos[0]+2][2*currPos[1]+1] = ' ';
			}
			else if(dir == 1 && (currPos[1]-1 >= 0 && visited[currPos[0]][currPos[1]-1] != '+')){ //left
					currPos[1]--;
					lab[2*currPos[0]+1][2*currPos[1]+2] = ' ';
			}
			else if(dir == 2 && (currPos[1]+1 < visited.length && visited[currPos[0]][currPos[1]+1] != '+')){ //right
					currPos[1]++;
					lab[2*currPos[0]+1][2*currPos[1]] = ' ';
			}
			else if(dir == 3 && (currPos[0]+1 < visited.length && visited[currPos[0]+1][currPos[1]] != '+')){ //down
					currPos[0]++;
					lab[2*currPos[0]][2*currPos[1]+1] = ' ';
					
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
		
		return lab;
		/*for(int i = 0; i < side; i++){
			for(int j = 0; j < side; j++){
				System.out.print(lab[i][j]);
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\n");

		for(int i = 0; i < vis; i++){
			for(int j = 0; j < vis; j++){
				System.out.print(visited[i][j]);
			}
			System.out.print("\n");
		}
		System.out.print(cellHist);
		*/
	}
		
	private static void pushCoord(Stack<Integer> st, int x, int y){
		st.push(y);
		st.push(x);
	}
	
	
	
	private MazeGenerator(){} //nao deve ser instanciada
			
}
	
	

