
package ui.threads;

import comunication.ListaArchivos;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;

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
                if(lista instanceof ListaArchivos)
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
    
    
}
