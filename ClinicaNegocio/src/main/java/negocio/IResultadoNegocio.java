package negocio;

import dto.GuardarResultadoDTO;
import entidad.ResultadoEntidad;
import java.util.List;

/**
 * Lógica de negocio para el ingreso de resultados.
 *
 * @author Cristian Devora
 */
public interface IResultadoNegocio {

    ResultadoEntidad ingresarResultado(GuardarResultadoDTO datos) throws NegocioException;

    void ingresarResultados(List<GuardarResultadoDTO> datos) throws NegocioException;

    /**
     * Resultados (renglones pre-creados) de una prueba, con su parámetro, para
     * mostrarlos y capturar su valor.
     */
    List<ResultadoEntidad> buscarResultadosPorPrueba(Integer pruebaId) throws NegocioException;
}
