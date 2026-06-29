/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import persistencia.IConexionBD;

/**
 * Módulo 1: Catálogo de Análisis. Pendiente de implementar por el compañero
 * asignado.
 * 
 * @author dylannvms
 */

public class frmCatalogo extends JFrame {

    public frmCatalogo(IConexionBD conexion) {
        setTitle("Catálogo de Análisis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        add(new JLabel("Módulo en construcción...", SwingConstants.CENTER));
    }
}
