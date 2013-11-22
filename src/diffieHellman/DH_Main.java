package diffieHellman;

import java.math.BigInteger;

import main.Main;

public class DH_Main {

	public static BigInteger x;
	public static DH_Alice dh_Alice;
	public static DH_Bob dh_Bob;
	
	public static void main(String[] args){
		
		x = BigInteger.ONE;
		Thread t_alice = new Thread(new DH_Alice(Main.ellipticCurve, Main.ellipticCurve.getGx()));
		Thread t_bob = new Thread(new DH_Bob(Main.ellipticCurve, Main.ellipticCurve.getGx()));
				
		t_bob.start();
		t_alice.start();
	}
}
