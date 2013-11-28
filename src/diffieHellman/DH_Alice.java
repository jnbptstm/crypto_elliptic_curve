package diffieHellman;

import java.math.BigInteger;
import java.util.Random;

import main.Main;
import operations.Point;

public class DH_Alice implements Runnable{
	
	private static Point B = null; // Bob's public key
	
	private Point generator = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
	private BigInteger privateKey;
	private Point publicKey;
	private Point secretKey;
	
	public DH_Alice(){
	}
	
	@Override
	public void run(){
		
		System.out.println("ALICE: generating private key...");
		privateKey = new BigInteger(160, new Random());
		
		System.out.println("ALICE: private key generated. Calculating public key...");
		publicKey = DH_Utils.calculPoint(this.privateKey, generator);
		
		System.out.println("ALICE: sending public key to Bob...");
		DH_Utils.envoyerDeAaB(publicKey);
		
		System.out.println("ALICE: public key sent to Bob. Waiting for Bob's public key...");
		while(DH_Alice.getB() == null){}
		
		System.out.println("ALICE: Bob's public key received. Computing secret key...");
		secretKey = DH_Utils.calculPoint(this.privateKey, B);
		
		System.out.println("ALICE: secret key is "+ secretKey);
	}

	public static synchronized Point getB() {
		return B;
	}

	public Point getGenerator() {
		return generator;
	}

	public BigInteger getPrivateKey() {
		return privateKey;
	}

	public Point getPublicKey() {
		return publicKey;
	}

	public Point getClefSecrete() {
		return secretKey;
	}
	
	public static synchronized void setB(Point b) {
		B = b;
	}
}
