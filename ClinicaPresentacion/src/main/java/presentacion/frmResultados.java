/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dto.GuardarResultadoDTO;
import entidad.PruebaEntidad;
import entidad.ResultadoEntidad;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import negocio.IResultadoNegocio;
import negocio.IPruebaNegocio;
import negocio.NegocioException;
import negocio.PruebaNegocio;
import negocio.ResultadoNegocio;

import persistencia.IConexionBD;

/**
 * Módulo 3: Ingreso de resultados.
 * El laboratorista busca la prueba por folio y captura el valor
 * de cada parámetro.
 *
 * @author dylannvms
 */
public class frmResultados extends JFrame {

    private final IResultadoNegocio resultadoNegocio;
    private final IPruebaNegocio pruebaNegocio;

    private JTextField txtFolio;
    private JTable tblResultados;
    private DefaultTableModel modeloTabla;
    private PruebaEntidad pruebaActual;

    private static final int COL_PARAMETRO_ID = 0;
    private static final int COL_PARAMETRO = 1;
    private static final int COL_UNIDAD = 2;
    private static final int COL_RANGO = 3;
    private static final int COL_VALOR = 4;
    private static final int COL_OBSERVACION = 5;

    public frmResultados(IConexionBD conexion) {
        this.resultadoNegocio = new ResultadoNegocio(conexion);
        this.pruebaNegocio = new PruebaNegocio(conexion);
        initComponents();
    }

    private void initComponents() {
        setTitle("Ingreso de Resultados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Panel búsqueda por folio
        JPanel pnlBusqueda = new JPanel(new GridLayout(1, 3, 8, 0));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar prueba por folio"));
        txtFolio = new JTextField();
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPrueba());
        pnlBusqueda.add(new JLabel("Folio (ej: LAB-20260628-0001):"));
        pnlBusqueda.add(txtFolio);
        pnlBusqueda.add(btnBuscar);

        // Tabla de parámetros y valores
        String[] columnas = {"ID", "Parámetro", "Unidad", "Rango normal", "Valor", "Observación"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == COL_VALOR || col == COL_OBSERVACION;
            }
        };
        tblResultados = new JTable(modeloTabla);

        // Ocultar columna ID (uso interno)
        tblResultados.getColumnModel().getColumn(COL_PARAMETRO_ID).setMinWidth(0);
        tblResultados.getColumnModel().getColumn(COL_PARAMETRO_ID).setMaxWidth(0);
        tblResultados.getColumnModel().getColumn(COL_PARAMETRO_ID).setWidth(0);

        // Botón guardar
        JButton btnGuardar = new JButton("Guardar resultados");
        btnGuardar.addActionListener(e -> guardarResultados());

        add(pnlBusqueda, BorderLayout.NORTH);
        add(new JScrollPane(tblResultados), BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void buscarPrueba() {
        String folio = txtFolio.getText().trim();
        if (folio.isBlank()) {
            mostrarError("Ingrese el folio de la prueba.");
            return;
        }
        try {
            pruebaActual = pruebaNegocio.buscarPorFolio(folio);
            if (pruebaActual == null) {
                mostrarError("No se encontró ninguna prueba con el folio: " + folio);
                modeloTabla.setRowCount(0);
                return;
            }
            cargarResultados();
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarResultados() {
        modeloTabla.setRowCount(0);
        try {
            List<ResultadoEntidad> resultados
                    = resultadoNegocio.buscarResultadosPorPrueba(pruebaActual.getId());

            for (ResultadoEntidad r : resultados) {
                String rango = "";
                if (r.getParametro().getRangoInicial() != null
                        && r.getParametro().getRangoFinal() != null) {
                    rango = r.getParametro().getRangoInicial()
                            + " - " + r.getParametro().getRangoFinal();
                }
                modeloTabla.addRow(new Object[]{
                    r.getParametro().getId(),
                    r.getParametro().getNombre(),
                    r.getParametro().getUnidadMedida() != null
                    ? r.getParametro().getUnidadMedida() : "",
                    rango,
                    r.getValor() != null ? r.getValor() : "",
                    r.getObservacion() != null ? r.getObservacion() : ""
                });
            }
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void guardarResultados() {
        if (pruebaActual == null) {
            mostrarError("Primero busque una prueba por folio.");
            return;
        }
        // Confirmar celda activa antes de guardar
        if (tblResultados.isEditing()) {
            tblResultados.getCellEditor().stopCellEditing();
        }
        try {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                Integer parametroId = (Integer) modeloTabla.getValueAt(i, COL_PARAMETRO_ID);
                String valor = (String) modeloTabla.getValueAt(i, COL_VALOR);
                String observacion = (String) modeloTabla.getValueAt(i, COL_OBSERVACION);

                GuardarResultadoDTO dto = new GuardarResultadoDTO(
                        pruebaActual.getId(), parametroId, valor, observacion);
                resultadoNegocio.ingresarResultado(dto);
            }
            JOptionPane.showMessageDialog(this,
                    "Resultados guardados correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
