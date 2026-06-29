package presentacion;

import dto.GuardarClienteDTO;
import dto.GuardarDoctorDTO;
import dto.GuardarPruebaDTO;
import entidad.AnalisisEntidad;
import entidad.ClienteEntidad;
import entidad.DoctorEntidad;
import entidad.PruebaEntidad;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import negocio.CatalogoNegocio;
import negocio.ClienteNegocio;
import negocio.DoctorNegocio;
import negocio.ICatalogoNegocio;
import negocio.IClienteNegocio;
import negocio.IDoctorNegocio;
import negocio.IPruebaNegocio;
import negocio.NegocioException;
import negocio.PruebaNegocio;
import persistencia.IConexionBD;

/**
 * Panel del módulo "Solicitudes". Registra o selecciona el cliente, elige doctor
 * (o ninguno), selecciona los análisis a realizar y genera la orden con un folio
 * único.
 */
public class PanelSolicitud extends JPanel implements Recargable {

    private final IClienteNegocio clienteNegocio;
    private final IDoctorNegocio doctorNegocio;
    private final ICatalogoNegocio catalogoNegocio;
    private final IPruebaNegocio pruebaNegocio;

    private JComboBox<ClienteEntidad> cboClientes;
    private JComboBox<Object> cboDoctores;
    private JPanel pnlAnalisis;
    private final List<JCheckBox> checksAnalisis;

    private JTextField txtNombreCliente;
    private JTextField txtFechaNacimiento;
    private JComboBox<String> cboSexo;
    private JTextField txtTipoSangre;

    private static final String SIN_DOCTOR = "Sin doctor";

    public PanelSolicitud(IConexionBD conexion) {
        this.clienteNegocio = new ClienteNegocio(conexion);
        this.doctorNegocio = new DoctorNegocio(conexion);
        this.catalogoNegocio = new CatalogoNegocio(conexion);
        this.pruebaNegocio = new PruebaNegocio(conexion);
        this.checksAnalisis = new ArrayList<>();
        initComponents();
        cargarDatos();
    }

    @Override
    public void recargar() {
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        principal.add(crearPanelClienteNuevo());
        principal.add(Box.createVerticalStrut(8));
        principal.add(crearPanelClienteExistente());
        principal.add(Box.createVerticalStrut(8));
        principal.add(crearPanelDoctor());
        principal.add(Box.createVerticalStrut(8));
        principal.add(crearPanelAnalisis());

        JButton btnGenerar = new JButton("Generar Orden");
        btnGenerar.addActionListener(e -> generarOrden());

        add(new JScrollPane(principal), BorderLayout.CENTER);
        add(btnGenerar, BorderLayout.SOUTH);
    }

    private JPanel crearPanelClienteNuevo() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar cliente nuevo"));

