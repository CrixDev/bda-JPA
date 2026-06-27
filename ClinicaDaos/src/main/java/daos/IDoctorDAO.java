package daos;

import entidad.DoctorEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de doctores.
 *
 * @author Cristian Devora
 */
public interface IDoctorDAO {

    DoctorEntidad guardar(DoctorEntidad doctor) throws PersistenciaException;

    DoctorEntidad buscarPorId(Integer id) throws PersistenciaException;

    List<DoctorEntidad> buscarTodos() throws PersistenciaException;
}
