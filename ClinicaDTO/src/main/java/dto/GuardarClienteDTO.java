package dto;

import java.util.Date;

/**
 * Datos necesarios para registrar un nuevo cliente.
 *
 * @author Cristian Devora
 */
public class GuardarClienteDTO {

    private String nombre;
    private Date fechaNacimiento;
    private String sexo;
    private String tipoSangre;

    public GuardarClienteDTO() {
    }

    public GuardarClienteDTO(String nombre, Date fechaNacimiento, String sexo, String tipoSangre) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.tipoSangre = tipoSangre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }
}
