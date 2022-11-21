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
import Tramas.Beacon;
import Tramas.CalculoMac;
import Tramas.CuartaTramaAsociacion;
import Tramas.CuartoIack;
import Tramas.PrimerIack;
import Tramas.PrimeraTramaAsociacion;
import Tramas.SegundaIack;
import Tramas.SegundaTramaAsociacion;
import Tramas.TercerIack;
import Tramas.TerceraTramaAsociacion;

import javax.swing.*;

public class Hub1GUI extends JFrame implements ActionListener {

	private Socket socket;
    private int puerto;
    private String host;
    private String usuario;
    private String tfMensaje;
    private String tipoTrama;
	public JTextArea mensajesChat;
	private JTextField textFieldAtaqueEjecutado;
	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
					Hub1GUI frame = new Hub1GUI();
					frame.setVisible(true);
					frame.recibirMensajesServidor();
	}

	/**
	 * Create the frame.
	 */
	public Hub1GUI() {
		super("HUB");
		mensajesChat = new JTextArea();
        mensajesChat.setEditable(false); // El area de mensajes del chat no se debe de poder editar
        mensajesChat.setLineWrap(true); // Las lineas se parten al llegar al ancho del textArea
        mensajesChat.setWrapStyleWord(true); // Las lineas se parten entre palabras (por los espacios blancos)
        JScrollPane scrollMensajesChat = new JScrollPane(mensajesChat);
        /*
        textFieldAtaqueEjecutado = new JTextField();
		textFieldAtaqueEjecutado.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldAtaqueEjecutado.setEditable(false);
		textFieldAtaqueEjecutado.setText("El ataque ejecutado es: ...");
        
        */
        // Colocacion de los componentes en la ventana
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
        //c.add(tfMensaje, gbc);
        // Restaura valores por defecto
        gbc.weightx = 0;
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        //c.add(textFieldAtaqueEjecutado, gbc);
        
                
        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        host = "localhost";
        puerto = 61111;
        usuario = "H2";
        
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
         tfMensaje="hub 2 inica conexion";
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
	 public void recibirMensajesServidor() throws Exception{ //se añade throws para cap ex del Witness
         
		 DataInputStream entradaDatos = null;
	        String mensaje;
	        String Asociacion1=null;
	        String Direccion_Hub=null;
	        String Direccion_nodo=null;
	        BigInteger SkB=new BigInteger("0");
	        String NonceB_Hub=null;
	        String Asociacion2=null;
	        String IACK1=null;
	        String Asociacion3=null;
	        String Asociacion4=null;
	        String WitnessA_nodo=null;
	        String MK_KMAC_4B=null;
	        String Temp1_nodo=null;
	        String Temp2_nodo=null;
	        String Temp=null;
	        int contador=0;
	        String IACK2=null;
	        boolean conectado = true;
	        boolean estado=false;
	        String IdNodo1="N1";
	        String IdNodo2="N1";
	        boolean IdNodocomprobacion=true;
	        //String Asociacion3=null;
	        try {
	            entradaDatos = new DataInputStream(socket.getInputStream());
	        } catch (IOException ex) {
	        	System.out.println("Error al crear el stream de entrada: " + ex.getMessage());
	        } catch (NullPointerException ex) {
	        	System.out.println("El socket no se creo correctamente... parte flujo. ");
	        }
	        
	        // Bucle infinito que recibe mensajes del servidor

	 
	        while (conectado) {
	            try {
	                
	                mensaje = entradaDatos.readUTF();
	                boolean usuarioTX=mensaje.startsWith("N") ;
	                        //|| mensaje.startsWith("HA");
	               //System.out.println("mensaje:"+ mensaje);
	                
	                if(mensaje.substring(3,5).equals("A1")){//condicional para que empiece a asociar cuando le llega un A1
	                    estado=true;
	                    IdNodo1=mensaje.substring(0,2);
	                    
	                }
	                if(usuarioTX==true){//este entra cada que llegue un N
	                    IdNodo2=mensaje.substring(0,2);//guarda el Id q llega
	                    IdNodocomprobacion=IdNodo1.equals(IdNodo2);//comprueba el Id con el que comenzo a asociarse
	                }
	                
	                
	                Beacon GeneracionBeacon=new Beacon();
			String TramaBeacon=GeneracionBeacon.Beacon1();
	                tfMensaje=TramaBeacon;
	                tipoTrama="BB";
	                ConexionServidor ce;

	                
	                if(estado==false){//timer con el que envia las tramas beacon
	                    Thread.sleep(3000);
	                    //System.out.println("contador"+contador+mensaje);
	                    ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
	                }
	                

	                if(usuarioTX==true && IdNodocomprobacion==true){
	                  
	                mensajesChat.append(mensaje + System.lineSeparator());//BIEN
	                String usuarioTramaTX=mensaje.substring(3,5);
	                
	                    
	                    //System.out.println(usuarioTramaTX + System.lineSeparator());//PRUEBA VER Q TIPO DE TRAMA LLEGA
	                    if(usuarioTramaTX.equals("A1")){
	                        Asociacion1=mensaje.substring(7, mensaje.length());//trama de asociacion recibida del NODO
	                        //System.out.println(Asociacion1 + System.lineSeparator());//prueba para ver trama A1
	                        PrimerIack primerIACK=new PrimerIack();
	                        IACK1=primerIACK.IACK();
	                        tfMensaje=IACK1;
	                        tipoTrama="I1";
	                        //envio Iack
	                        
	                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
	                        //creo trama de asociacion 2
	                        Tramas.CalculoMac MACTrama=new Tramas.CalculoMac();
	                        Direccion_Hub=MACTrama.Mac();
	                        mensajesChat.append("Dir Hub:"+Direccion_Hub + System.lineSeparator()+ System.lineSeparator());

	                        Direccion_nodo=Asociacion1.substring(28,40);
	                        mensajesChat.append("Dir nodo:"+Direccion_nodo + System.lineSeparator()+ System.lineSeparator());


	                       // BigInteger SkB;
	                        StandardCurve procesoHub=new StandardCurve("P-256");
	                        //BigInteger SkB;
	                        SkB=procesoHub.getRandomSK();
	                        String SkB_Hub=(String.format("%x",SkB)).toUpperCase();
	                        mensajesChat.append("SKA Hub:"+SkB_Hub + System.lineSeparator()+ System.lineSeparator());
	                        BigInteger [] PkB;
	                        PkB=procesoHub.getPublicKey(SkB);
	                        String PkBx_Hub=(String.format("%x", PkB[0])).toUpperCase();
	                        if(PkBx_Hub.length()==63){
	                            PkBx_Hub="0"+PkBx_Hub;
	                        }
	                        mensajesChat.append("PKAx Hub:"+PkBx_Hub+ System.lineSeparator()+ System.lineSeparator());
	                        String PkBy_Hub=(String.format("%x", PkB[1])).toUpperCase();
	                        if(PkBy_Hub.length()==63){
	                            PkBy_Hub="0"+PkBy_Hub;
	                        }
	                        mensajesChat.append("PKAy Hub:"+PkBy_Hub+ System.lineSeparator()+ System.lineSeparator());

	                        Random rnd=new SecureRandom();
	                        BigInteger NonceB=new BigInteger(128,rnd);
	                        NonceB_Hub=String.format("%x", NonceB).toUpperCase();
	                        if(NonceB_Hub.length()==31){
	                            NonceB_Hub="0"+NonceB_Hub;
	                        }
	                        mensajesChat.append("NonceB nodo:"+NonceB_Hub+ System.lineSeparator()+ System.lineSeparator());

	                        SegundaTramaAsociacion prueba2=new SegundaTramaAsociacion();
	                        Asociacion2=prueba2.Asociacion2(Asociacion1, PkBx_Hub, PkBy_Hub,NonceB_Hub);
	                        tfMensaje=Asociacion2;
	                        //System.out.println(Asociacion2);//prueba de A2
	                        tipoTrama="A2";
	                       // ConexionServidor ce;
	                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
	                        
	                    }
	                    
	                    if(usuarioTramaTX.equals("I2")){
	                        IACK2=mensaje.substring(7, mensaje.length());
	                        BigInteger a= new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948");
	                        BigInteger b= new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291");
	                        BigInteger p= new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");
	                
	                        BigInteger PKBx;
	                        BigInteger PKBy;
	                        PKBx=new BigInteger(Asociacion1.substring(80,144),16);
	                        PKBy=new BigInteger(Asociacion1.substring(144,208),16);

	                        MCurve DHK=new MCurve(a, b, p, PKBx, PKBy);

	                        BigInteger [] DHKey;
	                        DHKey=DHK.getPublicKey(SkB);
	                        Temp=String.format("%x", DHKey[0]).toUpperCase();
	                        Temp1_nodo=Temp.substring(0,32);
	                        mensajesChat.append("Temp1_nodo: "+Temp1_nodo+ System.lineSeparator()+ System.lineSeparator());

	                        WitnessA_nodo=Asociacion1.substring(48,80);
	                        String SecuritySS=Asociacion1.substring(40,44);

	                        String Mensaje_MK_KMAC_3B=Direccion_nodo.concat(Direccion_Hub).concat(WitnessA_nodo).concat(NonceB_Hub).concat(SecuritySS);
	                        double len1=Mensaje_MK_KMAC_3B.length();
	                        double len=len1/2;

	                        CMAC CMAC=new CMAC();
	                        String MK_KMAC_3B=(CMAC.CalcCMAC(Temp1_nodo, Mensaje_MK_KMAC_3B, 64));
	                        mensajesChat.append("MK_KMAC_3B: "+MK_KMAC_3B+ System.lineSeparator()+ System.lineSeparator());

	                        String Mensaje_MK_KMAC_4B=Direccion_Hub.concat(Direccion_nodo).concat(NonceB_Hub).concat(WitnessA_nodo).concat(SecuritySS);
	                        len1=Mensaje_MK_KMAC_4B.length();
	                        len=len1/2;
	                        MK_KMAC_4B=(CMAC.CalcCMAC(Temp1_nodo, Mensaje_MK_KMAC_4B, 64));
	                        mensajesChat.append("MK_KMAC_4B: "+MK_KMAC_4B+ System.lineSeparator()+ System.lineSeparator());

	                        TerceraTramaAsociacion trama3=new TerceraTramaAsociacion ();

	                        Asociacion3=trama3.Asociacion3(Asociacion2, MK_KMAC_3B);
	                        mensajesChat.append("Asociacion3: "+Asociacion3+ System.lineSeparator()+ System.lineSeparator());
	                        tfMensaje=Asociacion3;
	                        //System.out.println(Asociacion2);//prueba de A2
	                        tipoTrama="A3";
	                        
	                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);

	                    }
	                     if(usuarioTramaTX.equals("A4")){
	                        Asociacion4=mensaje.substring(7, mensaje.length());
	                        CuartoIack cuartoIACK=new CuartoIack();
	                        String IACK4=cuartoIACK.IACK4(IACK1);
	                        tfMensaje=IACK4;
	                        //System.out.println(Asociacion2);//prueba de A2
	                        tipoTrama="I4";
	                        
	                        ce=new ConexionServidor(socket,usuario,tipoTrama, tfMensaje);
	                        
	                        String PKAx_nodo=Asociacion4.substring(80,144);
	                        String PKAy_nodo=Asociacion4.substring(144,208);
	                        String NonceA=Asociacion4.substring(48,80);
	                
	                
	                        String Mensaje_Witness_A=Direccion_nodo.concat(Direccion_Hub).concat(PKAx_nodo).concat(PKAy_nodo);
	                        CMAC CMAC=new CMAC();
	                        String W=CMAC.CalcCMAC(NonceA, Mensaje_Witness_A, 128);
	                        
	                        String MK_KMAC_4A=Asociacion4.substring(208,224);
	                         //System.out.println(MK_KMAC_4A);
	                
	                        if(MK_KMAC_4B.equals(MK_KMAC_4A)==true){
	                           // System.out.println("entro aqui");
	                    
	                                if(W.equals(WitnessA_nodo)==true){
	                                       BigInteger NonceABig=new BigInteger(NonceA,16);
	                                       BigInteger NonceBBig=new BigInteger(NonceB_Hub,16);
	                                       NonceABig=NonceABig.xor(NonceBBig);
	                                       String KeyD=NonceA.concat(NonceB_Hub);
	                                        String D=CMAC.CalcCMAC(NonceA, NonceB_Hub.concat(NonceA).concat(Temp1_nodo), 16);
	                                        int Display_A=Integer.parseInt(D,16);
	                                        mensajesChat.append("Display A:"+Display_A+ System.lineSeparator()+ System.lineSeparator());
	                                        
	                                        Temp2_nodo=Temp.substring(32,Temp.length());
	                                        String MK=CMAC.CalcCMAC(Temp2_nodo, NonceA.concat(NonceB_Hub),128);
	                                        mensajesChat.append("MK: "+MK+ System.lineSeparator()+ System.lineSeparator());
	                                        
	                                }else{
	                                    mensajesChat.append("El procedimiento ha sido abortado 1"+ System.lineSeparator()+ System.lineSeparator());
	           
	                                }
	                        }else{
	                            mensajesChat.append("El procedimiento ha sido abortado 2"+ System.lineSeparator()+ System.lineSeparator());
	           
	                        }
	                        
	                         
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
	@Override
	public void actionPerformed(ActionEvent e) {
		String accion=e.getActionCommand();
		if(accion.equals("enviar")){
			Hub1GUI frame2 = new Hub1GUI();
			frame2.setVisible(true);
			try {
				frame2.recibirMensajesServidor();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

}
