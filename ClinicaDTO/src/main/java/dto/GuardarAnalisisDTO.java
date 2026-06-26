package dto;

/**
 * Datos para dar de alta un análisis en el catálogo. Se referencia el tipo de
 * muestra por su id.
 *
 * @author Cristian Devora
 */
public class GuardarAnalisisDTO {

    private String nombre;
    private String nota;
    private Integer tipoMuestraId;

    public GuardarAnalisisDTO() {
    }

    public GuardarAnalisisDTO(String nombre, String nota, Integer tipoMuestraId) {
        this.nombre = nombre;
        this.nota = nota;
        this.tipoMuestraId = tipoMuestraId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Integer getTipoMuestraId() {
        return tipoMuestraId;
    }

    public void setTipoMuestraId(Integer tipoMuestraId) {
        this.tipoMuestraId = tipoMuestraId;
    }
}
