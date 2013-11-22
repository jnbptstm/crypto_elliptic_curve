package dh_sts;

import java.math.BigInteger;
import java.util.Random;

import operations.Courbe;
import operations.Operations;
import operations.Point;

public class DHSTS_Bob {
	
	private Courbe courbe;
	private BigInteger x;
	private BigInteger kb;
	private Point rb; // Public key
	private Point p; // Point de la courbe
	private Point sharedSecret;
	private DHSTS_Alice alice;
	private String concatenates;
	
	public DHSTS_Bob(Courbe courbe, BigInteger x){
		this.courbe = courbe;
		this.x = x;
		this.p = new Point(x, courbe.value_y(x)[0], false);
		this.kb = new BigInteger(250, new Random());
		assert(kb.compareTo(courbe.getP()) == 1); // ka < p
		this.rb = operations.Operations.multiplication(p, kb);
	}
	
	public void receiveRaFromAlice(Point ra){
		this.sharedSecret = operations.Operations.multiplication(ra, kb);
		// Bob concatenates (rb, ra).
		this.concatenates = operations.Operations.addition(rb, ra).toString();
	}
}
