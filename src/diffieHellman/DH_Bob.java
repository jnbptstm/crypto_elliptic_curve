package diffieHellman;

import java.math.BigInteger;
import java.util.Random;

import main.Main;
import operations.Point;

/**
 * See algorithm in 'DH_Main.java'.
 */

public class DH_Bob implements Runnable{

	private static Point A = null; // Alice's public key
	
	private Point generator = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
	private BigInteger privateKey;
	private Point publicKey;
	private Point secretKey;

	public DH_Bob(){
	}
	
	@Override
	public void run(){
		
		System.out.println("BOB: generating private key...");
		this.privateKey = new BigInteger(160, new Random());
		
		System.out.println("BOB: private key generated. Calculating public key...");
		publicKey = DH_Utils.calculPoint(this.privateKey, generator);

		
		System.out.println("BOB: sending public key to Alice...");
		DH_Utils.envoyerDeBaA(publicKey);
		
		System.out.println("BOB: public key sent. Waiting for Alice's public key...");
		while(DH_Bob.getA() == null){}
		
		System.out.println("BOB: public key received. Computing secret key...");
		secretKey = DH_Utils.calculPoint(this.privateKey, A);
		
		System.out.println("BOB: secret key is "+ secretKey);
	}

	public static synchronized Point getA() {
		return A;
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
	
	public static void setA(Point a) {
		A = a;
	}
}
