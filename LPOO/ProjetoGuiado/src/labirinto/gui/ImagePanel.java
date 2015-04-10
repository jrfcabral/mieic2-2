package labirinto.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private Image tile;
	
	public ImagePanel(){
		super();
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(tile, 0, 0, null);
	}
	
	public void setImg(Image img){
		tile = img;
	}
}