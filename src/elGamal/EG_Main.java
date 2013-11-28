package elGamal;

import java.math.BigInteger;

import main.Main;
import operations.Point;

/********************************************
 *  El Gamal Encryption with Elliptic Curve *
 ********************************************/
/*
 * 1. Alice and Bob pick a point P that lies on the elliptic
 * curve (on Fp).
 * 
 * 2. Alice and Bob chose a random number, x and y, between 1 and p-1
 * These are the private keys.
 * 
 * 3. Alice computes R = yP and Bob computes Q = xP.
 * R and Q are the public keys. 
 * 
 * 4. For each message:
 *      a. Alice calculates S = M + yQ = M + xyP
 *      b. Alice sends the ciphertext S to Bob
 *      c. To read the message, Bob calculates M = S - xR = S - xyP
 *     
 *      
 * Source: http://web.maths.unsw.edu.au/~jim/mandyseetthesis.pdf
 */

public class EG_Main {
	
	public static BigInteger x;
	public static Point P;
	public static EG_Bob eg_bob;
	public static EG_Alice eg_alice;
	
	public static void main(String args[]){

		P = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
		
		Thread t_alice = new Thread(new EG_Alice());
		Thread t_bob = new Thread(new EG_Bob());
		t_bob.start();
		t_alice.start();	
	}
}