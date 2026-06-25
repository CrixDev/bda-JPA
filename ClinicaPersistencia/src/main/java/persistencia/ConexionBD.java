package persistencia;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Implementación de {@link IConexionBD}. Mantiene un único
 * {@link EntityManagerFactory} (patrón singleton) para la unidad de
 * persistencia "ClinicaJPA", evitando crear una fábrica por cada operación.
 *
 * @author Cristian Devora
 */
public class ConexionBD implements IConexionBD {

    private static final String UNIDAD_PERSISTENCIA = "ClinicaJPA";

    private static EntityManagerFactory emf;

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
        }
        return emf;
    }

    /**
     * Cierra la fábrica de EntityManager. Útil al cerrar la aplicación.
     */
    public static void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
