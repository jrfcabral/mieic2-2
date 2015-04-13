package labirinto.gui;

/**
 * Interface to be implemented by classes that are able to transform a grid x and y according to internally decided procedures specific to that class.
 */
public interface GridTransformer {
	
	/**
	 * Transforms this class' grid' cell at position (x,y) according to the internel procedures of the class.
	 * @param x
	 * @param y
	 */
	public void transform(int x, int y);
}
