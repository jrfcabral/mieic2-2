package labirinto.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;

import java.util.concurrent.ConcurrentHashMap;

import labirinto.logic.GridQueryable;

/**
 * <p>A simple extension of JPanel to visually represent grid objects such as mazes.</p>
 */
public class GridPanel<T> extends JPanel {
	
	public static final int GRID_PANEL_DIMENSION = 700;
	private static final long serialVersionUID = 5796328570279197459L;
	private GridQueryable<T> grid;
	private Border cellBorder;
	private GridTransformer transformer;

	private ConcurrentHashMap<T, Image> imageMap;
	private ImagePanel[][] gridElements;	

	public GridPanel(GridQueryable<T> grid, GridTransformer t)
	{
		this(grid);
		transformer = t;
	}
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
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Double x = e.getXOnScreen() - GridPanel.this.getLocationOnScreen().getX() -5;
				x = x/(GridPanel.GRID_PANEL_DIMENSION / grid.getDimensao());
				x = Math.floor(x);
				
				Double y = e.getYOnScreen() - GridPanel.this.getLocationOnScreen().getY() -5;
				y = y/(GridPanel.GRID_PANEL_DIMENSION / grid.getDimensao());
				y = Math.floor(y);
				
				
				
				GridPanel.this.transformer.transform(y.intValue(), x.intValue());
				
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
			
		});
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
		for (int i = 0; i < dimensao; i++){
			
			for (int j = 0; j < dimensao; j++){
				gridElements[i][j] = new PlayCell();
				if (imageMap.get(grid.getCellSymbol(i, j)) != null)
					gridElements[i][j].setImage(imageMap.get(grid.getCellSymbol(i, j)).getScaledInstance(GRID_PANEL_DIMENSION/dimensao, GRID_PANEL_DIMENSION/dimensao, Image.SCALE_SMOOTH));
				gridElements[i][j].setBorder(cellBorder);
				gridElements[i][j].setPreferredSize(new Dimension(GRID_PANEL_DIMENSION/dimensao, GRID_PANEL_DIMENSION/dimensao));
				gridElements[i][j].setBackground(Color.BLACK);
				
				this.add(gridElements[i][j]);				
			}		
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
