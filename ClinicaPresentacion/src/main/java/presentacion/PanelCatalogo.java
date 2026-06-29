package presentacion;

import dto.GuardarAnalisisDTO;
import dto.GuardarParametroDTO;
import entidad.AnalisisEntidad;
import entidad.TipoMuestraEntidad;
import negocio.ICatalogoNegocio;
import negocio.NegocioException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel del módulo "Catálogo de análisis". Registra tipos de análisis con sus
 * parámetros. Todos los datos (tipos de muestra, análisis) provienen de la base
 * de datos: no hay datos de prueba precargados.
 */
public class PanelCatalogo extends JPanel implements Recargable {

    private final ICatalogoNegocio catalogoNegocio;

    private JTextField txtNombreAnalisis;
    private JComboBox<TipoMuestraEntidad> cbTipoMuestra;
    private JTextField txtNotaDescriptiva;

    // ── Paleta de colores ────────────────────────────────────────────────────
    private static final Color C_TEAL        = new Color(0x2ECC8E);
    private static final Color C_TEAL_LIGHT  = new Color(0xE8F5F0);
    private static final Color C_TEAL_TEXT   = new Color(0x1A5C44);
    private static final Color C_BG          = new Color(0xF0F4F3);
    private static final Color C_WHITE       = Color.WHITE;
    private static final Color C_TEXT_DARK   = new Color(0x1A2E28);
    private static final Color C_TEXT_MUTED  = new Color(0x7A8C87);
    private static final Color C_BORDER      = new Color(0xDDE5E3);
    private static final Color C_ROW_ODD     = new Color(0xF7FAF9);
    private static final Color C_INPUT_BG    = new Color(0xF5F8F7);

    private static final Font F_BOLD_13  = new Font("SansSerif", Font.BOLD, 13);
    private static final Font F_BOLD_12  = new Font("SansSerif", Font.BOLD, 12);
    private static final Font F_PLAIN_12 = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font F_PLAIN_11 = new Font("SansSerif", Font.PLAIN, 11);

    // Parámetros capturados para el análisis en edición (vacío al inicio).
    private final List<String[]> parametros = new ArrayList<>();
    private DefaultTableModel tableModel;

    public PanelCatalogo(ICatalogoNegocio catalogoNegocio) {
        this.catalogoNegocio = catalogoNegocio;
        setLayout(new BorderLayout());
        setBackground(C_BG);
        add(buildContenido(), BorderLayout.CENTER);
        cargarTiposMuestra();
    }

    @Override
    public void recargar() {
        cargarTiposMuestra();
    }

