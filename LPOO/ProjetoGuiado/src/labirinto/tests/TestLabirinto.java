package labirinto.tests;

import static org.junit.Assert.*;
import labirinto.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import labirinto.logic.*;

import org.junit.Test;

public class TestLabirinto{
	
	private Labirinto testLab;

	public Labirinto mazeSetup(Posicao heroPos, Posicao swordPos, Posicao dragonPos, int dragonnum, Posicao shieldPos, Posicao javPos){
		return new Labirinto(MazeGenerator.getPredef(), MazeGenerator.getPredefSize(), dragonnum, Estrategia.PARADO, heroPos, swordPos, dragonPos, shieldPos, javPos);
	}
	
	@Test
	public void testMoveDown() {
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.BAIXO); 
		assertEquals(new Posicao(6, 4), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveUp(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.CIMA);
		assertEquals(new Posicao(4, 4), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveRight(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.DIREITA);
		assertEquals(new Posicao(5, 5), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveLeft(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.ESQUERDA);
		assertEquals(new Posicao(5, 3), testLab.getHeroi().getPosicao());
	}
	
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
	
	@Test
	public void testPickUpSword(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(6, 4), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.getHeroi().isArmado());
		assertEquals('H', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.getHeroi().isArmado());	
		assertEquals('A', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
	}
	
	@Test
	public void testDefeat(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(7, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.isPerdeu());
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.isPerdeu());
	}
	
	@Test //Preciso ver oq se passa aqui (apesar de o teste passar bem)
	public void testSlayDragon(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(7, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		testLab.getHeroi().setArmado(true);
		testLab.getHeroi().setShielded(true);
		assertFalse(testLab.nenhumDragao());
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.nenhumDragao());
	}
	
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
	
	@Test
	public void testExitNoVictory(){
		testLab = mazeSetup(new Posicao(5, 8), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.isAcabou());
		testLab.move(Direcao.DIREITA);
		assertFalse(testLab.isAcabou());
	}
	
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
	
	@Test
	public void testPickUpShield(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0), 1, new Posicao(4, 4), new Posicao(0, 0));
		assertFalse(testLab.getHeroi().hasShield());
		testLab.move(Direcao.CIMA);
		assertTrue(testLab.getHeroi().hasShield());
		assertEquals('@', testLab.getCellSymbol(testLab.getHeroi().getPosicao()));
	}
	
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
	
	@Test
	public void testRandomMaze(){
		for(int i = 0; i < 10000; i++){
			testLab = new Labirinto(MazeGenerator.generate(11), 11, 1, Estrategia.PARADO);
			Labirinto testLab2 = new Labirinto(MazeGenerator.generate(11), 11, 1, Estrategia.PARADO);
			assertNotEquals(testLab, testLab2);
		}	
	}
	
	@Test
	public void testOverlapPositions(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(3, 4), new Posicao(3, 4), 1, new Posicao(0, 0), new Posicao(0, 0));
		assertEquals('F', testLab.getCellSymbol(3,  4));
		
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(3, 4), 1, new Posicao(3, 4), new Posicao(0, 0));
		assertEquals('F', testLab.getCellSymbol(3,  4));
		
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(3, 4), 1, new Posicao(0, 0), new Posicao(3, 4));
		assertEquals('F', testLab.getCellSymbol(3,  4));
	}
	
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
	
	@Test //Not done
	public void testSaveGameState() throws FileNotFoundException, IOException, ClassNotFoundException{
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(1, 1), new Posicao(2, 1), 1, new Posicao(3, 1), new Posicao(4, 1));
		testLab.saveState(testLab, "test.dat");
		//Interface.printTabuleiro(testLab, 10);
		//System.out.print("Labirinto antes d des-serializar\n\n\n\n\n\nLabirinto dp:\n");
		Labirinto testLab2 = (Labirinto) testLab.loadState("test.dat");
		assertEquals(testLab.isAcabou(), testLab2.isAcabou());
		//Interface.printTabuleiro(testLab2, 10);
		
	}
}
