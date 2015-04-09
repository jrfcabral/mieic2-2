package utilitarios;

import java.util.Random;

/**
 * An utilitary class that provides a "chance" generator.
 *
 */
public class Hipotese {
	private int numerador;
	private int denominador;
	private Random gerador;
	
	/**
	 * Constructor for the Hipotese class.
	 * 
	 * @param numerador Self-explanatory
	 * @param denominador Self-Explanatory
	 */
	public Hipotese(int numerador, int denominador)
	{
		if (numerador > denominador)
			throw new IllegalArgumentException();
		
		this.numerador = numerador;
		this.denominador = denominador;
		gerador = new Random();
	}
	
	/**
	 * Has the random number generator generate a new number and determines an event's success according to the numerador and denominador parameters.
	 * 
	 * @return Returns true when the random generator returns a number bigger than or equal to numerator, false otherwise. 
	 */
	public boolean isSucesso()
	{
		int tentativa = gerador.nextInt(denominador) +1;
		if (tentativa >= numerador)
			return true;
		else
			return false;
	}
}
