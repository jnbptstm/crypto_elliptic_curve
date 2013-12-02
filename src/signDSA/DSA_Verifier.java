package signDSA;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import main.Main;
import operations.*;

public class DSA_Verifier {

	private BigInteger p;
	public Point Q; // cle publique d'Alice

	private Courbe ec;
	private Point G;
	
	public DSA_Verifier(Point Q){
		
		ec = Main.ellipticCurve;
		p = ec.getP();
		
		G = new Point(ec.getGx(), ec.getGy(), false); // Point appartenant a la courbe elliptique.
		
		this.setQ(Q);
		
		
		//recup et verif la clef pub de alice
//		try {
//			DSA_Sender.class.getDeclaredField("Q");
//		} 
//		catch (SecurityException e) {e.printStackTrace();} 
//		catch (NoSuchFieldException e) {e.printStackTrace();}
		
		if (Q.isElementNeutre()) System.out.println("ERROR, Q = (0, 0)");
		if (Q.getIsInfinite()) System.out.println("ERROR, Q point � l'infini");
		if (!(Operations.multiplication(Q, p).isElementNeutre())) System.out.println("ERROR, nQ != (0, 0)");
		
		System.out.println("nQ: "+ Operations.multiplication(Q, p));
	}
	
	public boolean verif(String message, Point sign){
		
		System.out.println("Verification de la signature...");
		
		if ((sign.getX().compareTo(p.subtract(BigInteger.ONE)) > 0) || (sign.getX().compareTo(BigInteger.ONE) < 0 )) 
		{
			System.out.println("ERROR: 'u' n'est pas comris entre 1 et "+ p);
			return false;
		}
		if ((sign.getY().compareTo(p.subtract(BigInteger.ONE)) > 0) || (sign.getY().compareTo(BigInteger.ONE) < 0)) 
		{
			System.out.println("ERROR: 'v' n'est pas compris entre 1 et "+ p);
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
		
		Point Test1 = Operations.multiplication(G, (new BigInteger(hash).mod(p).multiply(sign.getY().modInverse(p))));
		Point Test2 = Operations.multiplication(Q, sign.getX().multiply(sign.getY().modInverse(p)).mod(p));
		Point Test = Operations.addition(Test1, Test2);
		System.out.println("u: "+ sign.getX().mod(p) +"\nx: "+ Test.getX().mod(p));
		System.out.println("v: "+ sign.getY().mod(p) +"\ny: "+ Test.getY().mod(p));
		
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
		return new Point(Q);
	}

	public void setQ(Point q) {
		Q = new Point(q);
	}
}
