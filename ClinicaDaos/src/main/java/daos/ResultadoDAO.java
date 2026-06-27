package daos;

import entidad.ResultadoEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link IResultadoDAO}.
 *
 * @author Cristian Devora
 */
public class ResultadoDAO implements IResultadoDAO {

    private final IConexionBD conexion;

    public ResultadoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public ResultadoEntidad guardar(ResultadoEntidad resultado) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(resultado);
            em.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar el resultado", e);
        } finally {
            em.close();
        }
    }

    @Override
    public ResultadoEntidad actualizar(ResultadoEntidad resultado) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            ResultadoEntidad fusionado = em.merge(resultado);
            em.getTransaction().commit();
            return fusionado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo actualizar el resultado", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ResultadoEntidad> buscarPorPrueba(Integer pruebaId) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM ResultadoEntidad r WHERE r.prueba.id = :pruebaId ORDER BY r.parametro.orden",
                    ResultadoEntidad.class)
                    .setParameter("pruebaId", pruebaId)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los resultados de la prueba", e);
        } finally {
            em.close();
        }
    }
}
