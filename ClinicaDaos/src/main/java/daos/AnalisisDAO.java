package daos;

import entidad.AnalisisEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link IAnalisisDAO}.
 *
 * @author Cristian Devora
 */
public class AnalisisDAO implements IAnalisisDAO {

    private final IConexionBD conexion;

    public AnalisisDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public AnalisisEntidad guardar(AnalisisEntidad analisis) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(analisis);
            em.getTransaction().commit();
            return analisis;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar el analisis", e);
        } finally {
            em.close();
        }
    }

    @Override
    public AnalisisEntidad buscarPorId(Integer id) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(AnalisisEntidad.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("No se pudo buscar el analisis con id " + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<AnalisisEntidad> buscarPorTipoMuestra(Integer tipoMuestraId) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM AnalisisEntidad a WHERE a.tipoMuestra.id = :tipoId ORDER BY a.nombre",
                    AnalisisEntidad.class)
                    .setParameter("tipoId", tipoMuestraId)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los analisis del tipo de muestra", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<AnalisisEntidad> buscarTodos() throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT a FROM AnalisisEntidad a ORDER BY a.nombre", AnalisisEntidad.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los analisis", e);
        } finally {
            em.close();
        }
    }
}
