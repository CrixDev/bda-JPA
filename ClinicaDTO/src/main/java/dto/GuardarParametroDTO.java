package dto;

/**
 * Datos para dar de alta un parámetro dentro de un análisis, incluyendo el rango
 * normal y el intervalo de edad al que aplica.
 *
 * @author Cristian Devora
 */
public class GuardarParametroDTO {

    private String nombre;
    private Integer orden;
    private String unidadMedida;
    private String observaciones;
    private Double rangoInicial;
    private Double rangoFinal;
    private Integer edadInicial;
    private Integer edadFinal;
    private Integer analisisId;

    public GuardarParametroDTO() {
    }

    public GuardarParametroDTO(String nombre, Integer orden, String unidadMedida, String observaciones,
            Double rangoInicial, Double rangoFinal, Integer edadInicial, Integer edadFinal, Integer analisisId) {
        this.nombre = nombre;
        this.orden = orden;
        this.unidadMedida = unidadMedida;
        this.observaciones = observaciones;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.edadInicial = edadInicial;
        this.edadFinal = edadFinal;
        this.analisisId = analisisId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getRangoInicial() {
        return rangoInicial;
    }

    public void setRangoInicial(Double rangoInicial) {
        this.rangoInicial = rangoInicial;
    }

    public Double getRangoFinal() {
        return rangoFinal;
    }

    public void setRangoFinal(Double rangoFinal) {
        this.rangoFinal = rangoFinal;
    }

    public Integer getEdadInicial() {
        return edadInicial;
    }

    public void setEdadInicial(Integer edadInicial) {
        this.edadInicial = edadInicial;
    }

    public Integer getEdadFinal() {
        return edadFinal;
    }

    public void setEdadFinal(Integer edadFinal) {
        this.edadFinal = edadFinal;
    }

    public Integer getAnalisisId() {
        return analisisId;
    }

    public void setAnalisisId(Integer analisisId) {
        this.analisisId = analisisId;
    }
}
