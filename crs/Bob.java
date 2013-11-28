
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.asn1.x9.X9ECParameters;

import Model.Courbe;
import Model.Point;

public class Bob {
	
	static Point bP;
	static Courbe C1;
	static Point P;
	static X9ECParameters ecParams;
	
	//Calcul de B=bP
	public Point firstCalc()
	{
		C1 = new Courbe();
				
		P =new Point(C1.get_gx(),C1.get_gy());
		
		return P;
	}
	
	public static int byteArrayToInt(byte[] encodedValue) {
	    int index = 0;
	    int value = encodedValue[index++] << Byte.SIZE * 3;
	    value ^= (encodedValue[index++] & 0xFF) << Byte.SIZE * 2;
	    value ^= (encodedValue[index++] & 0xFF) << Byte.SIZE * 1;
	    value ^= (encodedValue[index++] & 0xFF);
	    return value;
	}
	
	public static byte[] intToByteArray(int value) {
	    int index = 0;
	    byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
	    encodedValue[index++] = (byte) (value >> Byte.SIZE * 3);
	    encodedValue[index++] = (byte) (value >> Byte.SIZE * 2);   
	    encodedValue[index++] = (byte) (value >> Byte.SIZE);   
	    encodedValue[index++] = (byte) value;
	    return encodedValue;
	}
	
	public static void receptKeyfromAlice() throws NoSuchAlgorithmException
	{
		ServerSocket socketserver  ;
        Socket socketduserveur ;
        PrintWriter out;
        byte []lgrecept=new byte[4];
        byte []recept;
         
	     C1=new Courbe();
	     
        try 
        {
			////////////////////////////////////////////
			/////// DEBUT DU PROTOCOLE DIFFIE-HELMANN
			////////////////////////////////////////////

        	socketserver = new ServerSocket(2009);
            socketduserveur = socketserver.accept(); 
            out = new PrintWriter(socketduserveur.getOutputStream());
            out.println("Vous Ãªtes connectÃ© zÃ©ro !");
            out.flush();
            
            java.io.InputStream inp =socketduserveur.getInputStream();
            
            //Récupération de la longueur du message
            inp.read(lgrecept);
            
            recept=new byte[byteArrayToInt(lgrecept)];
            
            inp.read(recept);
            
            System.out.println("Message reçu : "+new String(recept));
            
            //Récupération de la longueur de u
            inp.read(lgrecept);
            
            recept=new byte[byteArrayToInt(lgrecept)];
            
            inp.read(recept);
            
            BigInteger u = new BigInteger(recept);
            
            //Récupération de la longueur de v
            inp.read(lgrecept);
            
            recept=new byte[byteArrayToInt(lgrecept)];
            
            inp.read(recept);
            
            BigInteger v = new BigInteger(recept);
            
            System.out.println("Signature reçue : ("+u+","+v+")");
            
            int etapverif=0;
            
            if(C1.get_n().subtract(u).compareTo(BigInteger.ZERO) >0 && C1.get_n().subtract(v).compareTo(BigInteger.ZERO) >0)
            {
            	System.out.println("Premier test OK");
            	etapverif++;
            }
            else
            {
            	System.out.println("Premier test FAIL");
            }
            
            //Recuperation de la clé publique d'Alice
            
            //Récupération de la longueur de XPubKey
            inp.read(lgrecept);
            
            recept=new byte[byteArrayToInt(lgrecept)];
            
            inp.read(recept);
            
            BigInteger x = new BigInteger(recept);
            
            //Récupération de la longueur de YPubKey
            inp.read(lgrecept);
            
            recept=new byte[byteArrayToInt(lgrecept)];
            
            inp.read(recept);
            
            BigInteger y = new BigInteger(recept);
            
            Point pubKeyAl = new Point(x,y);
                            
            /////////Calcul 2e test
           
            /////////////////
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            @SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream("MESSAGE.txt");
     
            byte[] dataBytes = new byte[1024];
     
            int nread = 0; 
            
            while ((nread = fis.read(dataBytes)) != -1) 
            {
              md.update(dataBytes, 0, nread);
            };
            byte[] mdbytes = md.digest();
     
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            
            for (int i = 0; i < mdbytes.length; i++) 
            {
              sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
                       	
            String hex = sb.toString();
            BigInteger temp = new BigInteger(hex,16);
            
            System.out.println("Haché : "+temp);

            
            //vérifier que u = x mod n sachant que (x, y) = (H(m)/v mod n)P + (u/vmod n)Q.
            
            BigInteger cal1 = temp.multiply(v.modInverse(C1.get_n())).mod(C1.get_n());
            Point P1 = P.multiplyByScalar(P, cal1, C1);
            
            
            BigInteger cal2 = u.multiply(v.modInverse(C1.get_n())).mod(C1.get_n());
            Point P2 = P.multiplyByScalar(pubKeyAl, cal2, C1);
            
            
            Point res = P1.add(P1, P2, C1);
            
            if(u.equals(res.getPx().mod(C1.get_n())))
            {
            	System.out.println("Deuxième test OK");
            	etapverif++;
            }
            else
            {
            	System.out.println("Deuxième test FAIL");
            }
            
            
            if(!pubKeyAl.getPx().equals(BigInteger.ZERO) && !pubKeyAl.getPy().equals(BigInteger.ZERO) && pubKeyAl.isOnTheCurve(C1))
            {
            	System.out.println("Troisième test OK");
            	etapverif++;
            }
            else
            {
            	System.out.println("Troisième test FAIL");
            }
            
            if(pubKeyAl.multiplyByScalar(pubKeyAl, C1.get_n(), C1).isInfinite() == true)
            {
            	System.out.println("Quatrième test OK");
            	etapverif++;
            }
            else
            {
            	System.out.println("Quatrième test FAIL");
            }
            
            if(etapverif == 4)
            {
            	System.out.println("La signature a été vérifiée avec succès");
            }
            
            socketduserveur.close();
            socketserver.close();
                 
        }catch (IOException e) {
             
            e.printStackTrace();
        }
    }
	
	public static BigInteger sqrt(BigInteger n) 
	{
		  BigInteger a = BigInteger.ONE;
		  BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		  
		  while(b.compareTo(a) >= 0) 
		  {
		    BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
		    if(mid.multiply(mid).compareTo(n) > 0) 
		    	b = mid.subtract(BigInteger.ONE);
		    else 
		    	a = mid.add(BigInteger.ONE);
		  }
		  return a.subtract(BigInteger.ONE);
	}
	
	public static Point MapToPoint(BigInteger x)
	{
		Courbe C = new Courbe();
		
		BigInteger y2 = x.pow(3).add(C.get_a4().multiply(x)).add(C.get_a6());
		BigInteger y = sqrt(y2);
		
		return new Point(x,y);
	}
	
	public static int K(Courbe C)
	{
		return 1 + (int)(Math.random() * ((C.get_p().intValue() - 1) + 1));
	}
	
	public static void UP() throws NoSuchAlgorithmException
	{
		// Gï¿½nï¿½ration d'une instance de Bob et calcul de B = bP
		Bob B = new Bob();
		bP=B.firstCalc();
		receptKeyfromAlice();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		UP();
	}
}
