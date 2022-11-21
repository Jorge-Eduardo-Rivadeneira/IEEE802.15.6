
package CMAC_AES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.IllegalBlockSizeException;
 
public class AES {
    public String EncriptAES(String Mensaje,String Key) throws Exception{
        if(Key.length()==31){//Verificacion de longitud de llaves
            Key="0"+Key;    
        }else if(Key.length()==33){
            Key=Key.substring(1,33);
        }
        byte[] BytesKey = DatatypeConverter.parseHexBinary(Key);
        SecretKeySpec secretKeySpec1;
        secretKeySpec1 = new SecretKeySpec(BytesKey, "AES");//Creación de llave secreta
        byte[] cipherText = encryptText(Mensaje, secretKeySpec1);
        return bytesToHex(cipherText);
    }
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{

        Cipher aesCipher = Cipher.getInstance("AES/CBC/NoPadding");//Encripción en modo CBC-noPadding
        String Key = "3736353461363AA53334353736353233";
        byte[] newIv = DatatypeConverter.parseHexBinary(Key);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(newIv);
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey,ivParameterSpec);//Iniciación del encriptador
        byte[] bytes = DatatypeConverter.parseHexBinary(plainText);
        byte[] byteCipherText = aesCipher.doFinal(bytes);//Encripción
        return byteCipherText;
         
    }
        private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);

     }
}
