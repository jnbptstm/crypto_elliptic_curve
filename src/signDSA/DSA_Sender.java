package signDSA;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import main.Main;
import operations.Courbe;
import operations.Operations;
import operations.Point;

public class DSA_Sender {

	private Courbe ec;
	public Point Q; // Clé publique.
	private BigInteger s; // Clé privée.
	private Point G;	
	
	public DSA_Sender(){
		ec = Main.ellipticCurve;
		s = new BigInteger(160, new Random());
		G = new Point(ec.getGx(), ec.getGy(), false);		
		Q = Operations.multiplication(G, s);
	}

	//return le point qui code le message
	public Point sign(String message){
		BigInteger x;
		BigInteger k;
		BigInteger y;
		BigInteger p = ec.getP();
		byte[] messageb = message.getBytes();
		byte[] hash;
		
		do{
			hash = null;
			k = new BigInteger(160, new Random());	
			Point IJ = Operations.multiplication(G, k);
			x = IJ.getX().mod(p);
			
			try { 
				hash = MessageDigest.getInstance("SHA-1").digest(messageb); //MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
			} 
			catch (NoSuchAlgorithmException e) { throw new Error("no SHA1 support in this VM"); }
			catch (Exception e) { e.printStackTrace(); }
			
			y = (new BigInteger(hash).add(s.multiply(x).mod(p)).mod(p)).multiply(k.modInverse(p)).mod(p);
			
		}while (y.equals(BigInteger.ZERO) || x.equals(BigInteger.ZERO));
		
		return new Point(x, y, false);
	}
	
	public Point getQ() {
		return Q;
	}

	public void setQ(Point q) {
		Q = q;
	}
}
