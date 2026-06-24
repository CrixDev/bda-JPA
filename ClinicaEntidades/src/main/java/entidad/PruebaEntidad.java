package entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Prueba de laboratorio = la solicitud / orden generada para un cliente. Cada
 * prueba se identifica por un folio único.
 *
 * @author Cristian Devora
 */
@Entity
@Table(name = "pruebas")
public class PruebaEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "folio", nullable = false, unique = true, length = 30)
    private String folio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_hora")
    private Date fechaHora;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntidad cliente;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = true)
    private DoctorEntidad doctor;

    @OneToMany(mappedBy = "prueba", cascade = CascadeType.ALL)
    private List<ResultadoEntidad> resultados;

    public PruebaEntidad() {
        this.resultados = new ArrayList<>();
    }

    public PruebaEntidad(String folio, Date fechaHora, ClienteEntidad cliente, DoctorEntidad doctor) {
        this();
        this.folio = folio;
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.doctor = doctor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ClienteEntidad getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntidad cliente) {
        this.cliente = cliente;
    }

    public DoctorEntidad getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntidad doctor) {
        this.doctor = doctor;
    }

    public List<ResultadoEntidad> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResultadoEntidad> resultados) {
        this.resultados = resultados;
    }

    @Override
    public String toString() {
        return folio;
    }
}
