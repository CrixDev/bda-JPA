/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CatalogoAnalisis - Interfaz para registrar análisis de laboratorio.
 * Proyecto NetBeans JPA - Salud Total Laboratorio Clínico
 * Versión mejorada y conectada al backend de Negocio.
 */
public class CatalogoAnalisis extends JFrame {

    // ── Capa de Negocio Inyectada ────────────────────────────────────────────
    private final ICatalogoNegocio catalogoNegocio;

    // ── Componentes Globales para recuperar datos ───────────────────────────
    private JTextField txtNombreAnalisis;
    private JComboBox<TipoMuestraEntidad> cbTipoMuestra; 
    private JTextField txtNotaDescriptiva;

    // ── Paleta de colores ────────────────────────────────────────────────────
    private static final Color C_SIDEBAR_BG    = new Color(0x1A3C34);
    private static final Color C_SIDEBAR_ITEM  = new Color(0x2D5E4F);
    private static final Color C_TEAL          = new Color(0x2ECC8E);
    private static final Color C_TEAL_DARK     = new Color(0x27AE73);
    private static final Color C_TEAL_LIGHT    = new Color(0xE8F5F0);
    private static final Color C_TEAL_BORDER   = new Color(0xB2DECE);
    private static final Color C_TEAL_TEXT     = new Color(0x1A5C44);
    private static final Color C_BG            = new Color(0xF0F4F3);
    private static final Color C_WHITE         = Color.WHITE;
    private static final Color C_TEXT_DARK     = new Color(0x1A2E28);
    private static final Color C_TEXT_MUTED    = new Color(0x7A8C87);
    private static final Color C_BORDER        = new Color(0xDDE5E3);
    private static final Color C_ROW_ODD       = new Color(0xF7FAF9);
    private static final Color C_INPUT_BG      = new Color(0xF5F8F7);
    private static final Color C_RED_BG        = new Color(0xFFECEC);
    private static final Color C_RED           = new Color(0xE05252);
    private static final Color C_USER_AVATAR   = new Color(0x5B7C72);

    // ── Fuentes ──────────────────────────────────────────────────────────────
    private static final Font F_BOLD_13  = new Font("SansSerif", Font.BOLD, 13);
    private static final Font F_BOLD_12  = new Font("SansSerif", Font.BOLD, 12);
    private static final Font F_BOLD_11  = new Font("SansSerif", Font.BOLD, 11);
    private static final Font F_BOLD_10  = new Font("SansSerif", Font.BOLD, 10);
    private static final Font F_PLAIN_12 = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font F_PLAIN_11 = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font F_PLAIN_10 = new Font("SansSerif", Font.PLAIN, 10);

    // ── Estado de la Tabla ───────────────────────────────────────────────────
    private final List<String[]> parametros = new ArrayList<>();
    private DefaultTableModel tableModel;

