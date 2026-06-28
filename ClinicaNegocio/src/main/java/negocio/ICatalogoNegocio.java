package negocio;

import dto.GuardarAnalisisDTO;
import dto.GuardarParametroDTO;
import dto.GuardarTipoMuestraDTO;
import entidad.AnalisisEntidad;
import entidad.ParametroEntidad;
import entidad.TipoMuestraEntidad;
import java.util.List;

/**
 * Lógica de negocio para el catálogo (tipos de muestra, análisis y parámetros).
 *
 * @author Cristian Devora
 */
public interface ICatalogoNegocio {

    TipoMuestraEntidad registrarTipoMuestra(GuardarTipoMuestraDTO datos) throws NegocioException;

    AnalisisEntidad registrarAnalisis(GuardarAnalisisDTO datos) throws NegocioException;

    ParametroEntidad registrarParametro(GuardarParametroDTO datos) throws NegocioException;

    List<TipoMuestraEntidad> buscarTiposMuestra() throws NegocioException;

    List<AnalisisEntidad> buscarTodosAnalisis() throws NegocioException;

    List<AnalisisEntidad> buscarAnalisisPorTipo(Integer tipoMuestraId) throws NegocioException;

    List<ParametroEntidad> buscarParametrosPorAnalisis(Integer analisisId) throws NegocioException;
}
