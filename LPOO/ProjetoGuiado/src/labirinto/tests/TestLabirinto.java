package labirinto.tests;

import static org.junit.Assert.*;
import labirinto.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import labirinto.logic.*;

import org.junit.Test;

/**
 * Class responsible for all the project's tests.
 *
 */

public class TestLabirinto{
	
	private Labirinto testLab;

	/**
	 * Sets up a maze according to certain conditions in order to provide a proper testing environment.
	 * 
	 * @param heroPos
	 * 			Position where the hero will be in the maze
	 * @param swordPos
	 * 			Position where the sword will be in the maze
	 * @param dragonPos
	 * 			Position where the first dragon will be in the maze
	 * @param dragonnum
	 * 			Number of the dragons present in the maze (in most tests set to 1)
	 * @param shieldPos
	 * 			Position where the shield will be in the maze
	 * @param javPos
	 * 			Position where the javelin will be in the maze (only one is ever used in tests)
	 * 
	 * @return An object of the class Labirinto according to the provided specifications
	 * @see Labirinto
	 */
	public Labirinto mazeSetup(Posicao heroPos, Posicao swordPos, Posicao dragonPos, int dragonnum, Posicao shieldPos, Posicao javPos){
		return new Labirinto(MazeGenerator.getPredef(), MazeGenerator.getPredefSize(), dragonnum, Estrategia.PARADO, heroPos, swordPos, dragonPos, shieldPos, javPos);
	}
	