    private void cargarTiposMuestra() {
        try {
            List<TipoMuestraEntidad> tipos = catalogoNegocio.buscarTiposMuestra();
            cbTipoMuestra.removeAllItems();
            for (TipoMuestraEntidad tipo : tipos) {
                cbTipoMuestra.addItem(tipo);
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de muestra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarAnalisisCompleto() {
        String nombre = txtNombreAnalisis.getText().trim();
        TipoMuestraEntidad tipoSeleccionado = (TipoMuestraEntidad) cbTipoMuestra.getSelectedItem();
        String nota = txtNotaDescriptiva.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del análisis es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de muestra.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un parámetro al análisis.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            GuardarAnalisisDTO analisisDTO = new GuardarAnalisisDTO();
            analisisDTO.setNombre(nombre);
            analisisDTO.setNota(nota.isEmpty() ? null : nota);
            analisisDTO.setTipoMuestraId(tipoSeleccionado.getId());

            AnalisisEntidad analisisGuardado = catalogoNegocio.registrarAnalisis(analisisDTO);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                GuardarParametroDTO parametroDTO = new GuardarParametroDTO();
                parametroDTO.setAnalisisId(analisisGuardado.getId());
                parametroDTO.setOrden(i + 1);

                parametroDTO.setNombre(tableModel.getValueAt(i, 1).toString());
                parametroDTO.setUnidadMedida(tableModel.getValueAt(i, 2).toString());

                // Mapeo según el String[] cols de buildTable()
                parametroDTO.setRangoInicial(Double.parseDouble(tableModel.getValueAt(i, 3).toString()));
                parametroDTO.setRangoFinal(Double.parseDouble(tableModel.getValueAt(i, 5).toString()));   // 5 = MÁX
                parametroDTO.setEdadInicial(Integer.parseInt(tableModel.getValueAt(i, 6).toString()));     // 6 = EDAD MÍN
                parametroDTO.setEdadFinal(Integer.parseInt(tableModel.getValueAt(i, 8).toString()));       // 8 = EDAD MÁX
                parametroDTO.setObservaciones(tableModel.getValueAt(i, 9).toString());                     // 9 = NOTE

                catalogoNegocio.registrarParametro(parametroDTO);
            }

            JOptionPane.showMessageDialog(this, "Análisis y sus parámetros guardados con éxito en la base de datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + e.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato: ingresa solo números en Rangos y Edades de la tabla.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN VISUAL
    // ════════════════════════════════════════════════════════════════════════
    private JScrollPane buildContenido() {
        JPanel content = new JPanel();
        content.setBackground(C_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 24, 24, 24));
        content.add(buildPageHeader());
        content.add(Box.createVerticalStrut(16));
        content.add(buildFormCard());
        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private JPanel buildPageHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_BG);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        JPanel left = new JPanel();
        left.setBackground(C_BG);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Catálogo de Análisis");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(C_TEXT_DARK);
        JLabel desc = new JLabel("<html>Registra y da de alta los tipos de análisis ofrecidos por el laboratorio.</html>");
        desc.setFont(F_PLAIN_11);
        desc.setForeground(C_TEXT_MUTED);
        left.add(title); left.add(Box.createVerticalStrut(4)); left.add(desc);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setBackground(C_BG);
        JButton newBtn = styledBtn("＋  Nuevo análisis", C_TEAL, C_WHITE);
        newBtn.addActionListener(e -> limpiarFormulario());
        right.add(newBtn);
        p.add(left, BorderLayout.WEST); p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel buildFormCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(C_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(16, 20, 16, 20)));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0; g.anchor = GridBagConstraints.NORTHWEST;

        g.gridy = 0; card.add(buildCardHeader(), g);
        g.gridy = 1; g.insets = new Insets(14, 0, 0, 0); card.add(buildInputRow(), g);
        g.gridy = 2; g.insets = new Insets(14, 0, 0, 0); card.add(buildParametrosSection(), g);
        g.gridy = 3; g.insets = new Insets(14, 0, 0, 0); card.add(new JSeparator(), g);
        g.gridy = 4; g.insets = new Insets(12, 0, 0, 0); card.add(buildCardFooter(), g);
        return card;
    }

    private JPanel buildCardHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_WHITE);
        JLabel title = new JLabel("  ＋  Registrar nuevo análisis");
        title.setFont(F_BOLD_13);
        title.setForeground(C_TEAL_TEXT);
        p.add(title, BorderLayout.WEST);
        return p;
    }

    private JPanel buildInputRow() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(C_WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTHWEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(0, 0, 0, 14);

        txtNombreAnalisis = new JTextField();
        txtNombreAnalisis.setFont(F_PLAIN_12);
        txtNombreAnalisis.setBackground(C_INPUT_BG);
        txtNombreAnalisis.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(6, 10, 6, 10)));

        cbTipoMuestra = new JComboBox<>();
        cbTipoMuestra.setFont(F_PLAIN_12);
        cbTipoMuestra.setBackground(C_INPUT_BG);
        cbTipoMuestra.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoMuestraEntidad) {
                    setText(((TipoMuestraEntidad) value).getNombre());
                }
                return this;
            }
        });

        txtNotaDescriptiva = new JTextField();
        txtNotaDescriptiva.setFont(F_PLAIN_12);
        txtNotaDescriptiva.setBackground(C_INPUT_BG);
        txtNotaDescriptiva.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(6, 10, 6, 10)));

        g.gridx = 0; g.weightx = 0.42; row.add(encapsularInput("Nombre del análisis *", txtNombreAnalisis), g);
        g.gridx = 1; g.weightx = 0.22; row.add(encapsularInput("Tipo de muestra *", cbTipoMuestra), g);
        g.gridx = 2; g.weightx = 0.36; g.insets = new Insets(0, 0, 0, 0); row.add(encapsularInput("Nota descriptiva", txtNotaDescriptiva), g);

        return row;
    }

    private JPanel encapsularInput(String title, JComponent comp) {
        JPanel p = new JPanel();
        p.setBackground(C_WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(title);
        l.setFont(F_PLAIN_11);
        l.setForeground(C_TEXT_MUTED);
        p.add(l); p.add(Box.createVerticalStrut(4));
        p.add(comp);
        return p;
    }

    private JPanel buildParametrosSection() {
        JPanel section = new JPanel(new GridBagLayout());
        section.setBackground(C_WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        JPanel subHdr = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        subHdr.setBackground(C_WHITE);
        JLabel t = new JLabel("Parámetros a evaluar");
        t.setFont(F_BOLD_12);
        subHdr.add(t);
        gc.gridy = 0; section.add(subHdr, gc);
        gc.gridy = 2; section.add(buildTable(), gc);

        JButton add = new JButton("  ＋  Agregar parámetro");
        add.setFont(F_BOLD_12);
        add.setForeground(C_TEAL_TEXT);
        add.setBackground(C_TEAL_LIGHT);
        add.addActionListener(e -> agregarParametro());
        GridBagConstraints gcBtn = new GridBagConstraints();
        gcBtn.gridy = 4; gcBtn.anchor = GridBagConstraints.WEST;
        section.add(add, gcBtn);
        return section;
    }

    private JScrollPane buildTable() {
        String[] cols = {"ORDEN", "NOMBRE DEL PARÁMETRO", "UNIDAD", "MÍN", "—", "MÁX", "EDAD MÍN", "a", "EDAD MÁX", "NOTE", ""};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c != 0 && c != 4 && c != 7 && c != 10; }
        };
        refreshTable();

        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? C_WHITE : C_ROW_ODD);
                return c;
            }
        };
        table.setRowHeight(36);
        table.setShowGrid(false);

        table.getColumnModel().getColumn(10).setCellRenderer(new DeleteBtnRenderer());
        table.getColumnModel().getColumn(10).setCellEditor(new DeleteBtnEditor(new JCheckBox(), tableModel, parametros));

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(0, 148));
        return sp;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (String[] p : parametros) {
            tableModel.addRow(new Object[]{"", p[0], p[1], p[2], "—", p[3], p[4], "a", p[5], p[6], "🗑"});
        }
    }

    private JPanel buildCardFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setBackground(C_WHITE);
        JButton cancelBtn = styledBtn("Cancelar", C_WHITE, C_TEXT_DARK);
        JButton saveBtn = styledBtn("✓  Guardar análisis", C_TEAL, C_WHITE);

        cancelBtn.addActionListener(e -> limpiarFormulario());
        saveBtn.addActionListener(e -> guardarAnalisisCompleto());

        p.add(cancelBtn); p.add(saveBtn);
        return p;
    }

    private JButton styledBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(F_BOLD_12);
        btn.setBackground(bg);
        btn.setForeground(fg);
        return btn;
    }

    private void limpiarFormulario() {
        txtNombreAnalisis.setText("");
        txtNotaDescriptiva.setText("");
        parametros.clear();
        refreshTable();
    }

    private void agregarParametro() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del parámetro:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            // {nombre, unidad, min, max, edadMin, edadMax, nota}
            parametros.add(new String[]{nombre.trim(), "mg/dL", "0", "100", "18", "99", "—"});
            refreshTable();
        }
    }

    static class DeleteBtnRenderer extends JButton implements TableCellRenderer {
        DeleteBtnRenderer() { setText("🗑"); }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }

    static class DeleteBtnEditor extends DefaultCellEditor {
        private final DefaultTableModel model;
        private final List<String[]> data;
        private int currentRow;
        DeleteBtnEditor(JCheckBox cb, DefaultTableModel model, List<String[]> data) {
            super(cb); this.model = model; this.data = data;
            JButton btn = new JButton("🗑");
            btn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow >= 0 && currentRow < data.size()) {
                    data.remove(currentRow); model.removeRow(currentRow);
                }
            });
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { currentRow = r; return editorComponent; }
    }
}
