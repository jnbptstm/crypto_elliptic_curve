

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Model.Courbe;
import Model.Point;

public class Alice {
	
	static Point aP;
	private static int s; //Au hasard entre 1 et n-1
	BigInteger privKey;
	public static Courbe C1;
	
	static Point P;
	static Point pubKey;
	
	//Calcul de A=aP
	public Point firstCalc()
	{
		
		C1 = new Courbe();
		
		P =new Point(C1.get_gx(),C1.get_gy()); 
		
		//s= 1 + (int)(Math.random() * ((C1.get_n().intValue() - 1) + 1));
		s= 1 + (int)(Math.random() * ((10 - 1) + 1));
		
		//s=40;
		
		privKey = BigInteger.valueOf(s);
		
        pubKey= P.multiplyByScalar(P,privKey,C1);
        
       return pubKey;
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
	
	public static void sendKeytoBob() throws NoSuchAlgorithmException
	{
		 Socket socket;
	     BufferedReader in;
	 
	     try 
	     {
	         socket = new Socket(InetAddress.getLocalHost(),2009);   
	         System.out.println("Demande de connexion");
	 
	         in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
	         String message_distant = in.readLine();
	         System.out.println(message_distant);
	         
	         
			////////////////////////////////////////////
			/////// DEBUT DE DSA
			////////////////////////////////////////////
	         
	         
	         //Connexion effectuÃ©e on envoie aP
	         
	         OutputStream Outsocket = socket.getOutputStream();
	         
	         //Choix aléatoire de k entre 1 et n-1
	         
	         int k = 1 + (int)(Math.random() * ((10 - 1) + 1));
	         Point kP = new Point();
             kP =  P.multiplyByScalar(P,BigInteger.valueOf(k),C1);
             
             BigInteger u = kP.getPx().mod(C1.get_n());
             

             
             /////////////////
             MessageDigest md = MessageDigest.getInstance("SHA-1");
             @SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream("MESSAGE.txt");
      
             byte[] dataBytes = new byte[1024];
      
             int nread = 0; 
             while ((nread = fis.read(dataBytes)) != -1) {
               md.update(dataBytes, 0, nread);
             };
             byte[] mdbytes = md.digest();
      
             //convert the byte to hex format method 1
             StringBuffer sb = new StringBuffer();
             for (int i = 0; i < mdbytes.length; i++) {
               sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
             }
             

      
             System.out.println("Hex format : " + sb.toString());
               	
         	////////////////////////////
             //H(m)+su
             String hex = sb.toString();
             BigInteger temp = new BigInteger(hex,16);
             
             System.out.println("Haché : "+temp);
             
             BigInteger temp2=u.multiply(BigInteger.valueOf(s));
             temp = temp.add(temp2);
             temp=temp.multiply(BigInteger.valueOf(k).modInverse(C1.get_n()));
             temp=temp.mod(C1.get_n());
             
             if(!u.equals(null) && !temp.equals(null))
             {
            	 System.out.println("Signature : ("+u+","+temp+")");
             }
             
             ////////
             //ENVOI DE M		
             /////////
             
             //Longueur
             Outsocket.write(intToByteArray(sb.toString().length()));
             
             //Le message
             Outsocket.write(sb.toString().getBytes());
             
             System.out.println("Message envoyé :"+ sb.toString());
             
             //Longueur de u
             Outsocket.write(intToByteArray(u.toByteArray().length));
                          
             //u
             Outsocket.write(u.toByteArray());
             
             //Longueur de v
             Outsocket.write(intToByteArray(temp.toByteArray().length));
             
             //v
             Outsocket.write(temp.toByteArray());
             
             System.out.println("Signature envoyée : ("+u+","+temp+")");
             
             //Envoi longueur XpubKey
             Outsocket.write(intToByteArray(aP.getPx().toByteArray().length));
             
             //Envoi de XpubKey
             Outsocket.write(aP.getPx().toByteArray());
             
             //Envoi longueur YpubKey
             Outsocket.write(intToByteArray(aP.getPy().toByteArray().length));
             
             
             //Envoi YpubKey
             Outsocket.write(aP.getPy().toByteArray());
             
             System.out.println("La signature a été générée avec succès");
             
	         socket.close();
	                
	    }
	    catch (UnknownHostException e) 
	    {
	        e.printStackTrace();
	    }
	    catch (IOException e) 
	    {
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
	
	public static Point MapToPoint(BigInteger x,Courbe C)
	{
		BigInteger y2 = x.pow(3).add(C.get_a4().multiply(x)).add(C.get_a6());
		BigInteger y = sqrt(y2);
		
		return new Point(x,y);
	}
	
	public static int K(Courbe C)
	{
		return 2;
	}
	
	public static void UP() throws NoSuchAlgorithmException
	{
		// Gï¿½nï¿½ration d'une instance d'Alice et calcul de A = aP
		Alice A = new Alice();
		aP=A.firstCalc();
		sendKeytoBob();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		UP();
	}
}
