package presentacion;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import negocio.CatalogoNegocio;
import negocio.ICatalogoNegocio;
import persistencia.ConexionBD;
import persistencia.IConexionBD;

/**
 * Pantalla principal del sistema. Da acceso a los cuatro módulos del laboratorio
 * y es el punto de entrada de la aplicación.
 *
 * @author Cristian Devora
 */
public class frmMenu extends JFrame {

    private final IConexionBD conexion;

    public frmMenu(IConexionBD conexion) {
        this.conexion = conexion;
        initComponents();
    }

    private void initComponents() {
        setTitle("Laboratorio Clinico Salud Total");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 360);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Laboratorio Clinico \"Salud Total\"", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        panel.add(titulo);

        JButton btnCatalogo = new JButton("1. Catalogo de Analisis");
        JButton btnSolicitud = new JButton("2. Nueva Solicitud");
        JButton btnResultados = new JButton("3. Ingreso de Resultados");
        JButton btnReporte = new JButton("4. Ver Reporte");

        // ── AQUÍ ESTÁ EL CAMBIO CLAVE ────────────────────────────────────────
        btnCatalogo.addActionListener(e -> {
            // 1. Instanciamos la capa de negocio usando la conexión del menú
            ICatalogoNegocio catalogoNegocio = new CatalogoNegocio(this.conexion);
            
            // 2. Abrimos la nueva pantalla pasándole el negocio como pide su constructor
            new CatalogoAnalisis(catalogoNegocio).setVisible(true);
        });
        // ────────────────────────────────────────────────────────────────────

        btnSolicitud.addActionListener(e -> abrirSolicitud());
        //btnResultados.addActionListener(e -> new frmResultados(conexion).setVisible(true));
        //btnReporte.addActionListener(e -> new frmReporte(conexion).setVisible(true));

        panel.add(btnCatalogo);
        panel.add(btnSolicitud);
        panel.add(btnResultados);
        panel.add(btnReporte);

        add(panel);
    }

    private void abrirSolicitud() {
        new frmSolicitud(conexion).setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si falla, se usa el look and feel por defecto.
        }
        IConexionBD conexion = new ConexionBD();
        SwingUtilities.invokeLater(() -> new frmMenu(conexion).setVisible(true));
    }
}