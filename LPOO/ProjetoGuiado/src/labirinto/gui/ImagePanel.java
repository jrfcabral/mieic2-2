package labirinto.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * <p>A simple extension to JPanel representing a panel with an image mapped over it - contains no image when initialized</p>
 */
public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private Image tile;
	
	public ImagePanel(){
		super();
	}
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(tile, 0, 0, null);
	}
	
	/**
	 * Alters the image to be show on this component
	 * @param img the image to be drawn on this component
	 */
	public void setImage(Image img){
		tile = img;
		this.repaint();
	}
}