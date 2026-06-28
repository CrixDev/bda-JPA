package negocio;

import dto.GuardarPruebaDTO;
import entidad.PruebaEntidad;

/**
 * Lógica de negocio para las solicitudes (pruebas). Es responsable de generar el
 * folio único de cada prueba.
 *
 * @author Cristian Devora
 */
public interface IPruebaNegocio {

    /**
     * Crea la solicitud generando un folio único con formato LAB-YYYYMMDD-NNNN.
     *
     * @return la prueba creada (incluye el folio generado).
     */
    PruebaEntidad crearSolicitud(GuardarPruebaDTO datos) throws NegocioException;

    PruebaEntidad buscarPorFolio(String folio) throws NegocioException;
}
