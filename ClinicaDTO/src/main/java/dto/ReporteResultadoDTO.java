package dto;

import java.util.Date;

/**
 * Renglón del reporte de resultados de una prueba: combina datos de la prueba,
 * el cliente, el parámetro y el valor capturado, e indica si el valor quedó
 * fuera del rango normal.
 *
 * @author Cristian Devora
 */
public class ReporteResultadoDTO {

    private String folio;
    private Date fechaHora;
    private String nombreCliente;
    private String nombreParametro;
    private String valor;
    private String unidadMedida;
    private Double rangoInicial;
    private Double rangoFinal;
    private boolean fueraDeRango;
    // Datos de cabecera del informe (paciente y médico).
    private Integer edad;
    private String sexo;
    private String nombreDoctor;

    public ReporteResultadoDTO() {
    }

    public ReporteResultadoDTO(String folio, Date fechaHora, String nombreCliente, String nombreParametro,
            String valor, String unidadMedida, Double rangoInicial, Double rangoFinal, boolean fueraDeRango) {
        this.folio = folio;
        this.fechaHora = fechaHora;
        this.nombreCliente = nombreCliente;
        this.nombreParametro = nombreParametro;
        this.valor = valor;
        this.unidadMedida = unidadMedida;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.fueraDeRango = fueraDeRango;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
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

    public boolean isFueraDeRango() {
        return fueraDeRango;
    }

    public void setFueraDeRango(boolean fueraDeRango) {
        this.fueraDeRango = fueraDeRango;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    /**
     * @return el rango normal formateado para mostrar en el reporte.
     */
    public String getRangoTexto() {
        if (rangoInicial == null || rangoFinal == null) {
            return "-";
        }
        return rangoInicial + " - " + rangoFinal;
    }
}
