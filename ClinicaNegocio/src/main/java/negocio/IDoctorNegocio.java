package negocio;

import dto.GuardarDoctorDTO;
import entidad.DoctorEntidad;
import java.util.List;

/**
 * Lógica de negocio para doctores.
 *
 * @author Cristian Devora
 */
public interface IDoctorNegocio {

    DoctorEntidad registrarDoctor(GuardarDoctorDTO datos) throws NegocioException;

    List<DoctorEntidad> buscarTodos() throws NegocioException;
}
