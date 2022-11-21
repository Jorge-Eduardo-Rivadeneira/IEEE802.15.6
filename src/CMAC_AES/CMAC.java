
package CMAC_AES;

import java.math.BigInteger;

public class CMAC {
    public String CalcCMAC(String Key, String Message, int Salida) throws Exception{
        String zero="00000000000000000000000000000000";
        String Padding="80000000000000000000000000000000";
        double Bsize=16;
        double len1=Message.length();
        double len=len1/2;
        Subkey K=new Subkey();
        BigInteger [] K1_K2=K.GenerateSubkey(Key);
        int numeroMensajes =(int) Math.ceil(len/Bsize);
        int numeroOctSobrantes=(int)len % (int)Bsize;
        int n;
        boolean flag;
        if(numeroMensajes==0){//Comprobación para un mensaje completo
            numeroMensajes=1;
            flag=false;
            n=1;
        }else{
            if((numeroOctSobrantes)==0){//Comprobación para varios mensajes completos
                flag=true;
                n=32;   
            }
            else{//Comprobación para mensajes incompletos
                flag=false;
                n=0;
            }
        }
        String Message_n=Message.substring((numeroMensajes-1)*32,((numeroMensajes-1)*32)+n+numeroOctSobrantes*2);
        BigInteger M_last=new BigInteger(zero,16);
        if(flag==true){//Determinación de ultimo mensaje para mensajes completos
           M_last= new BigInteger(Message_n,16);
           M_last=M_last.xor(K1_K2[0]);
         }
        else{//Determinación de ultimo mensaje para mensajes incompletos
            Message_n=Message_n.concat(Padding.substring(0,(16-(numeroOctSobrantes))*2));
            M_last= new BigInteger(Message_n,16);
            M_last=M_last.xor(K1_K2[1]);
        }
        
        BigInteger X=new BigInteger(zero,16);
        BigInteger Y=new BigInteger(zero,16);
        AES MensajeEncriptado=new AES();
        int aux=0;
        String Xaes="";        
        for(int i=1;i<=numeroMensajes-1;i++){// Algoritmo de iteraciones en CMAC
            String M_i=Message.substring(0+aux,32+aux);
            aux=32*i;
            Y=new BigInteger(M_i,16);
            Y=X.xor(Y);
            M_i=zero.substring(0,32-String.format("%x", Y).length()).concat(String.format("%x", Y));
            Xaes=MensajeEncriptado.EncriptAES(M_i, Key);
            X=new BigInteger(Xaes,16);
        }
        Y=X.xor(M_last);//Operación con ultimo mensaje
        String Y_1=String.format("%x", Y);
        int longMlast=32;
            if (longMlast<Y_1.length()){
                    Y_1 = Y_1.substring(1, Xaes.length()+1);
                 } else if(longMlast>Y_1.length()){
                    Y_1 = "0"+Y_1;                
                 }
        String CMAC=MensajeEncriptado.EncriptAES(Y_1, Key);//Ultima Encripción
        CMAC=CMAC.substring(0,Salida/4);
        return CMAC;
    }
    
}
