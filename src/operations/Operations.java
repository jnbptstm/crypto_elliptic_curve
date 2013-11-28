package operations;

import java.math.BigInteger;
import java.util.*;

import main.Main;


public class Operations {
	
	public static Point oppose(Point p){
		return new Point(p.getX(), p.getY().negate(), false);
	}
	
	public static Point addition(Point p, Point q){
	
		Point r = new Point();
		BigInteger lambda = BigInteger.ZERO;
		
		if(!(p.getX().equals(q.getX()))){
			lambda = (p.getY().subtract(q.getY()).mod(Main.ellipticCurve.getP())).multiply((p.getX().subtract(q.getX()).mod(Main.ellipticCurve.getP())).modInverse(Main.ellipticCurve.getP()));
			r.setX(lambda.modPow(new BigInteger("2"), Main.ellipticCurve.getP()).subtract(p.getX()).mod(Main.ellipticCurve.getP()).subtract(q.getX()).mod(Main.ellipticCurve.getP()));
			r.setY(lambda.negate().multiply(r.getX()).mod(Main.ellipticCurve.getP()).add(lambda.multiply(p.getX()).mod(Main.ellipticCurve.getP())).mod(Main.ellipticCurve.getP()).subtract(p.getY()).mod(Main.ellipticCurve.getP()));
		}

		else if(p.getX().equals(q.getX()) && !(p.getY().equals(q.getY()))){
			r.setX(BigInteger.ZERO);
			r.setY(BigInteger.ZERO);
			r.setIsInfinite(true);
		}
		
		else if(p.getX().equals(q.getX()) && p.getY().equals(q.getY()) && !(p.getY().equals(BigInteger.ZERO))){
			lambda = (new BigInteger("3").multiply(p.getX().modPow(new BigInteger("2"), Main.ellipticCurve.getP())).mod(Main.ellipticCurve.getP()).add(Main.ellipticCurve.getA4()).mod(Main.ellipticCurve.getP())).multiply(p.getY().multiply(new BigInteger("2")).modInverse(Main.ellipticCurve.getP())).mod(Main.ellipticCurve.getP());
			r.setX(lambda.modPow(new BigInteger("2"), Main.ellipticCurve.getP()).subtract(p.getX()).mod(Main.ellipticCurve.getP()).subtract(q.getX()).mod(Main.ellipticCurve.getP()));
			r.setY(lambda.negate().multiply(r.getX()).mod(Main.ellipticCurve.getP()).add(lambda.multiply(p.getX()).mod(Main.ellipticCurve.getP())).subtract(p.getY()).mod(Main.ellipticCurve.getP()));
		}
		
		else if(p.getX().equals(q.getX()) && p.getY().equals(q.getY()) && p.getY().equals(BigInteger.ZERO)){
			r.setX(BigInteger.ZERO);
			r.setY(BigInteger.ZERO);
			r.setIsInfinite(true);
		}
		
		return r;
	}
	
	public static Point doublement(Point p){ // return 2*p.
		return addition(p, p);
	}
	
	public static Point multiplication(Point p, BigInteger n){
		
		if (p.getIsInfinite() == true) return new Point(BigInteger.ZERO, BigInteger.ZERO, true);
		
		Point res = new Point(p);	
		String Bin = new BigInteger(n.toString(), 16).toString(2);
		
		for (int ii = Bin.length()-1 ; ii > 0 ; ii--){
			res = doublement(res);
			if (Bin.subSequence(Bin.length()-ii, Bin.length() - ii + 1).equals("1")) {
				res = addition(res, p);
			}
		}
		
		if(n.equals(Main.ellipticCurve.getP())) res.setIsInfinite(true);
		return res;
	}
	
	//hashmap avec p1:e1 ... pr:er>> p1^e1 ....
	//pair: decomposition en nombre premier, impaire : multiplicité du nombre premier
	public static LinkedList<BigInteger> ordreCourbe(){
		return new LinkedList<BigInteger>();
	}
	
	public static BigInteger[] ordreApproximatifCourbe(BigInteger q){
		return new BigInteger[]{q.add(BigInteger.ONE).add(new BigInteger("2").multiply(Operations.sqrt(q))),q.add(BigInteger.ONE).add((new BigInteger("2").multiply(Operations.sqrt(q))).negate())};	
	}
	
	public static BigInteger ordrePoint(Point p){
		
		BigInteger bi1 = BigInteger.ZERO;
		BigInteger bi2 = BigInteger.ZERO;
		
		LinkedList<BigInteger> ordreCourbe = ordreCourbe();
		ListIterator<BigInteger> it =ordreCourbe.listIterator();
		BigInteger m = new BigInteger("0");
		
		//BigInteger m = ordreCourbe; #E
		String xx = Integer.toString(ordreCourbe.size());
		BigInteger r =  (new BigInteger(xx).divide(new BigInteger("2")));
		
		for(BigInteger i = new BigInteger("0") ; i.compareTo(new BigInteger("2").multiply(r)) < 0 ; i.add(BigInteger.ONE)){
			
			bi1 = it.next();
			bi2 = it.next();
			m = m.divide(bi1.pow(bi2.intValue()).mod(Main.ellipticCurve.getP()));
			Point Q = new Point();
			Q = multiplication(p, m);
			
			while(Q.isElementNeutre()){
				Q = multiplication(p, bi1);
				m = m.multiply(bi1);
			}
		}
		return m;
	}
	
	public static  BigInteger sqrt(BigInteger n) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		while(b.compareTo(a) >= 0) {
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if(mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
			else a = mid.add(BigInteger.ONE);
		}
		return a.subtract(BigInteger.ONE);
	}
	
	
	public static void main(String args[]){
		
		Point P = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
		
		Point addP = addition(P, P);
		System.out.println("P+P: "+ addP);
		
		Point pp = multiplication(P, new BigInteger("2"));
		System.out.println("2P : "+ pp);
	}
}
