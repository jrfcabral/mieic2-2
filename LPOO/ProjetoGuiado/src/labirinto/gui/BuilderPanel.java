package labirinto.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import labirinto.logic.GridQueryable;
import labirinto.logic.Labirinto;

/**
 * Extension of the GridPanel class to allow editing of a maze
 */
public class BuilderPanel extends GridPanel<Character> implements MouseListener{
	
	private static final long serialVersionUID = 7840038385900092960L;
	private Labirinto labirinto;
	
	public BuilderPanel(Labirinto grid) {
		super(grid);		
		labirinto = grid;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
