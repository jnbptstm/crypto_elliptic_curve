package dh_sts;

import java.math.BigInteger;
import java.util.Random;

import operations.Courbe;
import operations.Point;

public class DHSTS_Alice {

	private Courbe courbe;
	private BigInteger x;
	private BigInteger ka;
	private Point ra; // Public key
	private Point p; // Point de la courbe
	private DHSTS_Bob bob;
	
	public DHSTS_Alice(Courbe courbe, BigInteger x){
		this.courbe = courbe;
		this.x = x;
//		this.p = new Point(x, courbe.value_y(x)[0], false);
		this.ka = new BigInteger(250, new Random());
		assert(ka.compareTo(courbe.getP()) == 1); // ka < p
		/* TODO:
		 * send A (?) and ra to Bob
		 */
	}
	
	public void start(){
		this.bob = DHSTS_Main.bob;
		this.ra = operations.Operations.multiplication(p, ka);
		bob.receiveRaFromAlice(ra);		
	}
	
	public static void main(String args[]){
		
		// Courbe non-supersingulière (donc ordinaire), donc non sujette à l'attaque MOV.
		BigInteger p = new BigInteger("8884933102832021670310856601112383279507496491807071433260928721853918699951");
		BigInteger n = new BigInteger("8884933102832021670310856601112383279454437918059397120004264665392731659049");
		BigInteger a4 =new BigInteger("2481513316835306518496091950488867366805208929993787063131352719741796616329");
		BigInteger a6 = new BigInteger("4387305958586347890529260320831286139799795892409507048422786783411496715073"); 
		BigInteger r4 = new BigInteger("5473953786136330929505372885864126123958065998198197694258492204115618878079");
		BigInteger r6 = new BigInteger("5831273952509092555776116225688691072512584265972424782073602066621365105518");
		BigInteger gx = new BigInteger("7638166354848741333090176068286311479365713946232310129943505521094105356372");
		BigInteger gy = new BigInteger("762687367051975977761089912701686274060655281117983501949286086861823169994");
		BigInteger r = new BigInteger("8094458595770206542003150089514239385761983350496862878239630488323200271273");
		
		// Generating elliptic curve
		Courbe ellipticCurve = new Courbe(p, n, a4, a6, r4, r6, gx, gy, r);
		
//		BigInteger lol = new BigInteger(250, new Random());
//		System.out.println("2^256 = "+ Math.pow(2, 256));
//		System.out.println("2^256 = "+ Math.pow(2, 250));
//		System.out.println("lol= "+ lol.toString());
//		System.out.println("p  = "+ ellipticCurve.getP());
//		System.out.println(lol.compareTo(ellipticCurve.getP()));
	}
	
}
