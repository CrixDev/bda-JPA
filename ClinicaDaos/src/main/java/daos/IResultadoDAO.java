package daos;

import entidad.ResultadoEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de resultados.
 *
 * @author Cristian Devora
 */
public interface IResultadoDAO {

    ResultadoEntidad guardar(ResultadoEntidad resultado) throws PersistenciaException;

    ResultadoEntidad actualizar(ResultadoEntidad resultado) throws PersistenciaException;

    List<ResultadoEntidad> buscarPorPrueba(Integer pruebaId) throws PersistenciaException;
}
