package ui;

import Server.ui.ServerMain;
import comunication.Archivo;
import comunication.DatosCom;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import ui.jTreeLib.JTreeFile;
import ui.threads.HiloConServer;

/**
 *
 * @author coby
 */
public class InterfaceGui extends javax.swing.JFrame {

    Socket socket;
    DatosCom datos;
    ObjectOutputStream output;
    ObjectInputStream input;

    /**
     * Inicializacion de la aplicacion
     */
    public InterfaceGui() {
        initComponents();
        JTreeFile jTreeFiles = new JTreeFile();
        jTreeFiles.setJTree(jTree1);
        jTreeFiles.init();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Compartir Archivos");

        jButton1.setText("Descargar");

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Desconectado");
        jPanel1.add(jLabel2, java.awt.BorderLayout.CENTER);

        jScrollPane2.setViewportView(jTree1);

        jButton2.setText("Subir Archivo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jMenu1.setText("Servidor");

        jMenuItem1.setText("Conectar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Crear servidor");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        ServerMain server = new ServerMain();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            if (socket != null) {
                if (socket.isConnected()) {
                    socket.close();
                }
            }
            datos = new DatosCom();
            SettingsCom com = new SettingsCom(this, true, datos);
            com.setVisible(true);
            socket = new Socket(datos.getHost(), datos.getPuerto());
            output = new ObjectOutputStream(socket.getOutputStream());
            HiloConServer hilo = new HiloConServer(socket, jList1);
            Thread hiloT = new Thread(hilo);
            hiloT.start();
            jLabel2.setText("Conectado");
        } catch (IOException ex) {
            jLabel2.setText("Ocurrio un error al conectar");
        }

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Object nodos[] = jTree1.getSelectionPath().getPath(); 
        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode)nodos[nodos.length-1];
        String ruta = nodo.getUserObject().toString(); 
        System.out.println("Ruta" + ruta);
        try {
            File file = new File(ruta);
            System.out.println(ruta);
            if (file.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Esta version solo acepta archivos");
            } else {            
                enviaArchivo(ruta, output, file.getName(), file.length());
            }
             
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error con el archivo");
        }

    }//GEN-LAST:event_jButton2ActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

}
