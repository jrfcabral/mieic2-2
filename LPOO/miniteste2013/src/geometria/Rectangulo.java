package geometria;

public class Rectangulo implements Figura {
	int x1,x2,y1,y2;

	public Rectangulo(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	@Override
	public double getArea() {
		return (x2-x1)*(y2-y1);
	}

	@Override
	public double getPerimetro() {
		return 2*(x2-x1) + 2*(y2-y1);
	}

}
