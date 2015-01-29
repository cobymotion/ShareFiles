/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunication;

import java.io.Serializable;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author coby
 */
public class ListaArchivos implements Serializable {
    
  
    String[] listaNombres; 

    public String[] getListaArchivos() {
        return listaNombres;
    }

    public void setListaArchivos(List listaArchivos) {
        listaNombres = new String[listaArchivos.size()]; 
        for(int i=0;i<listaArchivos.size();i++)
            listaNombres[i] = listaArchivos.get(i).toString();
    }

  
    
    
}
