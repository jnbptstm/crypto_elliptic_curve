package elGamal;

import java.math.BigInteger;

import main.Main;
import operations.Point;

/**
 * 
 * Courbe elliptique E et P point sur la courbe de grand ordre n.
Bob choisit x entier, 0 inf x inf n -1:
x est la clef privée de Bob.
H = x * P est la clef publique de Bob.


Alice transmet le message m appartient a E:
Elle choisit k entier au hasard.
Elle calcule a = k  *H.
Elle calcule b = m .add( a.
Elle transmet (a; b) à Bob.

Bob déchiffre:
Bob calcule c = x * a = x *(k  *P) = (xk) * P.
Bob retrouve le message par
m = b - c

 * @author p1106466
 *
 */
public class EG_Main {
	
	public static BigInteger x;
	public static Point P;
	public static EG_Bob eg_bob;
	public static EG_Alice eg_alice;
	
	public static void main(String args[]){

//		x = new BigInteger("6464761");
//		P = new Point(x, Main.ellipticCurve.value_y(x)[0], false);
		P = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
		
		Thread t_alice = new Thread(new EG_Alice(Main.ellipticCurve, P));
		Thread t_bob = new Thread(new EG_Bob(Main.ellipticCurve, P));
		
		t_alice.start();
		t_bob.start();
	}
	
}