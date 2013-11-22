package diffieHellman;

import java.math.BigInteger;

import main.Main;

import operations.Courbe;
import operations.Point;

public class DH_Alice implements Runnable{
	
	private Courbe courbe;
	private BigInteger x;
	private Point p = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
	private BigInteger clefPriveeA;
	private Point A;
	private static Point B = null;

	private Point clefSecrete;
	
	public DH_Alice(Courbe courbe, BigInteger x){
		this.courbe = courbe;
		this.x = x;
	}
	
	@Override
	public void run(){
		clefPriveeA = new BigInteger("2");
		
		A = DH_Utils.calculPoint(this.clefPriveeA, p);
		
		System.out.println("ALICE: Alice envoie A à Bob...");
		DH_Utils.envoyerDeAaB(A);
		System.out.println("ALICE: Alice a envoyé A à Bob. Attente de l'envoie de Bob...");
		while(DH_Alice.getB() == null){}
		System.out.println("ALICE: Envoie Bob effectué. Calcul clé secrète...");
		
		clefSecrete = DH_Utils.calculPoint(this.clefPriveeA, B);
		System.out.println("ALICE: clé secrète est "+ clefSecrete);
		
	}

	public static synchronized Point getB() {
		return B;
	}

	public Courbe getCourbe() {
		return courbe;
	}

	public BigInteger getX() {
		return x;
	}

	public Point getP() {
		return p;
	}

	public BigInteger getClefPriveeA() {
		return clefPriveeA;
	}

	public Point getA() {
		return A;
	}

	public Point getClefSecrete() {
		return clefSecrete;
	}
	
	public static synchronized void setB(Point b) {
		B = b;
	}
}
