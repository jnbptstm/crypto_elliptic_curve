package elGamal;

import java.math.BigInteger;
import java.util.Random;

import operations.Operations;
import operations.Point;

/**
 * See algorithm in "EG_Main.java".
 */

public class EG_Bob implements Runnable{
	
	private static Point publicKey = null;
	private static Point cypheredText = null;
	
	private Point P;
	private BigInteger privateKey;
	private Point message;
	
	
	public EG_Bob(){
		
		P = EG_Main.P;
		
		// generating private key
		privateKey = new BigInteger(160, new Random());
		
		// generating public key
		System.out.println("BOB: generating public key...");
		publicKey = EG_Utils.calculPoint(privateKey, P);
		System.out.println("BOB: public key generated");
	}
	
	@Override
	public void run() {
		
		System.out.println("BOB: waiting for the ciphered text...");
		while(getCypheredText() == null){}
		System.out.println("BOB: Ciphered text received! Deciphering...");
		
		message = Operations.addition(cypheredText, Operations.oppose(Operations.multiplication(EG_Alice.getPublicKey(), privateKey)));
		System.out.println("BOB: message decyphered: "+ message);
	}

	public static synchronized void setCypheredText(Point cypheredText) {
		EG_Bob.cypheredText = cypheredText;
	}
	
	public static synchronized Point getCypheredText(){
		System.out.println("debut get");
		return cypheredText;
	}
	
	public static Point getPublicKey() {
		return publicKey;
	}	
}
