package ECC;
import java.math.BigInteger;

public class  MCurve extends Curve {
	private boolean state;
	public MCurve(BigInteger a,BigInteger b, BigInteger p, BigInteger Gx, BigInteger Gy){
		super(a,b,p,Gx,Gy);		
	}
	
	public boolean checkCurve(){
		if ((((a.pow(3)).multiply(BigInteger.valueOf(4))).add((b.pow(2)).multiply(BigInteger.valueOf(27)))).mod(p)!=BigInteger.ZERO){
			state=true;
		}else{
			state=false;		
		}	
		return state;
	}
	
	public BigInteger [] getPublicKey(BigInteger SK) {
		checkCurve();
		if (state==true){
			BigInteger num,den,den_inv,lamda,Px,Py;	
			Px=this.Gx;
			Py=this.Gy;
			BigInteger t = new BigInteger("1000"); 
			BigInteger [] m= new BigInteger[t.intValue()];
			BigInteger ans=new BigInteger("0");
			int i=1;
			m[0]=SK;
			while(ans.compareTo(BigInteger.valueOf(1))!=0){	    
			    	if (SK.remainder(BigInteger.valueOf(2))==BigInteger.ZERO){	   
			    		SK=SK.divide(BigInteger.valueOf(2));
			    		ans=SK;
			    		m[i]=SK;
			    		i++;
			    	}else{
			    		SK=SK.subtract(BigInteger.valueOf(1));    
			    		m[i]=SK;
			    		i++;    		
			    	}
			 }
			 int j=i;
			 BigInteger [] n= new BigInteger[j];
			 for (j=0;j<i;j++){
				 n[j]=m[i-1-j];
			 }
			 for (j=1;j<i;j++){
			    	if (n[j].remainder(BigInteger.valueOf(2))==BigInteger.ZERO){
			    		num=((Px.pow(2)).multiply(BigInteger.valueOf(3)).add(a)).mod(p);
			    		den=((Py.multiply(BigInteger.valueOf(2)))).mod(p);
			    		den_inv = den.modInverse(p);
			    		lamda= (den_inv.multiply(num)).mod(p);
			    		BigInteger Px_aux=Px;
			    		BigInteger Py_aux=Py;
			    		Px=(((lamda.pow(2)).subtract(Px_aux)).subtract(Px_aux)).mod(p);
			    		Py=((lamda.multiply(Px_aux)).subtract(Py_aux)).subtract((lamda.multiply(Px))).mod(p);    			    		
			    	}else{
			    		num=(Py.subtract(Gy)).mod(p);
			    		den=(Px.subtract(Gx)).mod(p);
			    		den_inv=den.modInverse(p);
			    		lamda= (den_inv.multiply(num)).mod(p);
			    		BigInteger Px_aux=Px;
			    		Px=(((lamda.pow(2)).subtract(Px_aux)).subtract(Gx)).mod(p);
			    		Py=(((lamda.multiply(Gx)).subtract(lamda.multiply(Px))).subtract(Gy)).mod(p);	    		
			    	}	    	
			    }
                    BigInteger [] Pkreturn={Px,Py};
                    return Pkreturn;
		}else{
                    BigInteger [] Pkreturn={new BigInteger("1")};
                    return Pkreturn;
		}
	}
}
