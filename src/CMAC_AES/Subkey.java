
package CMAC_AES;
import java.math.BigInteger;

public class Subkey {
    public BigInteger [] GenerateSubkey(String K) throws Exception{
        AES MensajeEncriptado=new AES();
        String zero="00000000000000000000000000000000";
        String Rb1="00000000000000000000000000000087";
        BigInteger Rb=new BigInteger(Rb1,16);
        BigInteger K1;
        BigInteger K2;
        String L1=MensajeEncriptado.EncriptAES(zero, K);
        BigInteger L = new BigInteger(L1, 16);
        boolean msbL=L.testBit(0);
        if(msbL==true){//Comprobacion para subllave K1
            K1=L.shiftLeft(1);
            String K1_1=String.format("%x", K1);
                if (L1.length()<K1_1.length()){
                    K1_1 = K1_1.substring(1, L1.length()+1);
                    K1=new BigInteger(K1_1,16);
                 } 
        }else{
            K1=(L.shiftLeft(1)).xor(Rb);
            String K1_1=String.format("%x", K1);
                if (L1.length()<K1_1.length()){
                    K1_1 = K1_1.substring(1, L1.length()+1);
                    K1=new BigInteger(K1_1,16);
                 } 
        }
        boolean msbK1=K1.testBit(0);
        if(msbL==true){//Comprobación para subllave K2
            K2=K1.shiftLeft(1);
            String K2_2=String.format("%x", K2);
                if (L1.length()<K2_2.length()){
                    K2_2 = K2_2.substring(1, L1.length()+1);
                    K2=new BigInteger(K2_2,16);
                 } 
        }else{
            K2=(K1.shiftRight(1)).xor(Rb);
            String K2_2=String.format("%x", K2);
                if (L1.length()<K2_2.length()){
                    K2_2 = K2_2.substring(1, L1.length()+1);
                    K2=new BigInteger(K2_2,16);
                 } 
        }
        
         BigInteger [] Subkeyreturn={K1,K2};
         return Subkeyreturn;
    }
}
