
package ui.threads;

import comunication.Archivo;
import comunication.ListaArchivos;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import ui.InterfaceGui;

/**
 *
 * @author coby
 */
public class HiloConServer implements Runnable {

    Socket socket; 
    ObjectInputStream input; 
    JList jlista ;
    
    public HiloConServer(Socket socket, JList jlist1) {
        this.socket = socket; 
        this.jlista = jlist1; 
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            while(true)
            {
                Object lista = input.readObject(); 
                if(lista instanceof Archivo)
                {
                     Archivo archivo = (Archivo) lista;
                     recibeArchivo(archivo, input);
                }
                else if(lista instanceof ListaArchivos)
                {
                    ListaArchivos lista2 = (ListaArchivos) lista;
                    
                        jlista.removeAll();
                    DefaultListModel modelo = new DefaultListModel(); 
                    
                    for(int i=0;i<lista2.getListaArchivos().length;i++)
                        modelo.addElement(lista2.getListaArchivos()[i]);
                    
                    jlista.setModel(modelo);
                    jlista.repaint();
                    
                    System.out.println(" -------- " );
                }
            }
        } catch (IOException ex) {
            System.out.println("No pudo leer ");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloConServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    private void recibeArchivo(Archivo archivo, ObjectInputStream entrada) {
        try {
            String rutaAbsoluta = InterfaceGui.rutaDestino + "/" + archivo.nombreArchivo;
            FileOutputStream fos = new FileOutputStream(InterfaceGui.rutaDestino + "/" + archivo.nombreArchivo);
            Object mensajeAux = archivo;
            Archivo mensajeRecibido = null;
            
            boolean primero = true;
            do {
                if (!primero) {
                    mensajeAux = entrada.readObject();
                }
                if (mensajeAux instanceof Archivo) {
                    mensajeRecibido = (Archivo) mensajeAux;
                   // System.out.print(new String(
                     //       mensajeRecibido.contenidoArchivo, 0,
                   //         mensajeRecibido.bytesValidos));
                    fos.write(mensajeRecibido.contenidoArchivo, 0,
                            mensajeRecibido.bytesValidos);
                }
                primero = false;
            } while (!mensajeRecibido.ultimoMsj);
            fos.close();
            System.out.println("Se termino de pasar el archivo");
             
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println("Error al copiar el archivo");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al copiar el archivo");
        }
    }
}
