
package Server.threads;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author coby
 */
public class ControlUsuarios implements Runnable {

    private final int puerto; 
    private boolean activo = true; 
    private final String ruta; 
    private DefaultListModel lista = new DefaultListModel(); 
    private List<String> listaArchivos = new ArrayList<>(); 

    
    public ControlUsuarios(int puerto, String ruta) {
        this.puerto = puerto; 
        this.ruta = ruta; 
        
    }

    
    
    @Override
    public void run() {
        ServerSocket server;
        try { 
            server = new ServerSocket(puerto);
            System.out.println("Esperando conexiones");
            File nuevo = new File(ruta); 
            for(int i=0;i<nuevo.list().length;i++)
                listaArchivos.add(nuevo.list()[i]);
            while(activo)
            {
                Socket socket = server.accept(); 
                System.out.println("Se conecto alguien");
                ProcesoCliente pCliente = new ProcesoCliente(socket, lista, ruta, listaArchivos); 
                Thread hilo = new Thread(pCliente); 
                hilo.start();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"No se pudo abrir el puerto");
        }
    }

    public synchronized void close() {
        activo = false; 
    }
    
}
