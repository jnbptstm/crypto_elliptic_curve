package operations;

import java.math.BigInteger;
import java.util.HashSet;

import main.Main;

public class Courbe {

	private BigInteger p;
	private BigInteger n;
	private BigInteger a4;
	private BigInteger a6;
	private BigInteger r4;
	private BigInteger r6;
	private BigInteger gx;
	private BigInteger gy;
	private BigInteger r;
	
	private BigInteger b2;
	private BigInteger b4;
	private BigInteger b6;
	private BigInteger b8;
	private BigInteger c4;
	private BigInteger c6;
	
	private BigInteger discriminant;
	private BigInteger j_invariant;
	
	private HashSet<Point> pointsOfCurve = new HashSet<Point>();

	public Courbe(BigInteger p, BigInteger n, BigInteger a4, BigInteger a6, BigInteger r4,
			BigInteger r6, BigInteger gx, BigInteger gy, BigInteger r) {
		
		this.p = p;
		this.n = n;
		this.a4 = a4;
		this.a6 = a6;
		this.r4 = r4;
		this.r6 = r6;
		this.gx = gx;
		this.gy = gy;
		this.r = r;
		calculDiscriminant();
//		calculJ_invariant();
	}
	
	private void calculDiscriminant(){
		b2 =  BigInteger.ZERO;
		b4 = new BigInteger("2") .multiply( a4);
		b6 = new BigInteger("4").multiply(a6);
		b8 = a4.pow( 2).negate();
		c4 = b2.pow( 2) .add( new BigInteger("24") .multiply( b4).negate());
		c6 = (b2.pow( 3).negate() .add( new BigInteger("36") .multiply( b2) .multiply(b4)) .add( new BigInteger("216") .multiply( b6).negate()));
		
		this.discriminant = b2.pow( 2).negate().multiply(b8) .add( new BigInteger("8").multiply(b4.pow( 3)).negate()) .add( new BigInteger("27").multiply(b6.pow( 2).negate())) .add( new BigInteger("9").multiply(b2).multiply(b4).multiply(b6)); 
	}
	
	private void calculJ_invariant(){
		this.j_invariant = c4.pow(3).multiply(discriminant.pow(-1));
	}
	
	public BigInteger[] value(BigInteger x, BigInteger y){
		BigInteger tab[]= new BigInteger[2];
//		tab[0] = y.pow(2).mod(Main.ellipticCurve.getP());
		tab[0] = y.modPow(new BigInteger("2"), Main.ellipticCurve.getP());
		tab[1] = x.pow(3).mod(Main.ellipticCurve.getP()).add(a4.multiply(x).mod(Main.ellipticCurve.getP()).add( a6).mod(Main.ellipticCurve.getP())).mod(Main.ellipticCurve.getP());
		return tab;
	}
	/*
	 * y^2 = x^3 + a4 x + a6
	 */
	public BigInteger[] value_y(BigInteger x){
//		return new BigInteger[]{
//				Operations.sqrt(x.pow(3).mod(Main.ellipticCurve.getP()).add(a4.multiply(x).mod(Main.ellipticCurve.getP()).add(a6).mod(Main.ellipticCurve.getP()))).mod(Main.ellipticCurve.getP()), 
//				Operations.sqrt(x.pow( 3) .add( a4.multiply(x) .add( a6))).negate()
//				};
		return new BigInteger[]{
				Operations.sqrt(x.modPow(new BigInteger("3"), Main.ellipticCurve.getP()).add(Main.ellipticCurve.getA4().multiply(x).mod(Main.ellipticCurve.getP())).add(Main.ellipticCurve.getA6()).mod(Main.ellipticCurve.getP())), 
				Operations.sqrt(new BigInteger("4"))
				};
	}

	public boolean testPoints(Point point) {
		// TODO Auto-generated method stub
		BigInteger tab[]= new BigInteger[2];
		tab = this.value(point.getX(), point.getY());
		if(tab[0].equals(tab[1])){
//			System.out.println(tab[0]+"  "+tab[1]);
			return true;
		}
//		System.out.println(tab[0]+"  "+tab[1]);
		return false;
	}
	
	
//	public BigInteger value_x(BigInteger y){
//		return Math.pow(y, 2);
//	}
	
	public BigInteger getP() {
		return p;
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getA4() {
		return a4;
	}

	public BigInteger getA6() {
		return a6;
	}

	public BigInteger getR4() {
		return r4;
	}

	public BigInteger getR6() {
		return r6;
	}

	public BigInteger getGx() {
		return gx;
	}

	public BigInteger getGy() {
		return gy;
	}

	public BigInteger getR() {
		return r;
	}

	public BigInteger getB2() {
		return b2;
	}

	public BigInteger getB4() {
		return b4;
	}

	public BigInteger getB6() {
		return b6;
	}

	public BigInteger getB8() {
		return b8;
	}

	public BigInteger getC4() {
		return c4;
	}

	public BigInteger getC6() {
		return c6;
	}

	public BigInteger getDiscriminant() {
		return discriminant;
	}

	public BigInteger getJ_invariant() {
		return j_invariant;
	}
	
	public HashSet<Point> getPointsOfCurve() {
		return pointsOfCurve;
	}
}
