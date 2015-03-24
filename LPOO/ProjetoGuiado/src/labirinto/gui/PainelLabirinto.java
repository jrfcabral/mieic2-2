package labirinto.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import labirinto.logic.Labirinto;

public class PainelLabirinto extends JPanel {
	
	private static final long serialVersionUID = -960304048446998188L;
	private Labirinto labirinto;
	private Grelha grelhaLabirinto;
	
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 100, 100);
		
		
	}
	
	public PainelLabirinto(Labirinto labirinto){
		this.labirinto = labirinto;
		grelhaLabirinto = new Grelha(this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.labirinto.getDimensao(), this.labirinto.getDimensao());
		
	//	this.setBorder(BorderFactory.createLineBorder(Color.PINK));
		
		
		
		grelhaLabirinto.setBounds(this.getBounds());
		Dimension d = new Dimension(100, 100);
		grelhaLabirinto.setPreferredSize(d);
		grelhaLabirinto.setLayout(null);
		
		this.add(grelhaLabirinto);
	}

}
