
package ProcesoAsociacion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    int puerto = 61111;//Puerto a utilizar para las conexiones
    int maximoConexiones = 64; // Maximo de conexiones simultaneas
    ServerSocket servidor = null; 
    Socket socket = null;
    
    public Servidor(){
    }
        public void IniciarServidor() {
        Procedimiento mensajes = new Procedimiento();
        try{
            servidor = new ServerSocket(puerto, maximoConexiones); // Se crea el serverSocket
            while (true) {// Bucle infinito para esperar conexiones
            	System.out.println("Servidor 2 a la espera de conexiones.");//Mensaje que indica que el servidor esta receptando conexiones
                socket = servidor.accept();
                System.out.println("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");//Mensaje con la dirección de equipo conectado
                ConexionCliente cc = new ConexionCliente(socket, mensajes);
                cc.start();
            }
        }catch(IOException ex) {
        	System.out.println("Error: " + ex.getMessage());
        }finally{
            try {
                socket.close();
                servidor.close();
            }catch (IOException ex) {
            	System.out.println("Error al cerrar el servidor: " + ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        Servidor nodo2=new Servidor();
        nodo2.IniciarServidor();
    }
}
