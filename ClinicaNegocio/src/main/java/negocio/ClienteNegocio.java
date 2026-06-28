package negocio;

import daos.ClienteDAO;
import daos.IClienteDAO;
import dto.GuardarClienteDTO;
import entidad.ClienteEntidad;
import java.util.List;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación de {@link IClienteNegocio}. Valida los datos y delega la
 * persistencia en {@link IClienteDAO}.
 *
 * @author Cristian Devora
 */
public class ClienteNegocio implements IClienteNegocio {

    private final IClienteDAO clienteDAO;

    public ClienteNegocio(IConexionBD conexion) {
        this.clienteDAO = new ClienteDAO(conexion);
    }

    @Override
    public ClienteEntidad registrarCliente(GuardarClienteDTO datos) throws NegocioException {
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new NegocioException("El nombre del cliente es obligatorio.");
        }
        if (datos.getFechaNacimiento() == null) {
            throw new NegocioException("La fecha de nacimiento es obligatoria.");
        }
        try {
            ClienteEntidad cliente = new ClienteEntidad(
                    datos.getNombre().trim(),
                    datos.getFechaNacimiento(),
                    datos.getSexo(),
                    datos.getTipoSangre());
            return clienteDAO.guardar(cliente);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo registrar el cliente.", e);
        }
    }

    @Override
    public ClienteEntidad buscarCliente(Integer id) throws NegocioException {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo obtener el cliente.", e);
        }
    }

    @Override
    public List<ClienteEntidad> buscarTodos() throws NegocioException {
        try {
            return clienteDAO.buscarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los clientes.", e);
        }
    }
}
