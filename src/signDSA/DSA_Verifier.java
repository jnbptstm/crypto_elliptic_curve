package signDSA;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import main.Main;
import operations.*;

public class DSA_Verifier {

	private BigInteger n;
	public Point Q; // clé publique d'Alice

	private Courbe ec;
	private Point G;
	
	public DSA_Verifier(Point Q){
		
		ec = Main.ellipticCurve;
		n = ec.getP();
		
		G = new Point(ec.getGx(), ec.getGy(), false); // Point appartenant à la courbe elliptique.
		
		this.setQ(Q);
		
		
		//recup et verif la clef pub de alice
//		try {
//			DSA_Sender.class.getDeclaredField("Q");
//		} 
//		catch (SecurityException e) {e.printStackTrace();} 
//		catch (NoSuchFieldException e) {e.printStackTrace();}
		
		if (Q.isElementNeutre()) System.out.println("ERROR, Q = (0, 0)");
		if (Q.getIsInfinite()) System.out.println("ERROR, Q point à l'infini");
		if (!(Operations.multiplication(Q, n).isElementNeutre())) System.out.println("ERROR, nQ != (0, 0)");
		
		System.out.println("nQ: "+ Operations.multiplication(Q, n));
	}
	
	public boolean verif(String message, Point sign){
		
		System.out.println("Verification de la signature...");
		
		if ((sign.getX().compareTo(n.subtract(BigInteger.ONE)) > 0) || (sign.getX().compareTo(BigInteger.ONE) < 0 )) 
		{
			System.out.println("ERROR: 'u' n'est pas comris entre 1 et "+ ec.getP());
			return false;
		}
		if ((sign.getY().compareTo(n.subtract(BigInteger.ONE)) > 0) || (sign.getY().compareTo(BigInteger.ONE) < 0)) 
		{
			System.out.println("ERROR: 'v' n'est pas compris entre 1 et "+ ec.getP());
			return false;
		}

		byte[] hash = null; 
		byte[] messageb = message.getBytes();
		
		try 
		{ 
			hash = MessageDigest.getInstance("SHA-1").digest(messageb); //MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
		} 
		catch (NoSuchAlgorithmException e) { throw new Error("no SHA1 support in this VM"); }
		catch (Exception e) { e.printStackTrace(); }
		
		BigInteger yinv = sign.getY().modInverse(n);
		
		Point Test1 = Operations.multiplication(G, (new BigInteger(hash)).multiply(yinv).mod(n));
		Point Test2 = Operations.multiplication(Q, sign.getX().multiply(yinv).mod(n));
		Point Test = Operations.addition(Test1, Test2);
		System.out.println("u: "+ sign.getX().mod(n) +"\nx: "+ Test.getX().mod(n));
		System.out.println("v: "+ sign.getY().mod(n) +"\ny: "+ Test.getY().mod(n));
		
//		System.out.println(Operations.multiplication(Test, n));
		
		if(Test.getX().equals(sign.getX())){
			System.out.println("VERIF OK: x = i mod n");
			return true;
		}
		else{
			System.out.println("ERROR: x != i mod n");
			return false;
		}
	}
	
	public Point getQ() {
		return Q;
	}

	public void setQ(Point q) {
		Q = new Point(q);
	}
}
