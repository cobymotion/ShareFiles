package ui.jTreeLib;

import java.io.File;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author coby
 */
public class JTreeFile implements TreeExpansionListener{

    
    private JTree jTree1;
    private DefaultTreeModel modelo;

    public DefaultTreeModel getModelo() {
        return modelo;
    }

    public JTreeFile() {
    }

    public JTreeFile(JTree jTree1) {
        this.jTree1 = jTree1;
    }

    public void setJTree(JTree jTree1) {
        this.jTree1 = jTree1;
    }

    /**
     * Metodo que permite enlazar los escuchas de eventos 
     * y permite actualizar 
     */
    public void init() {
        //creamos el nodo principal
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Directorio");
        //creamos un modelo con el nodo que creamos principal
        modelo = new DefaultTreeModel(top);
        // seteamos el modelo y el escucha al componente 
        jTree1.setModel(modelo);
        jTree1.addTreeExpansionListener(this);
        //extraemos todas las unidades disponibles en caso que tengamos C, D o otra
        for (File f : File.listRoots()) {
            DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(f);
            //añadimos el nodo a la raiz
            top.add(raiz);
            //hacemos un recorrido de dos niveles a partir de cada una unidad
            actualizaNodo(raiz, f);     
        }
    }

    private boolean actualizaNodo(DefaultMutableTreeNode nodo, File f) {
        //quitamos lo que tenga el nodo 
        nodo.removeAllChildren();
        //recursivamente mandamos actualizar
        return actualizaNodo(nodo,f,2); 
    }
    
    /**
     * A partir de un nodo enlista los archivos y los agrega como nodo 
     * @param nodo Es el nodo que tenemos parcialmente como raiz
     * @param f es el archivo que se enlaza con la raiz 
     * @param profundidad el numero de subdirectorios que se quiere que escarbe
     * @return 
     */

    private boolean actualizaNodo(DefaultMutableTreeNode nodo, File f, int profundidad) {
       File[] files = f.listFiles(); // de el nodo que llega listamos todos sus archivos
       if(files!=null && profundidad>0) //permite detener la recursividad si ya llego al limite 
       {   
           for(File file: files)  // recorre todos los archivos 
           {
               DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode(file);
               //vuelve a mandar en caso que sea directorio 
               actualizaNodo(nuevo, file,profundidad-1); 
               nodo.add(nuevo); //crea el nodo
           }
       }
       return true; 
    }
    /**
     * Metodo que se ejecuta al expandir alguno de los nodos
     * @param event 
     */
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath path = event.getPath(); // Se obtiene el path del nodo
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        // verifica que sea nodo valido
        if(node==null || !(node.getUserObject() instanceof File) ) return; 
        File f = (File) node.getUserObject();
        actualizaNodo(node, f);  //actualiza la estructura
        JTree tree = ( JTree) event.getSource(); 
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel(); 
        model.nodeStructureChanged(node);
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) { }

}
