package dh_sts;

import java.math.BigInteger;

import main.Main;

public class DHSTS_Main {

	public static BigInteger x;
	public static DHSTS_Alice alice;
	public static DHSTS_Bob bob;
	
	public static void main(String args[]){
		
		BigInteger x =  BigInteger.ONE;
		
		alice = new DHSTS_Alice(Main.ellipticCurve, x);
		bob = new DHSTS_Bob(Main.ellipticCurve, x);
		
		alice.start();
	}
}
