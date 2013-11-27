package elGamal;

import java.math.BigInteger;
import java.util.Random;

import operations.Courbe;
import operations.Operations;
import operations.Point;

public class EG_Bob implements Runnable{
	
	private BigInteger x; // cl� priv�e
	private static Point H = null; // cl� publique
	
	private static Point A = null;
	private static Point B = null;
	
	private static Point C;
	private Point m;
	
	public EG_Bob(Courbe courbe, Point P){
		//choix de x
//		x = new BigInteger("2").pow( 22);
		x = new BigInteger(160, new Random());
		
		System.out.println("BOB: calcul de la cl� publique...");
		H = EG_Utils.calculPoint(x, P);
		System.out.println("BOB: cl� publique g�n�r�e.");
	}
	
/**
 * Bob d�chiffre:
Bob calcule c = x * a = x *(k  *P) = (xk) * P.
Bob retrouve le message par
m = b - c
 */
	@Override
	public void run() {
		
		System.out.println("BOB: attente de la g�n�ration de 'a' et 'b' par Alice...");
//		while(EG_Alice.getA() == null && EG_Alice.getB() == null){}
//		A = EG_Alice.getA();
//		B = EG_Alice.getB();
		
		while(EG_Bob.getA() == null && EG_Bob.getB() == null){}
		
		System.out.println("BOB: g�n�ration de 'a' et 'b' par Alice effectu�e.");
		System.out.println("BOB: A="+ A);
		System.out.println("BOB: B="+ B);
		
		System.out.println("BOB: d�chiffrement du message...");
		
		C = EG_Utils.calculPoint(x, A);
		System.out.println("C: "+ C);
		m = EG_Utils.addPoint(B, Operations.oppose(C));
		
//		System.out.println(C);
//		System.out.println(Operations.oppose(C));
		
		System.out.println("BOB: message d�chiffr�.");
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
