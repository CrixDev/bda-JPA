package daos;

import entidad.PruebaEntidad;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación JPA de {@link IPruebaDAO}.
 *
 * @author Cristian Devora
 */
public class PruebaDAO implements IPruebaDAO {

    private final IConexionBD conexion;

    public PruebaDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public PruebaEntidad guardar(PruebaEntidad prueba) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prueba);
            em.getTransaction().commit();
            return prueba;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("No se pudo guardar la prueba", e);
        } finally {
            em.close();
        }
    }

    @Override
    public PruebaEntidad buscarPorId(Integer id) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(PruebaEntidad.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("No se pudo buscar la prueba con id " + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public PruebaEntidad buscarPorFolio(String folio) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT p FROM PruebaEntidad p WHERE p.folio = :folio", PruebaEntidad.class)
                    .setParameter("folio", folio)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new PersistenciaException("No se pudo buscar la prueba con folio " + folio, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PruebaEntidad> buscarPorCliente(Integer clienteId) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM PruebaEntidad p WHERE p.cliente.id = :clienteId ORDER BY p.fechaHora DESC",
                    PruebaEntidad.class)
                    .setParameter("clienteId", clienteId)
                    .getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron listar las pruebas del cliente", e);
        } finally {
            em.close();
        }
    }

    @Override
    public long contarPorFecha(String fechaYYYYMMDD) throws PersistenciaException {
        EntityManager em = conexion.getEntityManagerFactory().createEntityManager();
        try {
            // El folio es LAB-YYYYMMDD-NNNN; contamos los del mismo dia por prefijo.
            String prefijo = "LAB-" + fechaYYYYMMDD + "-%";
            return em.createQuery(
                    "SELECT COUNT(p) FROM PruebaEntidad p WHERE p.folio LIKE :prefijo", Long.class)
                    .setParameter("prefijo", prefijo)
                    .getSingleResult();
        } catch (Exception e) {
            throw new PersistenciaException("No se pudieron contar las pruebas de la fecha " + fechaYYYYMMDD, e);
        } finally {
            em.close();
        }
    }
}
