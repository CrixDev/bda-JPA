package negocio;

import daos.ClienteDAO;
import daos.DoctorDAO;
import daos.IClienteDAO;
import daos.IDoctorDAO;
import daos.IParametroDAO;
import daos.IPruebaDAO;
import daos.ParametroDAO;
import daos.PruebaDAO;
import dto.GuardarPruebaDTO;
import entidad.ClienteEntidad;
import entidad.DoctorEntidad;
import entidad.ParametroEntidad;
import entidad.PruebaEntidad;
import entidad.ResultadoEntidad;
import java.text.SimpleDateFormat;
import java.util.Date;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación de {@link IPruebaNegocio}. Genera el folio único de cada prueba
 * y pre-crea los renglones de resultado (vacíos) para cada parámetro de los
 * análisis seleccionados, de modo que la captura de resultados sepa qué medir.
 *
 * @author Cristian Devora
 */
public class PruebaNegocio implements IPruebaNegocio {

    private final IPruebaDAO pruebaDAO;
    private final IClienteDAO clienteDAO;
    private final IDoctorDAO doctorDAO;
    private final IParametroDAO parametroDAO;

    public PruebaNegocio(IConexionBD conexion) {
        this.pruebaDAO = new PruebaDAO(conexion);
        this.clienteDAO = new ClienteDAO(conexion);
        this.doctorDAO = new DoctorDAO(conexion);
        this.parametroDAO = new ParametroDAO(conexion);
    }

    @Override
    public PruebaEntidad crearSolicitud(GuardarPruebaDTO datos) throws NegocioException {
        if (datos == null || datos.getClienteId() == null) {
            throw new NegocioException("Debe seleccionar un cliente para la solicitud.");
        }
        if (datos.getListaAnalisisIds() == null || datos.getListaAnalisisIds().isEmpty()) {
            throw new NegocioException("Debe seleccionar al menos un analisis.");
        }
        try {
            ClienteEntidad cliente = clienteDAO.buscarPorId(datos.getClienteId());
            if (cliente == null) {
                throw new NegocioException("El cliente seleccionado no existe.");
            }

            DoctorEntidad doctor = null;
            if (datos.getDoctorId() != null) {
                doctor = doctorDAO.buscarPorId(datos.getDoctorId());
            }

            Date ahora = new Date();
            String folio = generarFolio(ahora);

            PruebaEntidad prueba = new PruebaEntidad(folio, ahora, cliente, doctor);

            // Pre-crear un resultado vacio por cada parametro de cada analisis pedido.
            for (Integer analisisId : datos.getListaAnalisisIds()) {
                for (ParametroEntidad parametro : parametroDAO.buscarPorAnalisis(analisisId)) {
                    ResultadoEntidad resultado = new ResultadoEntidad(null, null, prueba, parametro);
                    prueba.getResultados().add(resultado);
                }
            }

            if (prueba.getResultados().isEmpty()) {
                throw new NegocioException("Los analisis seleccionados no tienen parametros configurados.");
            }

            return pruebaDAO.guardar(prueba);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo crear la solicitud.", e);
        }
    }

    @Override
    public PruebaEntidad buscarPorFolio(String folio) throws NegocioException {
        if (folio == null || folio.isBlank()) {
            throw new NegocioException("Debe indicar un folio.");
        }
        try {
            return pruebaDAO.buscarPorFolio(folio.trim());
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo buscar la prueba.", e);
        }
    }

    /**
     * Genera un folio con formato LAB-YYYYMMDD-NNNN, donde NNNN es el
     * consecutivo del día.
     */
    private String generarFolio(Date fecha) throws PersistenciaException {
        String fechaTexto = new SimpleDateFormat("yyyyMMdd").format(fecha);
        long consecutivo = pruebaDAO.contarPorFecha(fechaTexto) + 1;
        return String.format("LAB-%s-%04d", fechaTexto, consecutivo);
    }
}
