package negocio;

import dto.ReporteResultadoDTO;
import java.util.List;

/**
 * Lógica de negocio para generar el reporte de resultados de una prueba,
 * calculando si cada valor está fuera del rango normal según la edad del
 * cliente.
 *
 * @author Cristian Devora
 */
public interface IReporteNegocio {

    List<ReporteResultadoDTO> generarReporte(Integer pruebaId) throws NegocioException;

    List<ReporteResultadoDTO> generarReportePorFolio(String folio) throws NegocioException;
}
