package diffieHellman;

/*************************************************
 * Diffie-Hellman algorithm using Elliptic Curve *
 *************************************************/
/*
 * 1. Alice and Bob 
 * 
 * 
 */

public class DH_Main {

	public static DH_Alice dh_Alice;
	public static DH_Bob dh_Bob;
	
	public static void main(String[] args){
		
		Thread t_alice = new Thread(new DH_Alice());
		Thread t_bob = new Thread(new DH_Bob());
				
		t_bob.start();
		t_alice.start();
	}
}
