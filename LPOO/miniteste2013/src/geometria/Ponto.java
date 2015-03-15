package geometria;

public class Ponto implements Comparable<Ponto> {
	int x;
	int y;
	
	public Ponto(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString()
	{
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
		
	}
	
	public boolean equals(Object other)
	{
		if (this == null || other == null || !(other instanceof Ponto))
			return false;
		
		Ponto otherPonto = (Ponto) other;
		
		return this.x == otherPonto.getX() && this.y == otherPonto.getY();
	}

	@Override
	public int compareTo(Ponto o) {
		if (x < o.x)
			return -1;
		if (x > o.x)
			return 1;
		if (y > o.y)
			return 1;
		if (y < o.y)
			return 1;
		return 0;
	}

	

}
