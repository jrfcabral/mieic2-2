package labirinto.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.util.concurrent.ConcurrentHashMap;

import labirinto.logic.GridQueryable;

/**
 * <p>A simple extension of JPanel to visually represent grid objects such as mazes.</p>
 */
public class GridPanel<T> extends JPanel {
	
	private static final long serialVersionUID = 5796328570279197459L;
	private GridQueryable<T> grid;
	private Border cellBorder;

	private ConcurrentHashMap<T, Image> imageMap;
	private ImagePanel[][] gridElements;	

	public GridPanel(GridQueryable<T> grid){
		super();
		if (grid == null)
			throw new NullPointerException();
		if (grid.getDimensao() < 1)
			throw new IllegalArgumentException();
		
		this.grid = grid;
		this.imageMap = new ConcurrentHashMap<T, Image>();
		this.gridElements = new ImagePanel[grid.getDimensao()][grid.getDimensao()];
		this.setCellBorder(BorderFactory.createEmptyBorder());		
		updateGrid();
	}
	
	/**
	 * <p>Refreshes the grid elements according to changes that may have happened to the represented grid. Should be called
	 * everytime the grid is expected to have changed.</p>
	 */
	public void updateGrid() {
		int dimensao = grid.getDimensao();
		
		this.setLayout(new GridLayout(dimensao, dimensao,0,0));
		gridElements = new PlayCell[dimensao][dimensao];
		this.removeAll();
		for (int i = 0; i < dimensao; i++)
			for (int j = 0; j < dimensao; j++){
				gridElements[i][j] = new PlayCell();
				if (imageMap.get(grid.getCellSymbol(i, j)) != null)
					gridElements[i][j].setImage(imageMap.get(grid.getCellSymbol(i, j)).getScaledInstance(800/dimensao, 800/dimensao, Image.SCALE_FAST));
				gridElements[i][j].setBorder(cellBorder);
				gridElements[i][j].setPreferredSize(new Dimension(800/dimensao, 800/dimensao));
				gridElements[i][j].setBackground(Color.BLACK);
				this.add(gridElements[i][j]);				
			}		
	}
		
	/**
	 * @return the map that transofrms each of the elements of the grid into an image
	 */
	public ConcurrentHashMap<T, Image> getImageMap() {
		return imageMap;
	}

	/**
	 * @param imageMap map of the image that the elements outputted by the underlying grid should translate to.
	 */
	public void setImageMap(ConcurrentHashMap<T, Image> imageMap) {
		this.imageMap = imageMap;
	}	

	/**
	 * @param cellBorder The border all cells of this grid should have
	 */
	public void setCellBorder(Border cellBorder) {
		this.cellBorder = cellBorder;
	}
	
	/**
	 * @param grid the grid this object should display
	 */
	public void setGrid(GridQueryable<T> grid) {
		this.grid = grid;
	}
}
