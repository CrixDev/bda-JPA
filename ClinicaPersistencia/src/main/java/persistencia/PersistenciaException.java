package persistencia;

/**
 * Excepción checked que envuelve los errores ocurridos en la capa de acceso a
 * datos (JPA), para no propagar excepciones de proveedor hacia capas superiores.
 *
 * @author Cristian Devora
 */
public class PersistenciaException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
