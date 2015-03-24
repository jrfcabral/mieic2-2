package labirinto.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Grelha extends JPanel {
	private int x, y;
	private int sx, sy;
	private int xcells, ycells;
	private Celula[][] celulas;
	
	@Override
	public void paintComponent(Graphics g)
	{		
		
		super.paintComponent(g);	
		System.out.println("Printandogrelha");
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 2000, 2000);
		
	}
	
	public Grelha(int x, int y, int width, int length, int xcells, int ycells)
	{
		
		this.x = x;
		this.y = y;
		this.sx = width;
		this.sy = length;
		this.xcells = xcells;
		this.ycells = ycells;
		celulas = new Celula[sx][sy];
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		inicializaCelulas();
				
		
	}

	private void inicializaCelulas() {
		int xCellSide = sx/xcells;
		int yCellSide = sy/ycells;
		
		for (int i = 0; i < sx; i++)
			for(int j = 0; j < sy; j++){
				celulas[i][j] = new Celula(x+xCellSide*i, y+yCellSide*j, xCellSide, yCellSide);
				celulas[i][j].setBounds(x+xCellSide*i, y+yCellSide*j, xCellSide, yCellSide);
				this.add(celulas[i][j]);
				
			}
	}
}
