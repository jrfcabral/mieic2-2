package geometria;

public class FiguraComposta implements Figura, Countable {
	private Figura[] figuras;
	
	public FiguraComposta(Figura[] figuras) {
		this.figuras = figuras;
	}


	public double getArea() {
		double result =0;
		for (Figura figura:figuras){
			result += figura.getArea();
		}
		return result;
			
	}

	
	public double getPerimetro() {
		double result =0;
		for (Figura figura:figuras){
			result += figura.getPerimetro();
		}
		return result;
	}


	@Override
	public int count() {
		int result=0;
		for (Figura figura:figuras)
			result++;
		return result;
	}

}
