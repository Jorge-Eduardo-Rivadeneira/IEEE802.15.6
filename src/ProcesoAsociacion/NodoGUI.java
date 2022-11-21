package ProcesoAsociacion;

import java.awt.*;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;

import CMAC_AES.CMAC;
import ECC.MCurve;
import ECC.StandardCurve;
import Tramas.CalculoMac;
import Tramas.CuartaTramaAsociacion;
import Tramas.PrimeraTramaAsociacion;
import Tramas.SegundaIack;
import Tramas.TercerIack;

import javax.swing.*;
public class NodoGUI extends JFrame {

        private Socket socket;
        private int puerto;
        private String host;
        private String usuario;
        private String tfMensaje;
        private String tipoTrama;
        public JTextArea mensajesChat;
        private JTextField textFieldAtaqueEjecutado;

    public static void main(String[] args) throws Exception {

        NodoGUI frame = new NodoGUI();
        frame.setVisible(true);
        frame.recibirMensajesServidor();
    }

    public NodoGUI() {
        super("NODO");
        mensajesChat = new JTextArea();
        mensajesChat.setEditable(false); // El area de mensajes del chat no se podra editar
        mensajesChat.setLineWrap(true); // Las lineas se parten al llegar al ancho del textArea
        mensajesChat.setWrapStyleWord(true); // Las lineas se parten entre palabras (por los espacios blancos)
        JScrollPane scrollMensajesChat = new JScrollPane(mensajesChat);
        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());       
        GridBagConstraints gbc = new GridBagConstraints();    
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(scrollMensajesChat, gbc);
        // Restaura valores por defecto
        gbc.gridwidth = 1;        
        gbc.weighty = 0;   
        gbc.fill = GridBagConstraints.HORIZONTAL;        
        gbc.insets = new Insets(0, 20, 20, 20);    
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;      
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        host = "192.168.0.1";
        puerto = 61111;
        usuario = "N1";    
        System.out.println("Quieres conectarte a " + host + " en el puerto " + puerto + " con el nombre de ususario: " + usuario + ".");
        try {
            socket = new Socket(host, puerto);
            System.out.println(socket.toString());
        } catch (UnknownHostException ex) {
            System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
            //GENERACION DE TRAMAS BEACON
        tfMensaje="nodo 1 inicia conexion";
        tipoTrama="IC";
        ConexionServidor ce;
        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
    }

