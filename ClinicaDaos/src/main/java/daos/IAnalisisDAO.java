package daos;

import entidad.AnalisisEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de análisis.
 *
 * @author Cristian Devora
 */
public interface IAnalisisDAO {

    AnalisisEntidad guardar(AnalisisEntidad analisis) throws PersistenciaException;

    AnalisisEntidad buscarPorId(Integer id) throws PersistenciaException;

    List<AnalisisEntidad> buscarPorTipoMuestra(Integer tipoMuestraId) throws PersistenciaException;

    List<AnalisisEntidad> buscarTodos() throws PersistenciaException;
}
