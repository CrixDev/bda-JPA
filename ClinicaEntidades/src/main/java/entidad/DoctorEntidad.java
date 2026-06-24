package entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Doctor que solicita una prueba de laboratorio.
 *
 * @author Cristian Devora
 */
@Entity
@Table(name = "doctores")
public class DoctorEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "sexo", length = 1)
    private String sexo;

    @OneToMany(mappedBy = "doctor")
    private List<PruebaEntidad> pruebas;

    public DoctorEntidad() {
        this.pruebas = new ArrayList<>();
    }

    public DoctorEntidad(String nombre, String sexo) {
        this();
        this.nombre = nombre;
        this.sexo = sexo;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<PruebaEntidad> getPruebas() {
        return pruebas;
    }

    public void setPruebas(List<PruebaEntidad> pruebas) {
        this.pruebas = pruebas;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
