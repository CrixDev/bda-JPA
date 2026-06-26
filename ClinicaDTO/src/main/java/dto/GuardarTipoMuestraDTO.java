package dto;

/**
 * Datos para dar de alta un tipo de muestra en el catálogo.
 *
 * @author Cristian Devora
 */
public class GuardarTipoMuestraDTO {

    private String nombre;

    public GuardarTipoMuestraDTO() {
    }

    public GuardarTipoMuestraDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
