package presentacion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import negocio.CatalogoNegocio;
import negocio.ICatalogoNegocio;
import persistencia.ConexionBD;
import persistencia.IConexionBD;

/**
 * Ventana principal de la aplicación. Es un único shell con un sidebar a la
 * izquierda que cambia entre los cuatro módulos del laboratorio dentro de la
 * misma ventana (sin un menú intermedio de botones).
 *
 * @author Cristian Devora
 */
public class frmPrincipal extends JFrame {

    // ── Paleta del sidebar ───────────────────────────────────────────────────
    private static final Color C_SIDEBAR_BG   = new Color(0x1A3C34);
    private static final Color C_SIDEBAR_ITEM = new Color(0x2D5E4F);
    private static final Color C_TEAL         = new Color(0x2ECC8E);
    private static final Color C_WHITE        = Color.WHITE;
    private static final Color C_TEXT_MUTED   = new Color(0x7A8C87);
    private static final Color C_BG           = new Color(0xF0F4F3);

    private static final Font F_BOLD_13  = new Font("SansSerif", Font.BOLD, 13);
    private static final Font F_BOLD_12  = new Font("SansSerif", Font.BOLD, 12);
    private static final Font F_BOLD_11  = new Font("SansSerif", Font.BOLD, 11);
    private static final Font F_PLAIN_12 = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font F_PLAIN_10 = new Font("SansSerif", Font.PLAIN, 10);

    private final JPanel contenido = new JPanel(new CardLayout());
    private final Map<String, JPanel> navItems = new LinkedHashMap<>();
    private final Map<String, JPanel> paneles = new LinkedHashMap<>();

    public frmPrincipal(IConexionBD conexion) {
        ICatalogoNegocio catalogoNegocio = new CatalogoNegocio(conexion);

        // Un panel por módulo, todos dentro de la misma ventana.
        paneles.put("catalogo", new PanelCatalogo(catalogoNegocio));
        paneles.put("solicitud", new PanelSolicitud(conexion));
        paneles.put("resultados", new PanelResultados(conexion));
        paneles.put("reporte", new PanelReporte(conexion));

        for (Map.Entry<String, JPanel> e : paneles.entrySet()) {
            contenido.add(e.getValue(), e.getKey());
        }
        contenido.setBackground(C_BG);

        initComponents();
        seleccionar("catalogo");
    }

    private void initComponents() {
        setTitle("Laboratorio Clínico \"Salud Total\"");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(940, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);
    }

    // ── SIDEBAR ──────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(C_SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.add(buildLogoPanel(), BorderLayout.NORTH);
        sidebar.add(buildNavPanel(), BorderLayout.CENTER);
        sidebar.add(buildUserFooter(), BorderLayout.SOUTH);
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
        JLabel sub = new JLabel("Laboratorio Clínico");
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
        nav.add(crearNavItem("🧪", "Catálogo de análisis", "catalogo"));
        nav.add(crearNavItem("📋", "Solicitudes", "solicitud"));
        nav.add(crearNavItem("📥", "Ingreso de resultados", "resultados"));
        nav.add(crearNavItem("📊", "Reportes", "reporte"));
        return nav;
    }

    private JPanel crearNavItem(String icon, String label, String card) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        item.setOpaque(true);
        item.setBackground(C_SIDEBAR_BG);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setAlignmentX(LEFT_ALIGNMENT);
        item.setBorder(new EmptyBorder(0, 14, 0, 0));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel ico = new JLabel(icon);
        JLabel txt = new JLabel(label);
        txt.setFont(F_PLAIN_12);
        txt.setForeground(C_TEXT_MUTED);
        item.add(ico);
        item.add(txt);
        item.putClientProperty("txt", txt);

        MouseAdapter click = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionar(card);
            }
        };
        item.addMouseListener(click);
        ico.addMouseListener(click);
        txt.addMouseListener(click);

        navItems.put(card, item);
        return item;
    }

    private JPanel buildUserFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        p.setBackground(C_SIDEBAR_BG);
        p.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0x28524A)));
        JLabel av = new JLabel("LM", SwingConstants.CENTER);
        av.setFont(F_BOLD_11);
        av.setForeground(C_WHITE);
        av.setBackground(new Color(0x5B7C72));
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

    // ── NAVEGACIÓN ───────────────────────────────────────────────────────────
    private void seleccionar(String card) {
        for (Map.Entry<String, JPanel> e : navItems.entrySet()) {
            boolean activo = e.getKey().equals(card);
            JPanel item = e.getValue();
            item.setBackground(activo ? C_SIDEBAR_ITEM : C_SIDEBAR_BG);
            item.setBorder(activo
                    ? BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, C_TEAL), new EmptyBorder(0, 11, 0, 0))
                    : new EmptyBorder(0, 14, 0, 0));
            JLabel txt = (JLabel) item.getClientProperty("txt");
            txt.setForeground(activo ? C_WHITE : C_TEXT_MUTED);
        }

        ((CardLayout) contenido.getLayout()).show(contenido, card);

        JPanel panel = paneles.get(card);
        if (panel instanceof Recargable recargable) {
            recargable.recargar();
        }
    }

    // ── ENTRADA ──────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si falla, se usa el look and feel por defecto.
        }
        IConexionBD conexion = new ConexionBD();
        SwingUtilities.invokeLater(() -> new frmPrincipal(conexion).setVisible(true));
    }
}
