package daos;

import entidad.TipoMuestraEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link ITipoMuestraDAO}.
 *
 * @author Cristian Devora
 */
public class TipoMuestraDAO implements ITipoMuestraDAO {

    private final IConexionBD conexion;

    public TipoMuestraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public TipoMuestraEntidad guardar(TipoMuestraEntidad tipoMuestra) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tipoMuestra);
            em.getTransaction().commit();
            return tipoMuestra;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar el tipo de muestra", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<TipoMuestraEntidad> buscarTodos() throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT t FROM TipoMuestraEntidad t ORDER BY t.nombre", TipoMuestraEntidad.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los tipos de muestra", e);
        } finally {
            em.close();
        }
    }
}
