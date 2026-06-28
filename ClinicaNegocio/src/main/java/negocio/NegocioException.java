package negocio;

/**
 * Excepción de la capa de negocio. Encapsula errores de validación o de la capa
 * de datos para presentarlos de forma controlada a la capa de presentación.
 *
 * @author Cristian Devora
 */
public class NegocioException extends Exception {

    private static final long serialVersionUID = 1L;

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
