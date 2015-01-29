package Server.threads;

import comunication.Archivo;
import comunication.ListaArchivos;
import comunication.Message;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author coby
 */
public class ProcesoCliente implements Runnable, ListDataListener {

    Socket socket;
    DefaultListModel lista;
    ObjectInputStream input;
    ObjectOutputStream output;
    String ruta;
    List<String> listaArchivos; 

    ProcesoCliente(Socket socket, DefaultListModel lista, String ruta, List<String> list) {
        this.lista = lista;
        this.socket = socket;
        this.ruta = ruta;
        this.listaArchivos = list; 
        lista.addListDataListener(this);
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Error en los flujos");
        }
    }

    @Override
    public void run() {
        while(true){
        try {
            Object objEntrada = input.readObject();
            if (objEntrada instanceof Message) {

            } else if (objEntrada instanceof Archivo) {
                Archivo archivo = (Archivo) objEntrada;
                recibeArchivo(archivo, input);
            }
        } catch (Exception e) {
            System.out.println("Prueba");
        }
        }
    }

    private void recibeArchivo(Archivo archivo, ObjectInputStream entrada) {
        try {
            String rutaAbsoluta = ruta + "/" + archivo.nombreArchivo;
            FileOutputStream fos = new FileOutputStream(ruta + "/" + archivo.nombreArchivo);
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
            listaArchivos.add(rutaAbsoluta);
            lista.addElement(rutaAbsoluta);           
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println("Error al copiar el archivo");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al copiar el archivo");
        }
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        ListaArchivos archivos  =new ListaArchivos(); 
        archivos.setListaArchivos(listaArchivos);
        System.out.println("Entro");
        try {
            output.writeObject(archivos);
        } catch (IOException ex) {
            System.out.println("No se pudo enviar ");
        }
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
    }

}
