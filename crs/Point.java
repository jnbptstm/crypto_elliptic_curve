package Model;

import java.math.BigInteger;

public class Point {
	
	private BigInteger px,py;
	public boolean isInfinite;
	
	public Point(BigInteger x, BigInteger y)
	{
		px=x;
		py=y;
		isInfinite = false;
	}

	public Point() {
		isInfinite = false;
	}

	public BigInteger getPx() {
		return px;
	}

	public void setPx(BigInteger px) {
		this.px = px;
	}

	public BigInteger getPy() {
		return py;
	}

	public void setPy(BigInteger py) {
		this.py = py;
	}
	
	// Teste si un point this est sur la courbe C
	
	public boolean isOnTheCurve(Courbe C)
	{
		BigInteger a4x = C.get_a4().multiply(this.getPx());
		BigInteger x3 = this.getPx().pow(3);
		BigInteger y2 = this.getPy().pow(2);
		
		if(y2.mod(C.get_p()).equals(x3.add(a4x).add(C.get_a6()).mod(C.get_p())))
			return true;
		else
			return false;
	}
	
	//Renvoi l'opposé de p sur la courbe C
	
	public Point Opposite(Point p,Courbe C)
	{
		BigInteger xq,yq;
		
		xq=p.getPx();
		yq=C.get_p().subtract(p.getPy());
		
		return new Point(xq,yq);
	}
	

	
	public boolean isEqual(Point P) 
	{
		if(this.getPx().equals(P.getPx()) && this.getPy().equals(P.getPy()))
			return true;
		else
			return false;
	}

	// Algorithme d'addition de pp + qq sur C (cf le cours)
	
	public Point add(Point pp, Point qq, Courbe C)
	{
		Point res = new Point();
		BigInteger lambda = new BigInteger("0");
		
		if((pp.isInfinite && !qq.isInfinite) || (!pp.isInfinite && qq.isInfinite))
		{
			return pp;
		}
		else if(pp.isInfinite && qq.isInfinite)
		{
			Point x = new Point(BigInteger.ZERO,BigInteger.ZERO);
			x.setInfinite(true);
			return x;
		}
		
		if(pp.getPx().equals(Opposite(qq,C).getPx()) && pp.getPy().equals(Opposite(qq,C).getPy()))
		{
			Point x = new Point(BigInteger.ZERO,BigInteger.ZERO);
			x.setInfinite(true);
			return x;
		}
		
		if(!(pp.getPx().equals(qq.getPx())))
		{
			BigInteger temp = pp.getPx().subtract(qq.getPx()).modInverse(C.get_p());
			lambda = (pp.getPy().subtract(qq.getPy())).multiply(temp);
			
		}
		else if(!(pp.getPx().equals(BigInteger.ZERO) && pp.getPy().equals(BigInteger.ZERO) && qq.getPx().equals(BigInteger.ZERO) && qq.getPy().equals(BigInteger.ZERO)))
		{
			BigInteger tempx2;
			tempx2=pp.getPx().pow(2);
			BigInteger temp = pp.getPy().multiply(new BigInteger("2")).modInverse(C.get_p());
			lambda = ((tempx2.multiply(new BigInteger("3"))).add(C.get_a4())).multiply(temp);
			
		}
		
		res.setPx(lambda.pow(2).subtract(pp.getPx()).subtract(qq.getPx()).mod(C.get_p()));
		res.setPy(res.getPx().multiply(lambda).negate().add(lambda.multiply(pp.getPx())).subtract(pp.getPy()).mod(C.get_p()));
		
		return res;
	}
	
	//Algorithme de doublement d'un point
	
	public Point doublement(Point p,Courbe C){
		return add(p, p,C);
	}
	
	//Algorithme de multiplication d'un point par un scalaire (cf le cours)
	
	public Point multiplyByScalar(Point p, BigInteger n,Courbe C)
	{
		Point R =p;
	    int length = n.bitLength();
	    byte[] binarray = new byte[length];
	    
	    for(int i=0;i<=length-1;i++)
	    {
	        binarray[i] = n.mod(BigInteger.valueOf(2)).byteValue();
	        n = n.divide(BigInteger.valueOf(2));
	    }

	    for(int i = length - 2;i >= 0;i--)
	    {
	        R = doublement(R,C);
	        if(binarray[i]== 1) 
	            R = R.add(R,p,C);
	    }
	    
	    return R;
	}

	@Override
	public String toString()
	{
		Courbe C = new Courbe();
		return "("+this.getPx().mod(C.get_p())+","+this.getPy().mod(C.get_p())+")";
	}
	
	// Fonction de Calcul de l'ordre d'un Point
	
	public int ordPoint(int [][]decompo,Courbe C)
	{
		int m=0;
		
		Point Q=new Point();
		
		// Le tableau decompo contient la dÃ©composition de #E
		
		for(int i=0;i<decompo.length;i++)
		{
			for(int j=0;j<decompo[i].length;j++)
			{
				m*=Math.pow((double)decompo[i][0], (double)decompo[i][1]);
			}
		}
		
		int r=decompo.length;
		
		for(int i=1;i<r;i++)
		{
			m/=Math.pow((double)decompo[i][0],(double)decompo[i][1] );
			
			Q=this.multiplyByScalar(this,BigInteger.valueOf(m), C);
			
			while(Q.getPx() != new BigInteger("0") || Q.getPy() != new BigInteger("0"))
			{
				Q=Q.multiplyByScalar(this,BigInteger.valueOf(decompo[i][0]), C);
				m*=decompo[i][0];
			}
			
			
		}
		return m;
	}

	public boolean isInfinite() {
		return isInfinite;
	}

	public void setInfinite(boolean isInfinite) {
		this.isInfinite = isInfinite;
	}
	
}
