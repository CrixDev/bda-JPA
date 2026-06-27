package daos;

import entidad.ClienteEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link IClienteDAO}.
 *
 * @author Cristian Devora
 */
public class ClienteDAO implements IClienteDAO {

    private final IConexionBD conexion;

    public ClienteDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public ClienteEntidad guardar(ClienteEntidad cliente) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return cliente;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar el cliente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public ClienteEntidad buscarPorId(Integer id) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(ClienteEntidad.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("No se pudo buscar el cliente con id " + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ClienteEntidad> buscarTodos() throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClienteEntidad c ORDER BY c.nombre", ClienteEntidad.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los clientes", e);
        } finally {
            em.close();
        }
    }
}
