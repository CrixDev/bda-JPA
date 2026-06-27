package daos;

import entidad.DoctorEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link IDoctorDAO}.
 *
 * @author Cristian Devora
 */
public class DoctorDAO implements IDoctorDAO {

    private final IConexionBD conexion;

    public DoctorDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public DoctorEntidad guardar(DoctorEntidad doctor) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(doctor);
            em.getTransaction().commit();
            return doctor;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar el doctor", e);
        } finally {
            em.close();
        }
    }

    @Override
    public DoctorEntidad buscarPorId(Integer id) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(DoctorEntidad.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("No se pudo buscar el doctor con id " + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<DoctorEntidad> buscarTodos() throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT d FROM DoctorEntidad d ORDER BY d.nombre", DoctorEntidad.class)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar los doctores", e);
        } finally {
            em.close();
        }
    }
}
