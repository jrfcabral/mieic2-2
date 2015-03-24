package labirinto.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Celula extends JPanel {
	private int x, y;
	private int length, height;
	
	Color color;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Celula(int x, int y, int sx, int sy)
	{
		this.x = x;
		this.y = y;
		this.length = sx;
		this.height = sy;
		color = Color.PINK;
		this.setPreferredSize(new Dimension(100, 100));
		
		
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		System.out.println("Printei-me");
		g.setColor(color);
		g.drawRect(0,0,length,height);
	}
}
