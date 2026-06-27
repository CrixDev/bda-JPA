package daos;

import entidad.ClienteEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de clientes.
 *
 * @author Cristian Devora
 */
public interface IClienteDAO {

    ClienteEntidad guardar(ClienteEntidad cliente) throws PersistenciaException;

    ClienteEntidad buscarPorId(Integer id) throws PersistenciaException;

    List<ClienteEntidad> buscarTodos() throws PersistenciaException;
}
