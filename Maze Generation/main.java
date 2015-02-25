import java.io.IOException;
import java.util.Random;
import java.util.Stack;


public class main {
	
	public static void main(String[] args) throws IOException{
			 
		int side = 7;
		char lab[][] = new char[side][side];
		int vis = (side-1)/2;
		char visited[][] = new char[vis][vis];
		Stack cellHist = new Stack();
		
		for(int i = 0; i < side; i++){ //Filling the labyrinth with walls
			for(int j = 0; j < side; j++){
				lab[i][j] = 'X';
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
		
		
		lab[guidedStartX][guidedStartY] = '+';
		visited[(guidedStartX - 1 )/2][(guidedStartY - 1)/2] = '+';
		
		pushCoord(cellHist, (guidedStartX - 1 )/2, (guidedStartY - 1)/2);
		
		while(!cellHist.isEmpty()){
			/*aqui o ciclo d exploracao*/
			System.out.print("Under Construction");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		for(int i = 0; i < side; i++){
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
	}
		
	public static void pushCoord(Stack st, int x, int y){
		st.push(y);
		st.push(x);
	}
	
	public static void popCoord(Stack st){
		st.pop();
		st.pop();
	}
			
}
	
	

