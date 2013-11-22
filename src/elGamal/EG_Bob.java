package elGamal;

import java.math.BigInteger;
import java.util.Random;

import operations.Courbe;
import operations.Operations;
import operations.Point;

public class EG_Bob implements Runnable{
	
	private BigInteger x; // clé privée
	private static Point H = null; // clé publique
	
	private static Point A = null;
	private static Point B = null;
	
	private static Point C;
	private Point m;
	
	public EG_Bob(Courbe courbe, Point P){
		//choix de x
//		x = new BigInteger("2").pow( 22);
		x = new BigInteger(160, new Random());
		
		System.out.println("BOB: calcul de la clé publique...");
		H = EG_Utils.calculPoint(x, P);
		System.out.println("BOB: clé publique générée.");
	}
	
/**
 * Bob dï¿½chiffre:
Bob calcule c = x * a = x *(k  *P) = (xk) * P.
Bob retrouve le message par
m = b - c
 */
	@Override
	public void run() {
		
		System.out.println("BOB: attente de la génération de 'a' et 'b' par Alice...");
//		while(EG_Alice.getA() == null && EG_Alice.getB() == null){}
//		A = EG_Alice.getA();
//		B = EG_Alice.getB();
		
		while(EG_Bob.getA() == null && EG_Bob.getB() == null){}
		
		System.out.println("BOB: génération de 'a' et 'b' par Alice effectuée.");
		System.out.println("BOB: A="+ A);
		System.out.println("BOB: B="+ B);
		
		System.out.println("BOB: déchiffrement du message...");
		C = EG_Utils.calculPoint(x, A);
		m = EG_Utils.addPoint(B, Operations.oppose(C));
		
//		System.out.println(C);
//		System.out.println(Operations.oppose(C));
		
		System.out.println("BOB: message déchiffré.");
		System.out.println(m.toString());
		
	}
	
	public static synchronized Point getA() {
		return A;
	}

	public static synchronized Point getB() {
		return B;
	}

	public static synchronized Point getH() {
		return H;
	}

	public static synchronized void setB(Point b) {
		B = b;
	}

	public static synchronized void setA(Point a) {
		A = a;
	}
		
}
