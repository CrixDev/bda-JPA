package dto;

/**
 * Datos para registrar el resultado de un parámetro dentro de una prueba.
 *
 * @author Cristian Devora
 */
public class GuardarResultadoDTO {

    private Integer pruebaId;
    private Integer parametroId;
    private String valor;
    private String observacion;

    public GuardarResultadoDTO() {
    }

    public GuardarResultadoDTO(Integer pruebaId, Integer parametroId, String valor, String observacion) {
        this.pruebaId = pruebaId;
        this.parametroId = parametroId;
        this.valor = valor;
        this.observacion = observacion;
    }

    public Integer getPruebaId() {
        return pruebaId;
    }

    public void setPruebaId(Integer pruebaId) {
        this.pruebaId = pruebaId;
    }

    public Integer getParametroId() {
        return parametroId;
    }

    public void setParametroId(Integer parametroId) {
        this.parametroId = parametroId;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
