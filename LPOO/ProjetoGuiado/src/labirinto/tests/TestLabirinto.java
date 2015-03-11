package labirinto.tests;

import static org.junit.Assert.*;
import labirinto.logic.*;

import org.junit.Test;

public class TestLabirinto {
	
	private Labirinto testLab;
	
	public Labirinto mazeSetup(Posicao heroPos, Posicao swordPos, Posicao dragonPos){
		return new Labirinto(MazeGenerator.getPredef(), MazeGenerator.getPredefSize(), 1, Estrategia.PARADO, heroPos, swordPos, dragonPos);
	}
	
	@Test
	public void testMoveDown() {
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.BAIXO);
		assertEquals(new Posicao(6, 4), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveUp(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.CIMA);
		assertEquals(new Posicao(4, 4), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveRight(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.DIREITA);
		assertEquals(new Posicao(5, 5), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveLeft(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(0, 0));
		testLab.move(Direcao.ESQUERDA);
		assertEquals(new Posicao(5, 3), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testMoveAgainstWall(){
		testLab = mazeSetup(new Posicao(8, 1), new Posicao(0, 0), new Posicao(0, 0));
		
		testLab.move(Direcao.BAIXO);
		assertEquals(new Posicao(8, 1), testLab.getHeroi().getPosicao());
		
		testLab.move(Direcao.ESQUERDA);
		assertEquals(new Posicao(8, 1), testLab.getHeroi().getPosicao());
		
		testLab.move(Direcao.BAIXO);
		assertEquals(new Posicao(8, 1), testLab.getHeroi().getPosicao());
		
		//moving to a position where it's not possible to move up
		testLab = mazeSetup(new Posicao(1, 1), new Posicao(0, 0), new Posicao(0, 0));
		
		testLab.move(Direcao.CIMA);
		assertEquals(new Posicao(1, 1), testLab.getHeroi().getPosicao());
	}
	
	@Test
	public void testPickUpSword(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(6, 4), new Posicao(0, 0));
		assertFalse(testLab.getHeroi().isArmado());
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.getHeroi().isArmado());	
	}
	
	@Test
	public void testDefeat(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(7, 4));
		assertFalse(testLab.isPerdeu());
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.isPerdeu());
	}
	
	@Test //Preciso ver oq se passa aqui (apesar de o teste passar bem)
	public void testSlayDragon(){
		testLab = mazeSetup(new Posicao(5, 4), new Posicao(0, 0), new Posicao(7, 4));
		testLab.getHeroi().setArmado(true);
		testLab.getHeroi().setShielded(true);
		assertFalse(testLab.nenhumDragao());
		testLab.move(Direcao.BAIXO);
		assertTrue(testLab.nenhumDragao());
	}
	
	@Test //Fazer o percurso todo? (Apanhar espada, matar dragao, andar ate saida)
	public void testVictory(){
		testLab = mazeSetup(new Posicao(6, 8), new Posicao(0, 0), new Posicao(5, 8));
		assertFalse(testLab.isAcabou());
		testLab.getHeroi().setArmado(true);
		testLab.getHeroi().setShielded(true);
		testLab.move(Direcao.CIMA);
		testLab.move(Direcao.DIREITA);
		assertTrue(testLab.isAcabou());
		
	}
	
	@Test
	public void testExitNoVictory(){
		testLab = mazeSetup(new Posicao(5, 8), new Posicao(0, 0), new Posicao(0, 0));
		assertFalse(testLab.isAcabou());
		testLab.move(Direcao.DIREITA);
		assertFalse(testLab.isAcabou());
	}

}
