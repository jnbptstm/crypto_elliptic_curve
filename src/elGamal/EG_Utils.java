package elGamal;

import java.math.BigInteger;

import operations.Operations;
import operations.Point;

public class EG_Utils {

	public static Point calculPoint(BigInteger b, Point p) {
		return Operations.multiplication(p, b);
	}
	
	public static Point addPoint(Point b, Point p) {
		return Operations.addition(p, b);
	}

	public static void envoyerDeAaB(Point a, Point b){
		EG_Bob.setA(a);
		EG_Bob.setB(b);
	}
	
	public static void envoyerDeBaA(Point B){
		//EG_Alice.setB(B);
	}
}
