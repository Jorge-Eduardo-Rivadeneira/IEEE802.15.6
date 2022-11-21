package ProcesoAsociacion;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConexionServidor {
    private Socket socket; 
    private String tfMensaje;
    private String usuario;
    private String tipoTrama;
    private DataOutputStream salidaDatos;
    
    public ConexionServidor(Socket socket,String usuario,String tipoTrama, String tfMensaje ) {
        this.socket = socket;
        this.tfMensaje = tfMensaje;//Aqui entraran los mensajes de las tramas a enviarse
        this.tipoTrama =tipoTrama;
        this.usuario = usuario;
        try {
            this.salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
        	System.out.println("Error al crear el stream de salida : " + ex.getMessage());
        } catch (NullPointerException ex) {
        	System.out.println("El socket no se creo correctamente dav. ");
        }
        try {
            salidaDatos.writeUTF(usuario +" "+tipoTrama+": "+tfMensaje);
        } catch (IOException ex) {
        	System.out.println("Error al intentar enviar un mensaje: " + ex.getMessage());
        }
    }
}
