package elGamal;

import java.math.BigInteger;
import java.util.Random;

import operations.Operations;
import operations.Point;

/**
 * See algorithm in "EG_Main.java".
 */

public class EG_Alice implements Runnable{
	
	private static Point publicKey = null;
	
	private Point message;
	private Point P = null;
	private BigInteger privateKey;
	private Point cypherText = null;
	
	public EG_Alice(Point message){
		
		P = EG_Main.P;
		
		// message
		this.message = message;
		System.out.println("ALICE: message: "+ message);
		
		// generating private key
		privateKey = new BigInteger(160, new Random());
		
		// generating public key
		publicKey = EG_Utils.calculPoint(privateKey, P);
	}

	@Override
	public void run() {
		
		System.out.println("ALICE: waiting for generation of Bob's public key...");
		while(EG_Bob.getPublicKey() == null){}
		System.out.println("ALICE: Bob's public key generated. Creating cypher text...");
				
		cypherText = Operations.addition(message, Operations.multiplication(EG_Bob.getPublicKey(), privateKey));
		EG_Bob.setCypheredText(cypherText);
		System.out.println("ALICE: Cipher text created and sent!");
	}

	public static Point getPublicKey(){
		return publicKey;
	}
}