    public void recibirMensajesServidor() throws Exception{ 
        DataInputStream entradaDatos = null;
        String mensaje;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
                System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
                System.out.println("El socket no se creo correctamente... parte flujo. ");
        }
        // Bucle infinito que recibe mensajes del servidor
        boolean conectado = true;
        String IACK1=null;
        String IACK2=null;
        BigInteger SkA=new BigInteger("0");
        String Asociacion1=null;
        String Direccion_nodo=null;
        String Direccion_Hub=null;
        String WitnessA_nodo=null;
        String Asociacion3=null;
        String MK_KMAC_3A=null;
        String NonceA_nodo=null;
        String MK_KMAC_4A=null;
        String NonceB=null;
        String Temp=null;
        String Temp1_nodo=null;
        int contadorTramas=0;
        boolean asociacion=false;
        boolean IdHubcomprobacion=true;
        String MK_KMAC_3B=null;
        String IdHub1="H2";
        String IdHub2="H2";
        while (conectado) {
            try {
                mensaje = entradaDatos.readUTF();
                boolean usuarioTX=mensaje.startsWith("H");
                if(mensaje.substring(3,5).equals("BB")==true){ 
                    contadorTramas++;
                }
                if(mensaje.startsWith("H")==true){//Sirve para guardar el Id del hub que esta llegando
                    IdHub1=mensaje.substring(0,2); 
                }
                ConexionServidor ce;
                if(asociacion==false){//Muestra las tramas Beacon que llegan
                    mensajesChat.append(mensaje + System.lineSeparator()+ System.lineSeparator());
                }
                if(asociacion==true && mensaje.substring(3,5).equals("BB")==true){//Condicional para evitar recibir tramas Beacon adicionales
                    mensaje="                  ";
                }
                if(contadorTramas==4){
                        asociacion=true;
                        IdHub2=mensaje.substring(0,2);//guarda el Id del hub con el que se va a asociar 
                        contadorTramas++;
                }
                IdHubcomprobacion=IdHub1.equals(IdHub2);
                if(usuarioTX==true && asociacion==true && IdHubcomprobacion==true){
                    mensajesChat.append(mensaje + System.lineSeparator()+ System.lineSeparator());
                    String usuarioTramaTX=mensaje.substring(3,5);
                    if(usuarioTramaTX.equals("BB")){
                        String Beacon_Hub=mensaje.substring(7, 59);
                        Direccion_Hub=Beacon_Hub.substring(14,26);
                        mensajesChat.append("Dir Hub:"+Direccion_Hub+ System.lineSeparator());
                        CalculoMac MACTrama=new CalculoMac();
                        Direccion_nodo=MACTrama.Mac();
                        mensajesChat.append("Dir nodo:"+Direccion_nodo+ System.lineSeparator());
                        StandardCurve procesoNodo=new StandardCurve("P-256");
                        SkA=procesoNodo.getRandomSK();
                        String SkA_nodo=(String.format("%x",SkA)).toUpperCase();
                        mensajesChat.append("SKA nodo:"+SkA_nodo+ System.lineSeparator()+ System.lineSeparator());
                        BigInteger [] PkA;
                        PkA=procesoNodo.getPublicKey(SkA);
                        String PkAx_nodo=(String.format("%x", PkA[0])).toUpperCase();
                        if(PkAx_nodo.length()==63){
                            PkAx_nodo="0"+PkAx_nodo;
                        }
                        mensajesChat.append("PKAx nodo:"+PkAx_nodo+ System.lineSeparator()+ System.lineSeparator());
                        String PkAy_nodo=(String.format("%x", PkA[1])).toUpperCase();
                        if(PkAy_nodo.length()==63){
                            PkAy_nodo="0"+PkAy_nodo;
                        }
                        mensajesChat.append("PKAy nodo:"+PkAy_nodo+ System.lineSeparator()+ System.lineSeparator());
                        Random rnd=new SecureRandom();
                        BigInteger NonceA=new BigInteger(128,rnd);
                        NonceA_nodo=String.format("%x", NonceA).toUpperCase();
                        if(NonceA_nodo.length()==31){
                            NonceA_nodo="0"+NonceA_nodo;
                        }
                        mensajesChat.append("NonceA nodo:"+NonceA_nodo+ System.lineSeparator()+ System.lineSeparator());
                        String Mensaje_Witness=Direccion_nodo.concat(Direccion_Hub).concat(PkAx_nodo).concat(PkAy_nodo);
                        double len1=Mensaje_Witness.length();
                        double len=len1/2;
                        mensajesChat.append("Mensaje witness: "+Mensaje_Witness+ System.lineSeparator()+ System.lineSeparator());
                        mensajesChat.append("Nonce_A nodo: "+NonceA_nodo+ System.lineSeparator()+ System.lineSeparator());
                        CMAC CMAC=new CMAC();
                        WitnessA_nodo=CMAC.CalcCMAC(NonceA_nodo, Mensaje_Witness, 128);
                        mensajesChat.append("Witness A:"+WitnessA_nodo+ System.lineSeparator()+ System.lineSeparator());
                        PrimeraTramaAsociacion trama=new PrimeraTramaAsociacion();
                        Asociacion1=trama.Asociacion1(Direccion_Hub, Direccion_nodo, PkAx_nodo , PkAy_nodo,WitnessA_nodo);
                        mensajesChat.append("Primera Trama:"+Asociacion1+ System.lineSeparator()+ System.lineSeparator());
                        tfMensaje=Asociacion1;
                        tipoTrama="A1";
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
                    }
                    if(usuarioTramaTX.equals("I1"))
                     {
                         IACK1=mensaje.substring(7, mensaje.length());
                    }

                    if(usuarioTramaTX.equals("A2")){
                        String Asociacion2=mensaje.substring(7, mensaje.length());
                        SegundaIack segundoIACK=new SegundaIack();
                        IACK2=segundoIACK.IACK2(IACK1);
                        tfMensaje=IACK2;
                        tipoTrama="I2";
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
                        DHKey=DHK.getPublicKey(SkA);
                        Temp=String.format("%x", DHKey[0]).toUpperCase();
                        Temp1_nodo=Temp.substring(0,32);
                        mensajesChat.append("Temp1_nodo: "+Temp1_nodo+ System.lineSeparator()+ System.lineSeparator());
                        NonceB=Asociacion2.substring(48,80);
                        String SecuritySS=Asociacion1.substring(40,44);
                        String Mensaje_MK_KMAC_3A=Direccion_nodo.concat(Direccion_Hub).concat(WitnessA_nodo).concat(NonceB).concat(SecuritySS);
                        CMAC CMAC=new CMAC();
                        MK_KMAC_3A=(CMAC.CalcCMAC(Temp1_nodo, Mensaje_MK_KMAC_3A, 64));
                        mensajesChat.append("MK_KMAC_3A: "+MK_KMAC_3A+ System.lineSeparator()+ System.lineSeparator());
                        String Mensaje_MK_KMAC_4A=Direccion_Hub.concat(Direccion_nodo).concat(NonceB).concat(WitnessA_nodo).concat(SecuritySS);
                        MK_KMAC_4A=(CMAC.CalcCMAC(Temp1_nodo, Mensaje_MK_KMAC_4A, 64)).substring(0,16);
                        mensajesChat.append("MK_KMAC_4A: "+MK_KMAC_4A+ System.lineSeparator()+ System.lineSeparator());
                    }
                    if(usuarioTramaTX.equals("A3")){
                        Asociacion3=mensaje.substring(7, mensaje.length());
                        TercerIack tercerIACK=new TercerIack();
                        String IACK3=tercerIACK.IACK3(IACK2);
                        tfMensaje=IACK3;
                        tipoTrama="I3";
                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje); //envio IACK3
                        MK_KMAC_3B=Asociacion3.substring(208,224);
                        if(MK_KMAC_3A.equals(MK_KMAC_3B)==true){
                            CuartaTramaAsociacion trama4=new CuartaTramaAsociacion();
                            String Asociacion4=trama4.Asociacion4(Asociacion1, NonceA_nodo, MK_KMAC_4A);
                            mensajesChat.append("Asociacion 4: "+Asociacion4+ System.lineSeparator()+ System.lineSeparator());
                            tfMensaje=Asociacion4;
                            tipoTrama="A4";
                            ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje); //envio Asociacion4
                        }else{
                            CuartaTramaAsociacion trama4=new CuartaTramaAsociacion();
                            String Asociacion4=trama4.Asociacion4(Asociacion1, NonceA_nodo, MK_KMAC_4A);
                            mensajesChat.append("Asociacion 4: "+Asociacion4+ System.lineSeparator()+ System.lineSeparator());
                            tfMensaje=Asociacion4;
                            tipoTrama="A4";
                            ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje); //envio Asociacion4
                            mensajesChat.append("El procedimiento Ha sido abortado debido a que la autenticacion MK_KMAC ha fallado"+ System.lineSeparator()+ System.lineSeparator());
                            mensajesChat.append("Los valores de Display y MK hubieran sido: "+ System.lineSeparator()+ System.lineSeparator());
                            CMAC CMAC=new CMAC();
                            BigInteger NonceABig=new BigInteger(NonceA_nodo,16);
                            BigInteger NonceBBig=new BigInteger(NonceB,16);
                            NonceABig=NonceABig.xor(NonceBBig);
                            String KeyD=NonceA_nodo.concat(NonceB);
                            String D=CMAC.CalcCMAC(NonceA_nodo, NonceB.concat(NonceA_nodo).concat(Temp1_nodo), 16);
                            int Display_A=Integer.parseInt(D,16);
                            mensajesChat.append("Display A:"+Display_A+ System.lineSeparator()+ System.lineSeparator());
                        String Temp2_nodo=Temp.substring(32,Temp.length());
                        String MK=CMAC.CalcCMAC(Temp2_nodo, NonceA_nodo.concat(NonceB),128);
                        mensajesChat.append("MK: "+MK+ System.lineSeparator()+ System.lineSeparator());    
                        }
                    }
                    if(usuarioTramaTX.equals("I4") && MK_KMAC_3A.equals(MK_KMAC_3B)==true ){
                        CMAC CMAC=new CMAC();
                        BigInteger NonceABig=new BigInteger(NonceA_nodo,16);
                        BigInteger NonceBBig=new BigInteger(NonceB,16);
                        NonceABig=NonceABig.xor(NonceBBig);
                        String KeyD=NonceA_nodo.concat(NonceB);
                        String D=CMAC.CalcCMAC(NonceA_nodo, NonceB.concat(NonceA_nodo).concat(Temp1_nodo), 16);
                        int Display_A=Integer.parseInt(D,16);
                        mensajesChat.append("Display A:"+Display_A+ System.lineSeparator()+ System.lineSeparator());
                        String Temp2_nodo=Temp.substring(32,Temp.length());
                        String MK=CMAC.CalcCMAC(Temp2_nodo, NonceA_nodo.concat(NonceB),128);
                        mensajesChat.append("MK: "+MK+ System.lineSeparator()+ System.lineSeparator());
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error al leer del stream de entrada: " + ex.getMessage());
                conectado = false;
            } catch (NullPointerException ex) {
                System.out.println("El socket no se creo correctamente parte recibir. ");
                conectado = false;
            }
        }
    }
}
