package dto;

/**
 * Datos necesarios para registrar un nuevo doctor.
 *
 * @author Cristian Devora
 */
public class GuardarDoctorDTO {

    private String nombre;
    private String sexo;

    public GuardarDoctorDTO() {
    }

    public GuardarDoctorDTO(String nombre, String sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
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
}
