package elGamal;

import java.math.BigInteger;
import java.util.Random;

import main.Main;

import operations.Courbe;
import operations.Operations;
import operations.Point;

/**

Alice transmet le message m appartient a E:
Elle choisit k entier au hasard.
Elle calcule a = k  *H.
Elle calcule b = m .add( a.
Elle transmet (a; b) à Bob.

 * @author p1106466 et JB
 *
 */
public class EG_Alice implements Runnable{
	
	private Point m;
	private BigInteger k; // clé privée (?)
	private static Point a = null;
	private static Point b = null;
	
	public EG_Alice(Courbe courbe, Point P){
		
		//Message m
//		BigInteger x = new BigInteger("666");
//		m = new Point (x, courbe.value_y(x)[0], false);
		m = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
		System.out.println("Message: "+ m);
		
		//choisis k
		k = new BigInteger(160, new Random());
		
	}

	@Override
	public void run() {
		
		System.out.println("ALICE: attente de la génération de la clé publique de Bob...");
		while(EG_Bob.getH() == null){}
		
//		System.out.println("ALICE: clé publique de Bob générée. Calcul de k*H...");
//		EG_Alice.setA(EG_Utils.calculPoint(k, EG_Bob.getH()));
		System.out.println("ALICE: clé publique de Bob générée. Calcul de C1 = k*P...");
		EG_Alice.setA(Operations.multiplication(EG_Main.P, k));
		
//		System.out.println("ALICE: calcul de k*H effectué. Calcul de b=m+a...");
//		b = EG_Utils.addPoint(m, a);
		System.out.println("ALICE: calcul de k*P effectué. Calcul de C2 = k*H...");
		EG_Alice.setB(Operations.multiplication(EG_Bob.getH(), k));
		
		System.out.println("ALICE: A="+ EG_Alice.getA() +"\nB="+ EG_Alice.getB());
		
		System.out.println("ALICE: calcul de b=m+a effectué. Envoie du couple (a, b) à Bob...");
		EG_Utils.envoyerDeAaB(a, b);
		
		System.out.println("ALICE: couple (a, b) envoyé à Bob.");
	}
	
	public static synchronized Point getA() {
		return a;
	}

	public static synchronized Point getB() {
		return b;
	}

	public static synchronized void setA(Point a) {
		EG_Alice.a = a;
	}

	public static synchronized void setB(Point b) {
		EG_Alice.b = b;
	}
}
