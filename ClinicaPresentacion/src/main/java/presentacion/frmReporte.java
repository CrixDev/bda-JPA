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
 *
 * @author dylannvms
 */

public class frmReporte extends JFrame {

    public frmReporte(IConexionBD conexion) {
        setTitle("Ver Reporte");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        add(new JLabel("Módulo en construcción...", SwingConstants.CENTER));
    }
}
