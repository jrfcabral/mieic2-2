package utilitarios;

import java.util.Random;

public class Hipotese {
	private int numerador;
	private int denominador;
	private Random gerador;
	
	public Hipotese(int numerador, int denominador)
	{
		if (numerador > denominador)
			throw new IllegalArgumentException();
		
		this.numerador = numerador;
		this.denominador = denominador;
		gerador = new Random();
	}
	
	public boolean isSucesso()
	{
		int tentativa = gerador.nextInt(denominador) +1;
		if (tentativa >= numerador)
			return true;
		else
			return false;
	}
}
