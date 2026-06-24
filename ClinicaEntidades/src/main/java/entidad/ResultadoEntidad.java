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
 * Resultado capturado para un parámetro dentro de una prueba.
 *
 * @author Cristian Devora
 */
@Entity
@Table(name = "resultados")
public class ResultadoEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "valor", length = 100)
    private String valor;

    @Column(name = "observacion", length = 500)
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "prueba_id", nullable = false)
    private PruebaEntidad prueba;

    @ManyToOne
    @JoinColumn(name = "parametro_id", nullable = false)
    private ParametroEntidad parametro;

    public ResultadoEntidad() {
    }

    public ResultadoEntidad(String valor, String observacion, PruebaEntidad prueba, ParametroEntidad parametro) {
        this.valor = valor;
        this.observacion = observacion;
        this.prueba = prueba;
        this.parametro = parametro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public PruebaEntidad getPrueba() {
        return prueba;
    }

    public void setPrueba(PruebaEntidad prueba) {
        this.prueba = prueba;
    }

    public ParametroEntidad getParametro() {
        return parametro;
    }

    public void setParametro(ParametroEntidad parametro) {
        this.parametro = parametro;
    }
}
