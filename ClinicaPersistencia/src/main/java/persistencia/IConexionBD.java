package persistencia;

import javax.persistence.EntityManagerFactory;

/**
 * Abstrae el acceso al {@link EntityManagerFactory} de la unidad de persistencia
 * para que los DAOs no dependan directamente de la implementación.
 *
 * @author Cristian Devora
 */
public interface IConexionBD {

    /**
     * @return el EntityManagerFactory de la unidad de persistencia "ClinicaJPA".
     */
    EntityManagerFactory getEntityManagerFactory();
}
