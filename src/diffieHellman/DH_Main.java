package diffieHellman;

/*************************************************
 * Diffie-Hellman algorithm using Elliptic Curve *
 *************************************************/
/*
 * 1. Alice and Bob generate their key pair:
 *      a. Random private key (da for Alice, db for Bob) between 1 and p.
 *      b. Public key (Qa = da.P for Alice, Qb = db.P for Bob).
 * 
 * 2. Alice computes (xk, yk) = da.Qb and Bob computes (xk, yk) = db.Qa
 * xk is the shared secret.
 */

public class DH_Main {

	public static DH_Alice dh_Alice;
	public static DH_Bob dh_Bob;
	
	public static void main(String[] args){
		
		Thread t_alice = new Thread(new DH_Alice());
		Thread t_bob = new Thread(new DH_Bob());
				
		t_bob.start();
		t_alice.start();
		
		try{
			t_bob.join();
			t_alice.join();
		}catch(InterruptedException e){e.printStackTrace();}
	}
}
