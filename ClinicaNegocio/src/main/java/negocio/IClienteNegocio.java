package negocio;

import dto.GuardarClienteDTO;
import entidad.ClienteEntidad;
import java.util.List;

/**
 * Lógica de negocio para clientes.
 *
 * @author Cristian Devora
 */
public interface IClienteNegocio {

    ClienteEntidad registrarCliente(GuardarClienteDTO datos) throws NegocioException;

    ClienteEntidad buscarCliente(Integer id) throws NegocioException;

    List<ClienteEntidad> buscarTodos() throws NegocioException;
}
