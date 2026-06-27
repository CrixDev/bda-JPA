package daos;

import entidad.ParametroEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de parámetros.
 *
 * @author Cristian Devora
 */
public interface IParametroDAO {

    ParametroEntidad guardar(ParametroEntidad parametro) throws PersistenciaException;

    ParametroEntidad buscarPorId(Integer id) throws PersistenciaException;

    List<ParametroEntidad> buscarPorAnalisis(Integer analisisId) throws PersistenciaException;
}
