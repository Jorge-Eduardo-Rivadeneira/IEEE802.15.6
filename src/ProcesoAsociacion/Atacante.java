
package ProcesoAsociacion;

import ECC.MCurve;
import ECC.StandardCurve;
import CMAC_AES.CMAC;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;
import Tramas.CuartoIack;
import Tramas.PrimerIack;
import Tramas.SegundaTramaAsociacion;
import Tramas.TerceraTramaAsociacion;
import Tramas.Beacon;
import Tramas.CalculoMac;
import Tramas.CuartaTramaAsociacion;
import Tramas.PrimeraTramaAsociacion;
import Tramas.SegundaIack;
import Tramas.TercerIack;



/**
 *
 * @author Usuario
 */
public class Atacante {
    
    private Socket socket;
    private int puerto;
    private String host;
    private String usuario;
    private String tfMensaje;
    private String tipoTrama;
    
    public Atacante(){
        host = "localhost";
        puerto = 61111;
        usuario = "HA";
        
        System.out.println("Quieres conectarte a " + host + " en el puerto " + puerto + " con el nombre de ususario: " + usuario + ".");
        
        // Se crea el socket para conectar con el Sevidor del Chat
        try {
            socket = new Socket(host, puerto);
        } catch (UnknownHostException ex) {
        	System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
        	System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
        //GENERACION DE TRAMAS BEACON
               /* Beacon GeneracionBeacon=new Beacon();
		String TramaBeacon=GeneracionBeacon.Beacon1();
                System.out.println(TramaBeacon);
                tfMensaje=TramaBeacon;*/
         tfMensaje="Atacante inicia conexion";
         tipoTrama="IC";
        // Accion para el boton enviar
        ConexionServidor ce;
        //ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
        Beacon GeneracionBeacon=new Beacon();
		String TramaBeacon=GeneracionBeacon.Beacon1();
                //System.out.println(TramaBeacon); //PRUEBA PARA VER LA BEACON QUE ENVIA
                tfMensaje=TramaBeacon;
                tipoTrama="BB"; 
       ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
        
    }
    
    public void recibirMensajesServidor() throws Exception{
        //TextArea informacion
        // Obtiene el flujo de entrada del socket
        //this.informacion=informacion;
        DataInputStream entradaDatos = null;
        String mensaje;
        String Asociacion1Hub=null;
        String Direccion_HubHub=null;
        String Direccion_nodoHub=null;
        BigInteger SkBHub=new BigInteger("0");
        String NonceB_HubHub=null;
        String Asociacion2Hub=null;
        String IACK1Hub=null;
        String Asociacion3Hub=null;
        String Asociacion4Hub=null;
        String WitnessA_nodoHub=null;
        String MK_KMAC_4BHub=null;
        String Temp1_nodoHub=null;
        String Temp2_nodoHub=null;
        String TempHub=null;
        String IACK2Hub=null;
        
        
        //**************************************
        int contadorTramasNodo=0;
        boolean asociacionNodo=false;
        String IACK1Nodo=null;
        String IACK2Nodo=null;
        BigInteger SkANodo=new BigInteger("0");
        
        String Asociacion1Nodo=null;
        String Direccion_nodoNodo=null;
        String Direccion_HubNodo=null;
        String WitnessA_nodoNodo=null;
        String Asociacion3Nodo=null;
        String MK_KMAC_3ANodo=null;
        String NonceA_nodoNodo=null;
        String MK_KMAC_4ANodo=null;
        String NonceBNodo=null;
        String TempNodo=null;
        String Temp1_nodoNodo=null;
        boolean estadoNodo=false;
        boolean asociacion=false;
        //**************************************
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
        	System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
        	System.out.println("El socket no se creo correctamente... parte flujo. ");
        }
        
        // Bucle infinito que recibe mensajes del servidor
        boolean conectado = true;
        boolean estadoHub=true;
        //ConexionServidor ce;
        //ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
        while (conectado) {
            try {
                ConexionServidor ce;
                mensaje = entradaDatos.readUTF();
                boolean usuarioTXHub=mensaje.startsWith("N1");
                
                if(mensaje.substring(3,4).equals("A")){
                    estadoHub=false;
                }
                
                Beacon GeneracionBeacon=new Beacon();
		String TramaBeacon=GeneracionBeacon.Beacon1();
                usuario = "HA";
                tfMensaje=TramaBeacon;
                tipoTrama="BB";
              

                
                if(estadoHub==true){
                    Thread.sleep(3000);
                    //System.out.println("contador"+contador+mensaje);
                    ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
                    
                    
                }
                
                
                //********************************************
                boolean usuarioTXNodo=mensaje.startsWith("H2");
                if(usuarioTXNodo==true){
                    contadorTramasNodo++;
                   
                }
                
                               
                if(asociacionNodo==false && usuarioTXNodo==true){
                     System.out.println(mensaje + System.lineSeparator());
                }
                if(asociacionNodo==true && mensaje.substring(3,5).equals("BB")==true){
                    mensaje="                  ";
                    
                }
                if(contadorTramasNodo==1){
                        asociacionNodo=true;                    
                }
                //************************************************************
                
                
                
                
                
                if(usuarioTXHub==true){
                  
                System.out.println(mensaje + System.lineSeparator());//BIEN
                String usuarioTramaTX=mensaje.substring(3,5);
                
                    
                    //System.out.println(usuarioTramaTX + System.lineSeparator());//PRUEBA VER Q TIPO DE TRAMA LLEGA
                    if(usuarioTramaTX.equals("A1")){
                        Asociacion1Hub=mensaje.substring(7, mensaje.length());//trama de asociacion recibida del NODO
                        //System.out.println(Asociacion1 + System.lineSeparator());//prueba para ver trama A1
                        PrimerIack primerIACK=new PrimerIack();
                        IACK1Hub=primerIACK.IACK();
                        usuario = "HA";
                        tfMensaje=IACK1Hub;
                        tipoTrama="I1";
                        //envio Iack
                        
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
                        //creo trama de asociacion 2
                        Tramas.CalculoMac MACTrama=new Tramas.CalculoMac();
                        Direccion_HubHub=MACTrama.Mac();
                        System.out.println("Dir Hub:"+Direccion_HubHub+" hub");

                        Direccion_nodoHub=Asociacion1Hub.substring(28,40);
                        System.out.println("Dir nodo:"+Direccion_nodoHub);


                       // BigInteger SkB;
                        StandardCurve procesoHub=new StandardCurve("P-256");
                        //BigInteger SkB;
                        SkBHub=procesoHub.getRandomSK();
                        String SkB_Hub=(String.format("%x",SkBHub)).toUpperCase();
                        System.out.println("SKA Hub:"+SkB_Hub+" hub");
                        BigInteger [] PkB;
                        PkB=procesoHub.getPublicKey(SkBHub);
                        String PkBx_Hub=(String.format("%x", PkB[0])).toUpperCase();
                        if(PkBx_Hub.length()==63){
                            PkBx_Hub="0"+PkBx_Hub;
                        }
                        System.out.println("PKAx Hub:"+PkBx_Hub+" hub");
                        String PkBy_Hub=(String.format("%x", PkB[1])).toUpperCase();
                        if(PkBy_Hub.length()==63){
                            PkBy_Hub="0"+PkBy_Hub;
                        }
                        System.out.println("PKAy Hub:"+PkBy_Hub+" hub");

                        Random rnd=new SecureRandom();
                        BigInteger NonceB=new BigInteger(128,rnd);
                        NonceB_HubHub=String.format("%x", NonceB).toUpperCase();
                        if(NonceB_HubHub.length()==31){
                            NonceB_HubHub="0"+NonceB_HubHub;
                        }
                        System.out.println("NonceB nodo:"+NonceB_HubHub+" hub");

                        SegundaTramaAsociacion prueba2=new SegundaTramaAsociacion();
                        Asociacion2Hub=prueba2.Asociacion2(Asociacion1Hub, PkBx_Hub, PkBy_Hub,NonceB_HubHub);
                        usuario = "HA";
                        tfMensaje=Asociacion2Hub;
                        //System.out.println(Asociacion2);//prueba de A2
                        tipoTrama="A2";
                       // ConexionServidor ce;
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
                        
                    }
                    
                    if(usuarioTramaTX.equals("I2")){
                        IACK2Hub=mensaje.substring(7, mensaje.length());
                        BigInteger a= new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948");
                        BigInteger b= new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291");
                        BigInteger p= new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");
                
                        BigInteger PKBx;
                        BigInteger PKBy;
                        PKBx=new BigInteger(Asociacion1Hub.substring(80,144),16);
                        PKBy=new BigInteger(Asociacion1Hub.substring(144,208),16);

                        MCurve DHK=new MCurve(a, b, p, PKBx, PKBy);

                        BigInteger [] DHKey;
                        DHKey=DHK.getPublicKey(SkBHub);
                        TempHub=String.format("%x", DHKey[0]).toUpperCase();
                        Temp1_nodoHub=TempHub.substring(0,32);
                        System.out.println("Temp1_nodo: "+Temp1_nodoHub+" hub");

                        WitnessA_nodoHub=Asociacion1Hub.substring(48,80);
                        String SecuritySS=Asociacion1Hub.substring(40,44);

                        String Mensaje_MK_KMAC_3B=Direccion_nodoHub.concat(Direccion_HubHub).concat(WitnessA_nodoHub).concat(NonceB_HubHub).concat(SecuritySS);
                        double len1=Mensaje_MK_KMAC_3B.length();
                        double len=len1/2;

                        CMAC CMAC=new CMAC();
                        String MK_KMAC_3B=(CMAC.CalcCMAC(Temp1_nodoHub, Mensaje_MK_KMAC_3B, 64));
                        System.out.println("MK_KMAC_3B: "+MK_KMAC_3B+" hub");

                        String Mensaje_MK_KMAC_4B=Direccion_HubHub.concat(Direccion_nodoHub).concat(NonceB_HubHub).concat(WitnessA_nodoHub).concat(SecuritySS);
                        len1=Mensaje_MK_KMAC_4B.length();
                        len=len1/2;
                        MK_KMAC_4BHub=(CMAC.CalcCMAC(Temp1_nodoHub, Mensaje_MK_KMAC_4B, 64));
                        System.out.println("MK_KMAC_4B: "+MK_KMAC_4BHub+" hub");

                        TerceraTramaAsociacion trama3=new TerceraTramaAsociacion ();

                        Asociacion3Hub=trama3.Asociacion3(Asociacion2Hub, MK_KMAC_3B);
                        System.out.println("Asociacion3: "+Asociacion3Hub+" hub");
                        usuario = "HA";
                        tfMensaje=Asociacion3Hub;
                        //System.out.println(Asociacion2);//prueba de A2
                        tipoTrama="A3";
                        
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);

                    }
                     if(usuarioTramaTX.equals("A4")){
                        Asociacion4Hub=mensaje.substring(7, mensaje.length());
                        CuartoIack cuartoIACK=new CuartoIack();
                        String IACK4=cuartoIACK.IACK4(IACK1Hub);
                        usuario = "HA";
                        tfMensaje=IACK4;
                        //System.out.println(Asociacion2);//prueba de A2
                        tipoTrama="I4";
                        
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
                        
                        String PKAx_nodo=Asociacion4Hub.substring(80,144);
                        String PKAy_nodo=Asociacion4Hub.substring(144,208);
                        String NonceA=Asociacion4Hub.substring(48,80);
                
                
                        String Mensaje_Witness_A=Direccion_nodoHub.concat(Direccion_HubHub).concat(PKAx_nodo).concat(PKAy_nodo);
                        CMAC CMAC=new CMAC();
                        String W=CMAC.CalcCMAC(NonceA, Mensaje_Witness_A, 128);
                        
                        String MK_KMAC_4A=Asociacion4Hub.substring(208,224);
                         //System.out.println(MK_KMAC_4A);
                
                        if(MK_KMAC_4BHub.equals(MK_KMAC_4A)==true){
                           // System.out.println("entro aqui");
                    
                                if(W.equals(WitnessA_nodoHub)==true){
                                       BigInteger NonceABig=new BigInteger(NonceA,16);
                                       BigInteger NonceBBig=new BigInteger(NonceB_HubHub,16);
                                       NonceABig=NonceABig.xor(NonceBBig);
                                       String KeyD=NonceA.concat(NonceB_HubHub);
                                        String D=CMAC.CalcCMAC(NonceA, NonceB_HubHub.concat(NonceA).concat(Temp1_nodoHub), 16);
                                        int Display_A=Integer.parseInt(D,16);
                                        System.out.println("Display A:"+Display_A+" hub");
                                        
                                        Temp2_nodoHub=TempHub.substring(32,TempHub.length());
                                        String MK=CMAC.CalcCMAC(Temp2_nodoHub, NonceA.concat(NonceB_HubHub),128);
                                        System.out.println("MK: "+MK);
                                        
                                }else{
                                    System.out.println("El procedimiento ha sido abortado 1");
           
                                }
                        }else{
                            System.out.println("El procedimiento ha sido abortado 2");
           
                        }
                        
                         
                     }

                    
                }
                
                
                
                
                //***********************************************************
                if(usuarioTXNodo==true && asociacionNodo==true){
                System.out.println(mensaje + System.lineSeparator()+" nodo");//BIEN
                
                    //String usuarioTramaTX=mensaje.substring(0,5);//incluye el o pero el 5 caracter no
                    String usuarioTramaTXNodo=mensaje.substring(3,5);
                    //System.out.println(usuarioTramaTX + System.lineSeparator());//PRUEBA VER Q TIPO DE TRAMA LLEGA
                    if(usuarioTramaTXNodo.equals("BB")){
                        String Beacon_Hub=mensaje.substring(7, 59);
                    //System.out.println(Beacon_Hub + System.lineSeparator());//PRUEBA PARA VER Q TOMA SOLO LA TRAMA BEACON
                        //INICIO CREACION DE PRIMERA TRAMA ASOCIACION
                        Direccion_HubNodo=Beacon_Hub.substring(14,26);
                        System.out.println("Dir Hub:"+Direccion_HubNodo+" nodo");

                        CalculoMac MACTrama=new CalculoMac();
                        Direccion_nodoNodo=MACTrama.Mac();
                        System.out.println("Dir nodo:"+Direccion_nodoNodo+" nodo");

                        
                        StandardCurve procesoNodo=new StandardCurve("P-256");
                        SkANodo=procesoNodo.getRandomSK();
                        String SkA_nodo=(String.format("%x",SkANodo)).toUpperCase();
                        System.out.println("SKA nodo:"+SkA_nodo+" nodo");
                        BigInteger [] PkANodo;
                        PkANodo=procesoNodo.getPublicKey(SkANodo);
                        String PkAx_nodo=(String.format("%x", PkANodo[0])).toUpperCase();
                        
                        if(PkAx_nodo.length()==63){
                            PkAx_nodo="0"+PkAx_nodo;
                        }
                        System.out.println("PKAx nodo:"+PkAx_nodo+" nodo");
                        String PkAy_nodo=(String.format("%x", PkANodo[1])).toUpperCase();
                        if(PkAy_nodo.length()==63){
                            PkAy_nodo="0"+PkAy_nodo;
                        }
                        System.out.println("PKAy nodo:"+PkAy_nodo+" nodo");

                        Random rnd=new SecureRandom();
                        BigInteger NonceA=new BigInteger(128,rnd);
                        NonceA_nodoNodo=String.format("%x", NonceA).toUpperCase();
                        if(NonceA_nodoNodo.length()==31){
                            NonceA_nodoNodo="0"+NonceA_nodoNodo;
                        }
                        System.out.println("NonceA nodo:"+NonceA_nodoNodo+" nodo");




                        String Mensaje_Witness=Direccion_nodoNodo.concat(Direccion_HubNodo).concat(PkAx_nodo).concat(PkAy_nodo);
                        double len1=Mensaje_Witness.length();
                        double len=len1/2;
                        System.out.println("Mensaje witness: "+Mensaje_Witness+" nodo");
                        System.out.println("Nonce_A nodo: "+NonceA_nodoNodo+" nodo");

                        CMAC CMAC=new CMAC();
                        WitnessA_nodoNodo=CMAC.CalcCMAC(NonceA_nodoNodo, Mensaje_Witness, 128);
                        System.out.println("Witness A:"+WitnessA_nodoNodo+" nodo");

                        PrimeraTramaAsociacion trama=new PrimeraTramaAsociacion();

                        Asociacion1Nodo=trama.Asociacion1(Direccion_HubNodo, Direccion_nodoNodo, PkAx_nodo , PkAy_nodo,WitnessA_nodoNodo);
                        System.out.println("Primera Trama:"+Asociacion1Nodo+" nodo");
                        usuario = "NA";
                        tfMensaje=Asociacion1Nodo;
                        tipoTrama="A1";
                        
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
                
                    }
   
                    if(usuarioTramaTXNodo.equals("I1"))
                     {
                         IACK1Nodo=mensaje.substring(7, mensaje.length());

                    }
                    
                    if(usuarioTramaTXNodo.equals("A2")){
                        
  
                        String Asociacion2=mensaje.substring(7, mensaje.length());
                        SegundaIack segundoIACK=new SegundaIack();
                        IACK2Nodo=segundoIACK.IACK2(IACK1Nodo);
 
                        usuario = "NA";
                        tfMensaje=IACK2Nodo;
                        tipoTrama="I2";
                        //ConexionServidor ce;
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje); //envio IACK2
                        
                        
                        BigInteger a= new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948");
                        BigInteger b= new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291");
                        BigInteger p= new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");

                        BigInteger PKBx;
                        BigInteger PKBy;
                        PKBx=new BigInteger(Asociacion2.substring(80,144),16);
                        PKBy=new BigInteger(Asociacion2.substring(144,208),16);

                        MCurve DHK=new MCurve(a, b, p, PKBx, PKBy);

                        BigInteger [] DHKey;
                        DHKey=DHK.getPublicKey(SkANodo);
                        TempNodo=String.format("%x", DHKey[0]).toUpperCase();
                        Temp1_nodoNodo=TempNodo.substring(0,32);
                        System.out.println("Temp1_nodo: "+Temp1_nodoNodo+" nodo");

                        NonceBNodo=Asociacion2.substring(48,80);
                        String SecuritySS=Asociacion1Nodo.substring(40,44);

                        String Mensaje_MK_KMAC_3A=Direccion_nodoNodo.concat(Direccion_HubNodo).concat(WitnessA_nodoNodo).concat(NonceBNodo).concat(SecuritySS);
                        CMAC CMAC=new CMAC();
                        MK_KMAC_3ANodo=(CMAC.CalcCMAC(Temp1_nodoNodo, Mensaje_MK_KMAC_3A, 64));
                        System.out.println("MK_KMAC_3A: "+MK_KMAC_3ANodo+" nodo");


                        String Mensaje_MK_KMAC_4A=Direccion_HubNodo.concat(Direccion_nodoNodo).concat(NonceBNodo).concat(WitnessA_nodoNodo).concat(SecuritySS);

                        MK_KMAC_4ANodo=(CMAC.CalcCMAC(Temp1_nodoNodo, Mensaje_MK_KMAC_4A, 64)).substring(0,16);
                        System.out.println("MK_KMAC_4A: "+MK_KMAC_4ANodo+" nodo");
                    }
                    if(usuarioTramaTXNodo.equals("A3")){
                        Asociacion3Nodo=mensaje.substring(7, mensaje.length());
                        TercerIack tercerIACK=new TercerIack();
                        String IACK3=tercerIACK.IACK3(IACK2Nodo);
                        usuario = "NA";
                        tfMensaje=IACK3;
                        tipoTrama="I3";
                        //ConexionServidor ce;
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje); //envio IACK3
                        String MK_KMAC_3B=Asociacion3Nodo.substring(208,224);
                
                        if(MK_KMAC_3ANodo.equals(MK_KMAC_3B)==true){
                    
                            CuartaTramaAsociacion trama4=new CuartaTramaAsociacion();
                            String Asociacion4=trama4.Asociacion4(Asociacion1Nodo, NonceA_nodoNodo, MK_KMAC_4ANodo);
                            System.out.println("Asociacion 4: "+Asociacion4+" nodo");
                            usuario = "NA";
                            tfMensaje=Asociacion4;
                            tipoTrama="A4";
                            //ConexionServidor ce;
                            ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje); //envio IACK3
                            
                        }else{
                            System.out.println("el procedimiento ha sido Abortado"+" nodo");
           
                        }
                        
                    }
                    if(usuarioTramaTXNodo.equals("I4")){
                        CMAC CMAC=new CMAC();
                        BigInteger NonceABig=new BigInteger(NonceA_nodoNodo,16);
                        BigInteger NonceBBig=new BigInteger(NonceBNodo,16);
                        NonceABig=NonceABig.xor(NonceBBig);
                        String KeyD=NonceA_nodoNodo.concat(NonceBNodo);
                        String D=CMAC.CalcCMAC(NonceA_nodoNodo, NonceBNodo.concat(NonceA_nodoNodo).concat(Temp1_nodoNodo), 16);
                        int Display_A=Integer.parseInt(D,16);
                        System.out.println("Display A:"+Display_A+" nodo");
                         
                        
                        String Temp2_nodo=TempNodo.substring(32,TempNodo.length());
                        String MK=CMAC.CalcCMAC(Temp2_nodo, NonceA_nodoNodo.concat(NonceBNodo),128);
                        System.out.println("MK: "+MK+" nodo");
                        
                    }
                }
                //***********************************************************
                
                
                
                
                
                
                
            } catch (IOException ex) {
            	System.out.println("Error al leer del stream de entrada: " + ex.getMessage());
                conectado = false;
            } catch (NullPointerException ex) {
            	System.out.println("El socket no se creo correctamente parte recibir. ");
                conectado = false;
            }
        }
    }
    public static void main(String[] args) throws Exception{
        
            Atacante c = new Atacante();
            c.recibirMensajesServidor();
           // HubGUIcliente1 d=new HubGUIcliente1();
          // c.recibirMensajesServidor();
            
            
        
        
        
    }
        
}