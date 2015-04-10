package labirinto.gui;

import java.awt.*;
import javax.swing.*;

import java.util.concurrent.ConcurrentHashMap;

import labirinto.logic.GridQueryable;

/**
 * <p>A simple extension of JPanel to visually represent grid objects such as mazes.</p>
 */
public class GridPanel<T> extends JPanel {
	
	private static final long serialVersionUID = 5796328570279197459L;
	private GridQueryable<T> grid;
	public void setGrid(GridQueryable<T> grid) {
		this.grid = grid;
	}

	private ConcurrentHashMap<T, Image> componentMap;
	private ImagePanel[][] gridElements;	

	public GridPanel(GridQueryable<T> grid){
		super();
		if (grid == null)
			throw new NullPointerException();
		if (grid.getDimensao() < 1)
			throw new IllegalArgumentException();
		
		this.grid = grid;
		this.componentMap = new ConcurrentHashMap<T, Image>();
		this.gridElements = new ImagePanel[grid.getDimensao()][grid.getDimensao()];
		resizeImages();
		updateGrid();
	}
	
	/**
	 * <p>Refreshes the grid elements according to changes that may have happened to the represented grid. Should be called
	 * everytime the grid is expected to have changed.</p>
	 */
	public void updateGrid() {
		int dimensao = grid.getDimensao();
		this.setLayout(new GridLayout(dimensao, dimensao,0,0));
		gridElements = new ImagePanel[dimensao][dimensao];
		this.removeAll();
		for (int i = 0; i < dimensao; i++)
			for (int j = 0; j < dimensao; j++){
				gridElements[i][j] = new ImagePanel();
				if (componentMap.get(grid.getCellSymbol(i, j)) != null)
					gridElements[i][j].setImg(componentMap.get(grid.getCellSymbol(i, j)));//.getScaledInstance(800/dimensao, 800/dimensao, Image.SCALE_SMOOTH));
				gridElements[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
				gridElements[i][j].setPreferredSize(new Dimension(800/dimensao, 800/dimensao));
				gridElements[i][j].setBackground(Color.BLACK);
				this.add(gridElements[i][j]);				
			}		
	}
		
	public ConcurrentHashMap<T, Image> getComponentMap() {
		return componentMap;
	}

	public void setComponentMap(ConcurrentHashMap<T, Image> componentMap) {
		this.componentMap = componentMap;
	}
	
	private void resizeImages(){
		int dimensao = grid.getDimensao();
		componentMap.forEach( (T t, Image i)->i.getScaledInstance(800/dimensao, 800/dimensao, Image.SCALE_SMOOTH) );
	}
}
