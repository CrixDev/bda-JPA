package presentacion;

import dto.ReporteResultadoDTO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import negocio.IReporteNegocio;
import negocio.NegocioException;
import negocio.ReporteNegocio;
import persistencia.IConexionBD;

/**
 * Panel del módulo "Reportes". Se escribe el folio, se genera el reporte de esa
 * prueba (marcando los valores fuera de rango según la edad del cliente) y se
 * puede enviar a la impresora.
 */
public class PanelReporte extends JPanel implements Recargable {

    private final IReporteNegocio reporteNegocio;

    private JTextField txtFolio;
    private JEditorPane visor;
    private JButton btnImprimir;

    public PanelReporte(IConexionBD conexion) {
        this.reporteNegocio = new ReporteNegocio(conexion);
        initComponents();
    }

    @Override
    public void recargar() {
        // Sin precarga: el reporte se genera bajo demanda por folio.
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Generar reporte por folio"));
        txtFolio = new JTextField(22);
        JButton btnGenerar = new JButton("Generar reporte");
        btnGenerar.addActionListener(e -> generarReporte());
        pnlBusqueda.add(new JLabel("Folio (ej: LAB-20260628-0001):"));
        pnlBusqueda.add(txtFolio);
        pnlBusqueda.add(btnGenerar);

        visor = new JEditorPane("text/html", "<html><body style='font-family:sans-serif;color:#7A8C87;'>"
                + "<p>Escribe un folio y presiona <b>Generar reporte</b>.</p></body></html>");
        visor.setEditable(false);

        btnImprimir = new JButton("Imprimir");
        btnImprimir.setEnabled(false);
        btnImprimir.addActionListener(e -> imprimir());
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.add(btnImprimir);

        add(pnlBusqueda, BorderLayout.NORTH);
        add(new JScrollPane(visor), BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void generarReporte() {
        String folio = txtFolio.getText().trim();
        if (folio.isBlank()) {
            JOptionPane.showMessageDialog(this, "Escriba el folio de la prueba.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<ReporteResultadoDTO> reporte = reporteNegocio.generarReportePorFolio(folio);
            if (reporte.isEmpty()) {
                visor.setText("<html><body style='font-family:sans-serif;'>"
                        + "<p>La prueba no tiene resultados capturados todavía.</p></body></html>");
                btnImprimir.setEnabled(false);
                return;
            }
            visor.setText(construirHtml(reporte));
            visor.setCaretPosition(0);
            btnImprimir.setEnabled(true);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            btnImprimir.setEnabled(false);
        }
    }

    private void imprimir() {
        try {
            visor.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo imprimir: " + ex.getMessage(),
                    "Error de impresión", JOptionPane.ERROR_MESSAGE);
        }
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
}
