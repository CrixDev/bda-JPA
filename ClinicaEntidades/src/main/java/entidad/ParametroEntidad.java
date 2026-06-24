package entidad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Parámetro medible dentro de un análisis (ej. "Hemoglobina"), con su unidad de
 * medida y el rango normal válido para un intervalo de edad.
 *
 * @author Cristian Devora
 */
@Entity
@Table(name = "parametros")
public class ParametroEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "rango_inicial")
    private Double rangoInicial;

    @Column(name = "rango_final")
    private Double rangoFinal;

    @Column(name = "edad_inicial")
    private Integer edadInicial;

    @Column(name = "edad_final")
    private Integer edadFinal;

    @ManyToOne
    @JoinColumn(name = "analisis_id", nullable = false)
    private AnalisisEntidad analisis;

    public ParametroEntidad() {
    }

    public ParametroEntidad(Integer orden, String nombre, String unidadMedida, String observaciones,
            Double rangoInicial, Double rangoFinal, Integer edadInicial, Integer edadFinal,
            AnalisisEntidad analisis) {
        this.orden = orden;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.observaciones = observaciones;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.edadInicial = edadInicial;
        this.edadFinal = edadFinal;
        this.analisis = analisis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public AnalisisEntidad getAnalisis() {
        return analisis;
    }

    public void setAnalisis(AnalisisEntidad analisis) {
        this.analisis = analisis;
    }

    @Override
    public String toString() {
        return nombre + (unidadMedida != null ? " (" + unidadMedida + ")" : "");
    }
}