    // ── Constructor con Inyección de Negocio ─────────────────────────────────
    public CatalogoAnalisis(ICatalogoNegocio catalogoNegocio) {
        this.catalogoNegocio = catalogoNegocio;

        // Datos demo iniciales
        parametros.add(new String[]{"Glucosa",           "mg/dL", "70", "100", "18", "99", "En ayuno"});
        parametros.add(new String[]{"Colesterol total", "mg/dL", "0",  "200", "18", "99", "—"});
        parametros.add(new String[]{"Triglicéridos",   "mg/dL", "0",  "150", "18", "99", "—"});

        setTitle("Salud Total – Catálogo de Análisis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 660);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        setBackground(C_BG);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMain(),    BorderLayout.CENTER);

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

    // ── MÉTODO PARA INSERCIÓN DE PRUEBA AUTOMÁTICA ───────────────────────────
    private void ejecutarInsertsDePrueba() {
        try {
            // 1. Verificar si hay tipos de muestra registrados en la base de datos
            List<TipoMuestraEntidad> tipos = catalogoNegocio.buscarTiposMuestra();
            if (tipos.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor registra o asegura un Tipo de Muestra en la base de datos primero.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Tomamos el primer tipo de muestra disponible para la prueba
            TipoMuestraEntidad tipoDemo = tipos.get(0);

            // 2. Crear y registrar el Análisis de prueba
            GuardarAnalisisDTO analisisDTO = new GuardarAnalisisDTO();
            analisisDTO.setNombre("Perfil Lipídico Automatizado (" + (System.currentTimeMillis() % 1000) + ")");
            analisisDTO.setNota("Prueba de inserción rápida desde la barra superior.");
            analisisDTO.setTipoMuestraId(tipoDemo.getId());

            AnalisisEntidad analisisGuardado = catalogoNegocio.registrarAnalisis(analisisDTO);

            // 3. Crear y registrar el Parámetro asociado usando tipos Integer y Double exactos
            GuardarParametroDTO p1 = new GuardarParametroDTO();
            p1.setAnalisisId(analisisGuardado.getId()); // Usa el Integer del ID recién generado
            p1.setOrden(1);
            p1.setNombre("Lípidos Totales");
            p1.setUnidadMedida("mg/dL");
            p1.setRangoInicial(400.0);
            p1.setRangoFinal(800.0);
            p1.setEdadInicial(18);
            p1.setEdadFinal(90);
            p1.setObservaciones("Ayuno de 12 horas obligatorio.");
            
            // Enviar al backend de negocio
            catalogoNegocio.registrarParametro(p1);

            JOptionPane.showMessageDialog(this, 
                "¡Éxito! Se insertó el análisis '" + analisisDTO.getNombre() + "' y sus parámetros de prueba.", 
                "Prueba Exitosa", JOptionPane.INFORMATION_MESSAGE);
            
            // Refrescar el combo de la pantalla por si acaso
            cargarTiposMuestra();
            
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error en la inserción de prueba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        try {
            GuardarAnalisisDTO analisisDTO = new GuardarAnalisisDTO();
            analisisDTO.setNombre(nombre);
            analisisDTO.setNota(nota.isEmpty() ? null : nota);
            analisisDTO.setTipoMuestraId(tipoSeleccionado.getId());

            AnalisisEntidad analisisGuardado = catalogoNegocio.registrarAnalisis(analisisDTO);

            // CORRECCIÓN CRÍTICA DE ÍNDICES DE COLUMNAS DE LA TABLA
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                GuardarParametroDTO parametroDTO = new GuardarParametroDTO();
                parametroDTO.setAnalisisId(analisisGuardado.getId());
                parametroDTO.setOrden(i + 1);
                
                parametroDTO.setNombre(tableModel.getValueAt(i, 1).toString());
                parametroDTO.setUnidadMedida(tableModel.getValueAt(i, 2).toString());

                // Mapeo corregido según el String[] cols del método buildTable()
                parametroDTO.setRangoInicial(Double.parseDouble(tableModel.getValueAt(i, 3).toString()));
                parametroDTO.setRangoFinal(Double.parseDouble(tableModel.getValueAt(i, 5).toString())); // Índice 5 es MÁX

                parametroDTO.setEdadInicial(Integer.parseInt(tableModel.getValueAt(i, 6).toString()));  // Índice 6 es EDAD MÍN
                parametroDTO.setEdadFinal(Integer.parseInt(tableModel.getValueAt(i, 8).toString()));    // Índice 8 es EDAD MÁX

                parametroDTO.setObservaciones(tableModel.getValueAt(i, 9).toString()); // Índice 9 es NOTE

                catalogoNegocio.registrarParametro(parametroDTO);
            }

            JOptionPane.showMessageDialog(this, "Análisis y sus parámetros guardados con éxito en la base de datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + e.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato: Asegúrate de ingresar solo números en los Rangos y Edades de la tabla.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN VISUAL
    // ════════════════════════════════════════════════════════════════════════
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(C_SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(205, 0));
        sidebar.add(buildLogoPanel(),   BorderLayout.NORTH);
        sidebar.add(buildNavPanel(),    BorderLayout.CENTER);
        sidebar.add(buildUserFooter(),  BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel buildLogoPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        p.setBackground(C_SIDEBAR_BG);
        p.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0x28524A)));
        JLabel badge = new JLabel("ST", SwingConstants.CENTER);
        badge.setFont(F_BOLD_13);
        badge.setForeground(C_SIDEBAR_BG);
        badge.setBackground(C_TEAL);
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(36, 36));

        JPanel textPanel = new JPanel();
        textPanel.setBackground(C_SIDEBAR_BG);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Salud Total");
        name.setFont(F_BOLD_12);
        name.setForeground(C_WHITE);
        JLabel sub  = new JLabel("Laboratorio Clínico");
        sub.setFont(F_PLAIN_10);
        sub.setForeground(C_TEXT_MUTED);
        textPanel.add(name);
        textPanel.add(sub);
        p.add(badge);
        p.add(textPanel);
        return p;
    }

    private JPanel buildNavPanel() {
        JPanel nav = new JPanel();
        nav.setBackground(C_SIDEBAR_BG);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel section = new JLabel("  OPERACIÓN");
        section.setFont(new Font("SansSerif", Font.BOLD, 9));
        section.setForeground(C_TEXT_MUTED);
        section.setBorder(new EmptyBorder(6, 14, 6, 0));
        section.setAlignmentX(LEFT_ALIGNMENT);
        nav.add(section);
        nav.add(navItem("🧪", "Catálogo de análisis", true));
        nav.add(navItem("📋", "Solicitudes",            false));
        nav.add(navItem("📥", "Ingreso de resultados", false));
        nav.add(navItem("📊", "Reportes",              false));
        return nav;
    }

    private JPanel navItem(String icon, String label, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        item.setAlignmentX(LEFT_ALIGNMENT);
        item.setBackground(active ? C_SIDEBAR_ITEM : C_SIDEBAR_BG);
        item.setBorder(active ? BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, C_TEAL), new EmptyBorder(0, 11, 0, 0)) : new EmptyBorder(0, 14, 0, 0));
        JLabel ico = new JLabel(icon);
        JLabel txt = new JLabel(label);
        txt.setFont(F_PLAIN_12);
        txt.setForeground(active ? C_WHITE : C_TEXT_MUTED);
        item.add(ico);
        item.add(txt);
        return item;
    }

