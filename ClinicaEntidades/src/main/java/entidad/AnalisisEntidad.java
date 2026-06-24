package entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Estudio o análisis clínico (ej. "Biometría hemática") asociado a un tipo de
 * muestra y compuesto por uno o más parámetros.
 *
 * @author Cristian Devora
 */
@Entity
@Table(name = "analisis")
public class AnalisisEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "nota", length = 500)
    private String nota;

    @ManyToOne
    @JoinColumn(name = "tipo_muestra_id", nullable = false)
    private TipoMuestraEntidad tipoMuestra;

    @OneToMany(mappedBy = "analisis", cascade = CascadeType.ALL)
    private List<ParametroEntidad> parametros;

    public AnalisisEntidad() {
        this.parametros = new ArrayList<>();
    }

    public AnalisisEntidad(String nombre, String nota, TipoMuestraEntidad tipoMuestra) {
        this();
        this.nombre = nombre;
        this.nota = nota;
        this.tipoMuestra = tipoMuestra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TipoMuestraEntidad getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(TipoMuestraEntidad tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public List<ParametroEntidad> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametroEntidad> parametros) {
        this.parametros = parametros;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
