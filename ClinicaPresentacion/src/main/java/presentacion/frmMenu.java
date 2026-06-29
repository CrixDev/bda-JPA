package presentacion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import dto.ReporteResultadoDTO;
import negocio.CatalogoNegocio;
import negocio.ICatalogoNegocio;
import negocio.IReporteNegocio;
import negocio.NegocioException;
import negocio.ReporteNegocio;
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
        JButton btnReporte = new JButton("4. Generar Reporte");

        btnCatalogo.addActionListener(e -> {
            ICatalogoNegocio catalogoNegocio = new CatalogoNegocio(this.conexion);
            new CatalogoAnalisis(catalogoNegocio).setVisible(true);
        });
        btnSolicitud.addActionListener(e -> new frmSolicitud(conexion).setVisible(true));
        btnResultados.addActionListener(e -> new frmResultados(conexion).setVisible(true));
        btnReporte.addActionListener(e -> generarReporte());

        panel.add(btnCatalogo);
        panel.add(btnSolicitud);
        panel.add(btnResultados);
        panel.add(btnReporte);

        add(panel);
    }

    /**
     * Pide el folio, genera el reporte de esa prueba y lo muestra en una vista
     * lista para imprimir.
     */
    private void generarReporte() {
        String folio = JOptionPane.showInputDialog(this,
                "Folio de la prueba (ej: LAB-20260628-0001):",
                "Generar Reporte", JOptionPane.QUESTION_MESSAGE);
        if (folio == null || folio.isBlank()) {
            return;
        }
        try {
            IReporteNegocio reporteNegocio = new ReporteNegocio(conexion);
            List<ReporteResultadoDTO> reporte = reporteNegocio.generarReportePorFolio(folio.trim());
            if (reporte.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La prueba no tiene resultados capturados todavía.",
                        "Reporte vacío", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            mostrarReporte(reporte);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Muestra el reporte ya generado como un documento HTML con un botón para
     * enviarlo a la impresora.
     */
    private void mostrarReporte(List<ReporteResultadoDTO> reporte) {
        JEditorPane visor = new JEditorPane("text/html", construirHtml(reporte));
        visor.setEditable(false);

        JButton btnImprimir = new JButton("Imprimir");
        JButton btnCerrar = new JButton("Cerrar");

        JDialog dialog = new JDialog(this, "Reporte de Resultados", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(visor), BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.add(btnImprimir);
        pnlBotones.add(btnCerrar);
        dialog.add(pnlBotones, BorderLayout.SOUTH);

        btnImprimir.addActionListener(e -> {
            try {
                visor.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "No se pudo imprimir: " + ex.getMessage(),
                        "Error de impresión", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCerrar.addActionListener(e -> dialog.dispose());

        dialog.setPreferredSize(new Dimension(640, 480));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String construirHtml(List<ReporteResultadoDTO> reporte) {
        ReporteResultadoDTO encabezado = reporte.get(0);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = encabezado.getFechaHora() != null ? formato.format(encabezado.getFechaHora()) : "-";

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:sans-serif;'>");
        html.append("<h2 style='text-align:center;'>Laboratorio Clínico \"Salud Total\"</h2>");
        html.append("<h3 style='text-align:center;'>Reporte de Resultados</h3>");
        html.append("<p><b>Folio:</b> ").append(encabezado.getFolio()).append("<br>");
        html.append("<b>Cliente:</b> ").append(encabezado.getNombreCliente()).append("<br>");
        html.append("<b>Fecha:</b> ").append(fecha).append("</p>");

        html.append("<table border='1' cellspacing='0' cellpadding='5' width='100%'>");
        html.append("<tr style='background-color:#eeeeee;'>")
                .append("<th>Parámetro</th><th>Valor</th><th>Unidad</th>")
                .append("<th>Rango normal</th><th>Estado</th></tr>");

        for (ReporteResultadoDTO r : reporte) {
            String estado = r.isFueraDeRango()
                    ? "<span style='color:red;'><b>FUERA DE RANGO</b></span>"
                    : "Normal";
            html.append("<tr>")
                    .append("<td>").append(r.getNombreParametro()).append("</td>")
                    .append("<td>").append(r.getValor() != null ? r.getValor() : "").append("</td>")
                    .append("<td>").append(r.getUnidadMedida() != null ? r.getUnidadMedida() : "").append("</td>")
                    .append("<td>").append(r.getRangoTexto()).append("</td>")
                    .append("<td>").append(estado).append("</td>")
                    .append("</tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
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
