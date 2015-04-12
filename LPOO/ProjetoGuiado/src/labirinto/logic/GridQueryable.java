package labirinto.logic;

/**
 * Represents an object that can be queryed for values at each particular position,
 * returning a value of type T
 */
public interface GridQueryable<T> {
	public T getCellSymbol(Posicao pos);
	public T getCellSymbol(int x, int y);
	public int getDimensao();
}
