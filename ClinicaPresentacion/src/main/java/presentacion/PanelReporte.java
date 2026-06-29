package presentacion;

import dto.ReporteResultadoDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
 * Panel del módulo "Reportes". Se escribe el folio, se genera el informe de
 * resultados (estilo informe clínico, con logo y código de barras) y se puede
 * enviar a la impresora.
 */
public class PanelReporte extends JPanel implements Recargable {

    private final IReporteNegocio reporteNegocio;

    private JTextField txtFolio;
    private ReporteVista vista;
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

        vista = new ReporteVista();
        JScrollPane scroll = new JScrollPane(vista);
        scroll.getViewport().setBackground(new Color(0xE4E9E8));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        btnImprimir = new JButton("Imprimir");
        btnImprimir.setEnabled(false);
        btnImprimir.addActionListener(e -> imprimir());
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.add(btnImprimir);

        add(pnlBusqueda, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
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
                vista.setDatos(null);
                btnImprimir.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "La prueba no tiene resultados capturados todavía.",
                        "Reporte vacío", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            vista.setDatos(reporte);
            btnImprimir.setEnabled(true);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            btnImprimir.setEnabled(false);
        }
    }

    private void imprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Informe de Resultados");
        job.setPrintable(vista);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo imprimir: " + ex.getMessage(),
                        "Error de impresión", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