	/**
	 * Tests if the hero moves down correctly.
	 */
	@Test
	public void testMoveDown() {
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.BAIXO); 
		assertEquals(new Posicao(6, 4), testLab.getHeroi().getPosicao());
	}
	
	/**
	 * Tests if the hero moves up correctly.
	 */
	@Test
	public void testMoveUp(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.CIMA);
		assertEquals(new Posicao(4, 4), testLab.getHeroi().getPosicao());
	}
	
	/**
	 * Tests if the hero moves to the right correctly.
	 */
	@Test
	public void testMoveRight(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.DIREITA);
		assertEquals(new Posicao(5, 5), testLab.getHeroi().getPosicao());
	}
	
	/**
	 * Tests if the hero moves to the left correctly.
	 */
	@Test
	public void testMoveLeft(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.ESQUERDA);
		assertEquals(new Posicao(5, 3), testLab.getHeroi().getPosicao());
	}
	
	/**
	 * Tests the hero's behavior when moving against a wall in all 4 directions.
	 */
	@Test
	public void testMoveAgainstWall(){
		testLab = mazeSetup(new Posicao(8, 1), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		
		testLab.move(Direcao.BAIXO);
		assertEquals(new Posicao(8, 1), testLab.getHeroi().getPosicao());
		
		testLab.move(Direcao.ESQUERDA);
		assertEquals(new Posicao(8, 1), testLab.getHeroi().getPosicao());
		
		testLab.move(Direcao.BAIXO);
		assertEquals(new Posicao(8, 1), testLab.getHeroi().getPosicao());
		
		//moving to a position where it's not possible to move up
		testLab = mazeSetup(new Posicao(1, 1), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		
		testLab.move(Direcao.CIMA);
		assertEquals(new Posicao(1, 1), testLab.getHeroi().getPosicao());
	}
	
	/**
	 * Tests the behavior of both the sword and the hero when the hero moves into the position where the sword is.
	 */
	@Test
	public void testPickUpSword(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(6, 4), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.getHeroi().isArmado());
		assertEquals('H', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
		assertEquals('E', testLab.getCellSymbol(new Posicao(6, 4)));
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.getHeroi().isArmado());	
		assertEquals('A', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
		testLab.move(Direcao.CIMA);
		assertEquals(' ', testLab.getCellSymbol(new Posicao(6, 4)));
	}
	
	/**
	 * Tests the game's behavior when the hero moves into a position adjacent to the dragon's while not wielding the sword (and therefore losing the game).
	 */
	@Test
	public void testDefeat(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(7, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.isPerdeu());
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.isPerdeu());
	}
	
	/**
	 * Tests the game's behavior when the hero moves into a position adjacent to the dragon's while wielding sword and shield 
	 * (and therefore killing said dragon).
	 */
	@Test //Preciso ver oq se passa aqui (apesar de o teste passar bem)
	public void testSlayDragon(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(7, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.getHeroi().setArmado(true);
		testLab.getHeroi().setShielded(true);
		assertFalse(testLab.nenhumDragao());
		assertEquals('D', testLab.getCellSymbol(new Posicao(7, 4)));
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.nenhumDragao());
		assertEquals(' ', testLab.getCellSymbol(new Posicao(7, 4)));
	}
	
	/**
	 * Creates an environment in which the hero is able to gather all the conditions necessary to be able to win and then moves to the exit
	 * (therefore winning the game).
	 * Tests whether the game raises the appropriate flag.
	 */
	@Test //Fazer o percurso todo? (Apanhar espada, matar dragao, andar ate saida)
	public void testVictory(){
		testLab = mazeSetup(new Posicao(6, 8), new Posicao(0, 0), new Posicao(5, 8), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.isAcabou());
		testLab.getHeroi().setArmado(true);
		testLab.getHeroi().setShielded(true); 
		testLab.move(Direcao.CIMA);
		testLab.move(Direcao.DIREITA);
		assertTrue(testLab.isAcabou());
		
	}
	
	/**
	 * Creates an environment in which the hero reches the exit without having reunited all the conditions necessary for victory.
	 * Tests whether the game raises the appropriate flag upon arrival to the exit
	 */
	@Test
	public void testExitNoVictory(){
		testLab = mazeSetup(new Posicao(5, 8), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.isAcabou());
		testLab.move(Direcao.DIREITA);
		assertFalse(testLab.isAcabou());
	}
	
	/**
	 * Tests the dragon's sleeping mechanics
	 */
	@Test
	public void testSleepingDragon(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(5, 2), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertTrue(testLab.getDragoes()[0].isAcordado());
		testLab.getDragoes()[0].setAcordado(false);
		assertFalse(testLab.getDragoes()[0].isAcordado());
		assertEquals('d', testLab.getCellSymbol(5, 2));
		testLab.move(Direcao.ESQUERDA);
		assertFalse(testLab.isPerdeu());
		testLab.move(Direcao.ESQUERDA);
		assertFalse(testLab.isPerdeu());
	}
	
/*@Test
	public void testMultipleDragons(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(3, 4), 2);
		testLab.getDragoes()[1] = new Dragao(new Posicao(7, 4));
		
	}*/
	
	/**
	 * Tests the behavior of both the shield and the hero when the hero moves into the position where the sword is.
	 */
	@Test
	public void testPickUpShield(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(4, 4), new Posicao(0, 0));
		assertFalse(testLab.getHeroi().hasShield());
		testLab.move(Direcao.CIMA);
		assertTrue(testLab.getHeroi().hasShield());
		assertEquals('@', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
	}
	
	/**
	 * Tests the behavior of both the javelin and the hero when the hero moves into the position where the sword is.
	 */
	@Test
	public void testPickUpJavelin(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(4, 4));
		assertFalse(testLab.getHeroi().hasJavelin());
		testLab.move(Direcao.CIMA);
		assertTrue(testLab.getHeroi().hasJavelin());
		assertEquals('R', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
	}
	
	@Test
	public void testThrowJavelin(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(2, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.getHeroi().setHasJavelin(true);
		assertTrue(testLab.getHeroi().hasJavelin());
		testLab.atiraDardo(Direcao.BAIXO);
		assertFalse(testLab.getHeroi().hasJavelin());
		testLab.getHeroi().setHasJavelin(true);
		testLab.atiraDardo(Direcao.CIMA);
		assertTrue(testLab.nenhumDragao());		
	}
	
	/**
	 * Tests whether two randomly generated mazes of the same size are ever the same.
	 */
	@Test //kek
	public void testRandomMaze(){
		for(int i = 0; i < 10000; i++){
			testLab = new Labirinto(MazeGenerator.generate(11), 11, 1, Estrategia.PARADO);
			Labirinto testLab2 = new Labirinto(MazeGenerator.generate(11), 11, 1, Estrategia.ALTERNADO);
			assertNotEquals(testLab, testLab2);
		}	
	}
	
	/**
	 * Tests the game's behavior when an item and a dragon are in the same position
	 */
	@Test
	public void testOverlapPositions(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(3, 4), new Posicao(3, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertEquals('F', testLab.getCellSymbol(3,  4));
		
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(3, 4), 1, new Posicao(3, 4), new Posicao(0, 0));
		assertEquals('F', testLab.getCellSymbol(3,  4));
		
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(3, 4), 1, new Posicao(0, 0), new Posicao(3, 4));
		assertEquals('F', testLab.getCellSymbol(3,  4));
	}
	
	/**
	 * Tests the game's behavior when the hero has the shield and either sword or javelin
	 */
	@Test
	public void testOverlapShieldAndWeapons(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertEquals('H', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
		
		testLab.getHeroi().setShielded(true);
		assertEquals('@', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
		
		testLab.getHeroi().setArmado(true);
		assertEquals('&', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
		
		testLab.getHeroi().setArmado(false); 
		testLab.getHeroi().setHasJavelin(true);
		assertEquals('$', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
	}
	
	/**
	 * Tests the game's saving and loading mechanics
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test //Not done
	public void testSaveLoadGameState() throws FileNotFoundException, IOException, ClassNotFoundException{
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(1, 1), new Posicao(2, 1), 1, new Posicao(3, 1), new Posicao(4, 1));
		testLab.saveState("test.dat");
		Labirinto testLab2 = (Labirinto) Labirinto.loadState("test.dat");

		
	}
}
