package diffieHellman;

import java.math.BigInteger;

import operations.Point;

public class DH_Utils {

	public static synchronized Point calculPoint(BigInteger b, Point p) {
		return operations.Operations.multiplication(p, b);
	}

	public static void envoyerDeAaB(Point A){
		DH_Bob.setA(A);
	}
	
	public static void envoyerDeBaA(Point B){
		DH_Alice.setB(B);
	}
}
