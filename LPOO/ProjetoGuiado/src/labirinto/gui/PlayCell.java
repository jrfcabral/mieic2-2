package labirinto.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

public class PlayCell extends ImagePanel implements MouseListener {
	
	PlayCell()
	{
		super();
		addMouseListener(this);
	}
	private static final long serialVersionUID = -8195336706610727034L;

	@Override
	public void mouseClicked(MouseEvent e) {
		this.getParent().dispatchEvent(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		this.getParent().dispatchEvent(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setBorder(BorderFactory.createEmptyBorder());
		this.getParent().dispatchEvent(e);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
