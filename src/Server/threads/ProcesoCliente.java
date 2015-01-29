package Server.threads;

import comunication.Archivo;
import comunication.ListaArchivos;
import comunication.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
        lista.addElement("Nuevo");
    }

    @Override
    public void run() {
        while(true){
        try {
            Object objEntrada = input.readObject();
            if (objEntrada instanceof Message) {
                System.out.println("Solicitaste el archivo "); 
                Message msg = (Message)objEntrada; 
                System.out.println(ruta + "/" + msg.ruta);
                File f = new File(ruta + "/" + msg.ruta); 
                if(f.exists())
                    enviaArchivo(ruta + "/" + msg.ruta, output, msg.ruta, f.length());
                else 
                    System.out.println("No existe el archivo");
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
    
     private void enviaArchivo(String nombreArchivo, ObjectOutputStream oos, String nombre, long tam) {
        String key = nombreArchivo;
        List<Archivo> lista = new ArrayList<>();
        try {
            boolean enviadoUltimo = false;
            // Se abre el fichero.
            FileInputStream fis = new FileInputStream(nombreArchivo);
            // Se instancia y rellena un mensaje de envio de fichero
            Archivo archivo = new Archivo();
            archivo.nombreArchivo = nombre;
            // Se leen los primeros bytes del fichero en un campo del mensaje
            int leidos = fis.read(archivo.contenidoArchivo);
                // Bucle mientras se vayan leyendo datos del fichero
            while (leidos > -1) {
                // Se rellena el número de bytes leidos
                archivo.bytesValidos = leidos;
                // Si no se han leido el máximo de bytes, es porque el fichero
                // se ha acabado y este es el último mensaje
                if (leidos < Archivo.LONGITUD) {
                    archivo.ultimoMsj = true;
                    enviadoUltimo = true;
                } else {
                    archivo.ultimoMsj = false;
                }
                // Se envía por el socket
                oos.writeObject(archivo);
                // Si es el último mensaje, salimos del bucle.
                if (archivo.ultimoMsj) {
                    break;
                }
                // Se crea un nuevo mensaje
                archivo = new Archivo();
                archivo.nombreArchivo = nombreArchivo;
                // y se leen sus bytes.
                leidos = fis.read(archivo.contenidoArchivo);
            }
            if (enviadoUltimo == false) {
                archivo.ultimoMsj = true;
                archivo.bytesValidos = 0;
                oos.writeObject(archivo);
                lista.add(archivo);
            }
            fis.close();
            // Se cierra el ObjectOutputStream
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
