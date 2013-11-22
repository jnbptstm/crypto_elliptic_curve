package signDSA;

import operations.Point;

public class DSA_Main {

	public static String message;
	public static DSA_Sender send;
	public static Point signature;
	public static Point clefpub;
	public static DSA_Verifier rec;
	public static boolean match;
	
	//public static Point p = new Point(Main.ellipticCurve.getGx(), Main.ellipticCurve.getGy(), false);
	
	public static void main(String args[]){
		
		message = "Top secret";
		send = new DSA_Sender();
		signature = send.sign(message);
		System.out.println("Signature: "+ signature);
		
		clefpub = new Point(send.getQ());
		
		rec = new DSA_Verifier(clefpub);
		match = rec.verif(message, signature);
		
		System.out.println(match);
	}
}
