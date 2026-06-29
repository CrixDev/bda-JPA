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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Tipo de muestra que se procesa en el laboratorio (sangre, orina, etc.).
 *
 * @author Cristian Devora
 */
@Entity
@Table(name = "tiposmuestra")
public class TipoMuestraEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoMuestra", cascade = CascadeType.ALL)
    private List<AnalisisEntidad> analisis;

    public TipoMuestraEntidad() {
        this.analisis = new ArrayList<>();
    }

    public TipoMuestraEntidad(String nombre) {
        this();
        this.nombre = nombre;
    }

    public TipoMuestraEntidad(Integer id, String nombre) {
        this(nombre);
        this.id = id;
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

    public List<AnalisisEntidad> getAnalisis() {
        return analisis;
    }

    public void setAnalisis(List<AnalisisEntidad> analisis) {
        this.analisis = analisis;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