    private JPanel buildUserFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        p.setBackground(C_SIDEBAR_BG);
        p.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0x28524A)));
        JLabel av = new JLabel("LM", SwingConstants.CENTER);
        av.setFont(F_BOLD_11);
        av.setForeground(C_WHITE);
        av.setBackground(C_USER_AVATAR);
        av.setOpaque(true);
        av.setPreferredSize(new Dimension(32, 32));

        JPanel info = new JPanel();
        info.setBackground(C_SIDEBAR_BG);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel uname = new JLabel("Q.F.B. Laura Méndez");
        uname.setFont(F_BOLD_11);
        uname.setForeground(C_WHITE);
        JLabel urole = new JLabel("Laboratorista");
        urole.setFont(F_PLAIN_10);
        urole.setForeground(C_TEXT_MUTED);
        info.add(uname);
        info.add(urole);
        p.add(av);
        p.add(info);
        return p;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(C_BG);
        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(buildContent(), BorderLayout.CENTER);
        return main;
    }

    // ── BOTÓN INTEGRADO SIN DES ACOMODAR LA PÁGINA ───────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(C_WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, C_BORDER), new EmptyBorder(9, 20, 9, 20)));
        JLabel bc = new JLabel("<html>Operación / <b>Catálogo de análisis</b></html>");
        bc.setFont(F_PLAIN_11);
        bc.setForeground(C_TEXT_MUTED);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setBackground(C_WHITE);
        
        // El botón de prueba se añade elegantemente a la izquierda de la barra de búsqueda
        JButton btnInsertTest = new JButton("⚡ Inserciones de Prueba");
        btnInsertTest.setFont(F_BOLD_11);
        btnInsertTest.setBackground(new Color(0xFFF9E6)); // Color crema cálido
        btnInsertTest.setForeground(new Color(0xD9822B)); // Naranja quemado profesional
        btnInsertTest.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xF2D0A7)), new EmptyBorder(5, 10, 5, 10)));
        btnInsertTest.addActionListener(e -> ejecutarInsertsDePrueba());
        right.add(btnInsertTest);

        JTextField search = new JTextField("Buscar análisis...", 20);
        search.setFont(F_PLAIN_11);
        search.setBackground(C_INPUT_BG);
        search.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C_BORDER), new EmptyBorder(5, 10, 5, 10)));
        right.add(search);
        
        bar.add(bc, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JScrollPane buildContent() {
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
        JButton newBtn = styledBtn("＋  Nuevo análisis", C_TEAL, C_WHITE, C_TEAL);
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
        JButton cancelBtn = styledBtn("Cancelar", C_WHITE, C_TEXT_DARK, C_BORDER);
        JButton saveBtn = styledBtn("✓  Guardar análisis", C_TEAL, C_WHITE, C_TEAL);

        cancelBtn.addActionListener(e -> limpiarFormulario());
        saveBtn.addActionListener(e -> guardarAnalisisCompleto());

        p.add(cancelBtn); p.add(saveBtn);
        return p;
    }

    private JButton styledBtn(String text, Color bg, Color fg, Color border) {
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
