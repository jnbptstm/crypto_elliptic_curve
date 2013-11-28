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
}
