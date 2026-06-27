package daos;

import entidad.ParametroEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link IParametroDAO}.
 *
 * @author Cristian Devora
 */
public class ParametroDAO implements IParametroDAO {

    private final IConexionBD conexion;

    public ParametroDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public ParametroEntidad guardar(ParametroEntidad parametro) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(parametro);
            em.getTransaction().commit();
            return parametro;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar el parametro", e);
        } finally {
            em.close();
        }
    }

    @Override
    public ParametroEntidad buscarPorId(Integer id) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(ParametroEntidad.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("No se pudo buscar el parametro con id " + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParametroEntidad> buscarPorAnalisis(Integer analisisId) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM ParametroEntidad p WHERE p.analisis.id = :analisisId ORDER BY p.orden",
                    ParametroEntidad.class)
                    .setParameter("analisisId", analisisId)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los parametros del analisis", e);
        } finally {
            em.close();
        }
    }
}
