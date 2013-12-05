package operations;

import java.math.BigInteger;

/**
 * Class that defines a Point
 */

public class Point {

	private BigInteger x;
	private BigInteger y;
	private boolean isInfinite;
	
	public Point(){
		this.x = BigInteger.ZERO;
		this.y = BigInteger.ZERO;
		this.isInfinite = false;
	}
	
	public Point(BigInteger x, BigInteger y, Boolean isInfinite){
		this.x = x;
		this.y = y;
		this.isInfinite = isInfinite;
	}
	
	public Point(Point p){
		this(p.getX(), p.getY(), p.getIsInfinite());
	}
	
	public boolean getIsInfinite() {
		return isInfinite;
	}

	public void setIsInfinite(Boolean isInfinite) {
		this.isInfinite = isInfinite;
	}
	
	public boolean isElementNeutre(){
		if(this.getX().equals(BigInteger.ZERO) && this.getY().equals(BigInteger.ZERO)) return true;
		else return false;
	}
	
	public BigInteger getX() {
		return x;
	}

	public void setX(BigInteger x) {
		this.x = x;
	}

	public BigInteger getY() {
		return y;
	}

	public void setY(BigInteger y) {
		this.y = y;
	}
	
	public void setCoord(Point p){
		this.x = p.getX();
		this.y = p.getY();
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null) return false;
		if(getClass() != o.getClass()) return false;
		
		Point p = (Point) o;
		if(p.getX().equals(this.x) && p.getY().equals(this.y) && p.getIsInfinite() == this.isInfinite) return true;
		else return false;
	}
	
	@Override
	public String toString(){
		return "Point x= "+this.x+", y= "+this.y+".";
	}
	
	// Tests...
	public static void main(String args[]){
		
		Point p1 = new Point(new BigInteger("2"), new BigInteger("3"), false);
		Point p2 = new Point();
		Point p3 = new Point(p1);
		Point p4 = p2;
		
		assert(p2 == p4);
		assert(p2.equals(p4));
		
		assert(p1 != p3);
		assert(p1.equals(p3));
		
		assert(p1 != p2);
		assert(!p1.equals(p2));
	}
}
