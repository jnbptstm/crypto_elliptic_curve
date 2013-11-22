package diffieHellman;

import java.math.BigInteger;

import main.Main;

import operations.Courbe;
import operations.Point;

public class DH_Bob implements Runnable{

	private Courbe courbe;
	private BigInteger x;
	private Point p = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
	private BigInteger clefPriveeB;
	private Point B;
	private Point clefSecrete;
	private static Point A = null;
	
	public static void setA(Point a) {
		A = a;
	}

	public DH_Bob(Courbe courbe, BigInteger x){
		this.courbe = courbe;
		this.x = x;
	}
	
	@Override
	public void run(){
		
		this.clefPriveeB = new BigInteger("3");
		B = DH_Utils.calculPoint(this.clefPriveeB, p);

		System.out.println("BOB: Bob envoie B à Alice...");
		DH_Utils.envoyerDeBaA(B);
		System.out.println("BOB: Bob a envoyé B à Alice. Attente de l'envoie d'Alice...");
		while(DH_Bob.getA() == null){}
		System.out.println("BOB: Envoie Alice effectué. Calcul clé secrète...");
		
		clefSecrete = DH_Utils.calculPoint(this.clefPriveeB, A);
		System.out.println("BOB: clé secrète est "+ clefSecrete);
		
	}

	public static synchronized Point getA() {
		return A;
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

	public BigInteger getClefPriveeB() {
		return clefPriveeB;
	}

	public Point getB() {
		return B;
	}

	public Point getClefSecrete() {
		return clefSecrete;
	}
}
