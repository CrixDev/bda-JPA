package daos;

import entidad.TipoMuestraEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de tipos de muestra.
 *
 * @author Cristian Devora
 */
public interface ITipoMuestraDAO {

    TipoMuestraEntidad guardar(TipoMuestraEntidad tipoMuestra) throws PersistenciaException;

    List<TipoMuestraEntidad> buscarTodos() throws PersistenciaException;
}