        txtNombreCliente = new JTextField();
        txtFechaNacimiento = new JTextField();
        cboSexo = new JComboBox<>(new String[]{"M", "F"});
        txtTipoSangre = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombreCliente);
        panel.add(new JLabel("Fecha nacimiento (aaaa-mm-dd):"));
        panel.add(txtFechaNacimiento);
        panel.add(new JLabel("Sexo:"));
        panel.add(cboSexo);
        panel.add(new JLabel("Tipo de sangre:"));
        panel.add(txtTipoSangre);

        JButton btnRegistrar = new JButton("Registrar cliente");
        btnRegistrar.addActionListener(e -> registrarCliente());
        panel.add(new JLabel());
        panel.add(btnRegistrar);

        return panel;
    }

    private JPanel crearPanelClienteExistente() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Cliente de la solicitud"));
        cboClientes = new JComboBox<>();
        panel.add(new JLabel("Cliente:"), BorderLayout.WEST);
        panel.add(cboClientes, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelDoctor() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Doctor"));
        cboDoctores = new JComboBox<>();
        JButton btnNuevoDoctor = new JButton("Nuevo doctor");
        btnNuevoDoctor.addActionListener(e -> registrarDoctor());
        panel.add(new JLabel("Doctor:"), BorderLayout.WEST);
        panel.add(cboDoctores, BorderLayout.CENTER);
        panel.add(btnNuevoDoctor, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelAnalisis() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Analisis a realizar"));
        pnlAnalisis = new JPanel();
        pnlAnalisis.setLayout(new BoxLayout(pnlAnalisis, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(pnlAnalisis);
        scroll.setPreferredSize(new java.awt.Dimension(520, 160));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void cargarDatos() {
        cargarClientes();
        cargarDoctores();
        cargarAnalisis();
    }

    private void cargarClientes() {
        try {
            List<ClienteEntidad> clientes = clienteNegocio.buscarTodos();
            cboClientes.setModel(new DefaultComboBoxModel<>(clientes.toArray(new ClienteEntidad[0])));
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarDoctores() {
        try {
            List<DoctorEntidad> doctores = doctorNegocio.buscarTodos();
            DefaultComboBoxModel<Object> modelo = new DefaultComboBoxModel<>();
            modelo.addElement(SIN_DOCTOR);
            for (DoctorEntidad d : doctores) {
                modelo.addElement(d);
            }
            cboDoctores.setModel(modelo);
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarAnalisis() {
        try {
            pnlAnalisis.removeAll();
            checksAnalisis.clear();
            for (AnalisisEntidad a : catalogoNegocio.buscarTodosAnalisis()) {
                JCheckBox check = new JCheckBox(a.getNombre());
                check.putClientProperty("analisisId", a.getId());
                checksAnalisis.add(check);
                pnlAnalisis.add(check);
            }
            pnlAnalisis.revalidate();
            pnlAnalisis.repaint();
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void registrarCliente() {
        try {
            if (txtNombreCliente.getText().isBlank()) {
                mostrarError("Capture el nombre del cliente.");
                return;
            }
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(txtFechaNacimiento.getText().trim());
            GuardarClienteDTO dto = new GuardarClienteDTO(
                    txtNombreCliente.getText().trim(),
                    fecha,
                    (String) cboSexo.getSelectedItem(),
                    txtTipoSangre.getText().trim());
            clienteNegocio.registrarCliente(dto);
            JOptionPane.showMessageDialog(this, "Cliente registrado.");
            limpiarCliente();
            cargarClientes();
        } catch (java.text.ParseException e) {
            mostrarError("La fecha de nacimiento debe tener el formato aaaa-mm-dd.");
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void registrarDoctor() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del doctor:");
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        Object[] opciones = {"M", "F"};
        int sel = JOptionPane.showOptionDialog(this, "Sexo:", "Nuevo doctor",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        String sexo = sel == 1 ? "F" : "M";
        try {
            doctorNegocio.registrarDoctor(new GuardarDoctorDTO(nombre.trim(), sexo));
            cargarDoctores();
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void generarOrden() {
        ClienteEntidad cliente = (ClienteEntidad) cboClientes.getSelectedItem();
        if (cliente == null) {
            mostrarError("Seleccione o registre un cliente.");
            return;
        }

        Integer doctorId = null;
        Object doctorSel = cboDoctores.getSelectedItem();
        if (doctorSel instanceof DoctorEntidad doctor) {
            doctorId = doctor.getId();
        }

        List<Integer> analisisIds = new ArrayList<>();
        for (JCheckBox check : checksAnalisis) {
            if (check.isSelected()) {
                analisisIds.add((Integer) check.getClientProperty("analisisId"));
            }
        }
        if (analisisIds.isEmpty()) {
            mostrarError("Seleccione al menos un analisis.");
            return;
        }

        try {
            GuardarPruebaDTO dto = new GuardarPruebaDTO(cliente.getId(), doctorId, analisisIds);
            PruebaEntidad prueba = pruebaNegocio.crearSolicitud(dto);
            JOptionPane.showMessageDialog(this,
                    "Orden generada correctamente.\nFolio: " + prueba.getFolio(),
                    "Solicitud creada", JOptionPane.INFORMATION_MESSAGE);
            limpiarSeleccion();
        } catch (NegocioException e) {
            mostrarError(e.getMessage());
        }
    }

    private void limpiarCliente() {
        txtNombreCliente.setText("");
        txtFechaNacimiento.setText("");
        txtTipoSangre.setText("");
        cboSexo.setSelectedIndex(0);
    }

    private void limpiarSeleccion() {
        for (JCheckBox check : checksAnalisis) {
            check.setSelected(false);
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
