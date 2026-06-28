package negocio;

import daos.DoctorDAO;
import daos.IDoctorDAO;
import dto.GuardarDoctorDTO;
import entidad.DoctorEntidad;
import java.util.List;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación de {@link IDoctorNegocio}.
 *
 * @author Cristian Devora
 */
public class DoctorNegocio implements IDoctorNegocio {

    private final IDoctorDAO doctorDAO;

    public DoctorNegocio(IConexionBD conexion) {
        this.doctorDAO = new DoctorDAO(conexion);
    }

    @Override
    public DoctorEntidad registrarDoctor(GuardarDoctorDTO datos) throws NegocioException {
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new NegocioException("El nombre del doctor es obligatorio.");
        }
        try {
            DoctorEntidad doctor = new DoctorEntidad(datos.getNombre().trim(), datos.getSexo());
            return doctorDAO.guardar(doctor);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo registrar el doctor.", e);
        }
    }

    @Override
    public List<DoctorEntidad> buscarTodos() throws NegocioException {
        try {
            return doctorDAO.buscarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los doctores.", e);
        }
    }
}
