package ECC;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.ECNamedCurveTable;

public class StandardCurve extends Curve{

	private BigInteger sk;
	private String id;
	private String curve_name;
	
	public StandardCurve(String ID){
		super(ID);	
		this.id=ID;
	
	}
	
	
	public BigInteger [] getPublicKey(BigInteger SK) {
		this.sk=SK;
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
		 PKVerificationwithAPI();
                 BigInteger [] Pkreturn={Px,Py};
                 return Pkreturn;
	}
	
	
	public String PKVerificationwithAPI(){
		if(id.compareTo("P-256")==0){
			curve_name="secp256r1";
		}else if(id.compareTo("P-192")==0){
			curve_name="secp192r1";
		}else if(id.compareTo("P-224")==0){
			curve_name="secp224r1";
		}else if(id.compareTo("P-384")==0){
			curve_name="secp384r1";
		}		
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(curve_name);
            ECPoint G = ecSpec.getG();
            ECPoint Q = G.multiply(sk);
            ECPoint P=Q.multiply(BigInteger.valueOf(1));
            BigInteger PX = P.getAffineXCoord().toBigInteger();
            BigInteger PY = P.getAffineYCoord().toBigInteger();
            return "Public Key PK:"+"\n"+"PKx: "+String.format("%x", PX) + "\n"+"PKy: "+String.format("%x", PY);
                
        }
        
	public BigInteger getRandomSK(){
		Random rnd = new SecureRandom();
		BigInteger r;
		BigInteger private_key= new BigInteger("1");;
	
		if (this.id.compareTo("P-256")==0){
			r= new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369");
			private_key = r;
			do {
	    		private_key = new BigInteger(256,rnd);
				}
				while (private_key.compareTo(r) == 1 || private_key.compareTo(r) == 0 || private_key.bitLength()!=256);
			
		} else if (this.id.compareTo("P-192")==0){
			r= new BigInteger("6277101735386680763835789423176059013767194773182842284081");
			private_key = r;
			do {
	    		private_key = new BigInteger(192,rnd);
				}
				while (private_key.compareTo(r) == 1 || private_key.compareTo(r) == 0 || private_key.bitLength()!=192);
			
		} else if (this.id.compareTo("P-224")==0){
			r= new BigInteger("26959946667150639794667015087019625940457807714424391721682722368061");
			private_key = r;
			do {
	    		private_key = new BigInteger(224,rnd);
				}
				while (private_key.compareTo(r) == 1 || private_key.compareTo(r) == 0 || private_key.bitLength()!=224);
			
		} else if (this.id.compareTo("P-384")==0){
			r= new BigInteger("39402006196394479212279040100143613805079739270465446667946905279627659399113263569398956308152294913554433653942643");
			private_key = r;
			do {
	    		private_key = new BigInteger(384,rnd);
				}
				while (private_key.compareTo(r) == 1 || private_key.compareTo(r) == 0 || private_key.bitLength()!=384);
		}	
		return private_key;
	} 
}
