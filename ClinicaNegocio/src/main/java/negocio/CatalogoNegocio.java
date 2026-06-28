package negocio;

import daos.AnalisisDAO;
import daos.IAnalisisDAO;
import daos.IParametroDAO;
import daos.ITipoMuestraDAO;
import daos.ParametroDAO;
import daos.TipoMuestraDAO;
import dto.GuardarAnalisisDTO;
import dto.GuardarParametroDTO;
import dto.GuardarTipoMuestraDTO;
import entidad.AnalisisEntidad;
import entidad.ParametroEntidad;
import entidad.TipoMuestraEntidad;
import java.util.List;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación de {@link ICatalogoNegocio}: alta y consulta de tipos de
 * muestra, análisis y parámetros.
 *
 * @author Cristian Devora
 */
public class CatalogoNegocio implements ICatalogoNegocio {

    private final ITipoMuestraDAO tipoMuestraDAO;
    private final IAnalisisDAO analisisDAO;
    private final IParametroDAO parametroDAO;

    public CatalogoNegocio(IConexionBD conexion) {
        this.tipoMuestraDAO = new TipoMuestraDAO(conexion);
        this.analisisDAO = new AnalisisDAO(conexion);
        this.parametroDAO = new ParametroDAO(conexion);
    }

    @Override
    public TipoMuestraEntidad registrarTipoMuestra(GuardarTipoMuestraDTO datos) throws NegocioException {
        // FEATURE CATALOGO DE ANALISIS: deshabilitada / rota a proposito.
        roto roto roto;
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new NegocioException("El nombre del tipo de muestra es obligatorio.");
        }
        try {
            return tipoMuestraDAO.guardar(new TipoMuestraEntidad(datos.getNombre().trim()));
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo registrar el tipo de muestra.", e);
        }
    }

    @Override
    public AnalisisEntidad registrarAnalisis(GuardarAnalisisDTO datos) throws NegocioException {
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new NegocioException("El nombre del analisis es obligatorio.");
        }
        if (datos.getTipoMuestraId() == null) {
            throw new NegocioException("Debe seleccionar un tipo de muestra para el analisis.");
        }
        try {
            TipoMuestraEntidad tipo = buscarTipoMuestraPorId(datos.getTipoMuestraId());
            if (tipo == null) {
                throw new NegocioException("El tipo de muestra seleccionado no existe.");
            }
            AnalisisEntidad analisis = new AnalisisEntidad(datos.getNombre().trim(), datos.getNota(), tipo);
            return analisisDAO.guardar(analisis);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo registrar el analisis.", e);
        }
    }

    @Override
    public ParametroEntidad registrarParametro(GuardarParametroDTO datos) throws NegocioException {
        if (datos == null || datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new NegocioException("El nombre del parametro es obligatorio.");
        }
        if (datos.getAnalisisId() == null) {
            throw new NegocioException("Debe seleccionar un analisis para el parametro.");
        }
        try {
            AnalisisEntidad analisis = analisisDAO.buscarPorId(datos.getAnalisisId());
            if (analisis == null) {
                throw new NegocioException("El analisis seleccionado no existe.");
            }
            ParametroEntidad parametro = new ParametroEntidad(
                    datos.getOrden(),
                    datos.getNombre().trim(),
                    datos.getUnidadMedida(),
                    datos.getObservaciones(),
                    datos.getRangoInicial(),
                    datos.getRangoFinal(),
                    datos.getEdadInicial(),
                    datos.getEdadFinal(),
                    analisis);
            return parametroDAO.guardar(parametro);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo registrar el parametro.", e);
        }
    }

    @Override
    public List<TipoMuestraEntidad> buscarTiposMuestra() throws NegocioException {
        try {
            return tipoMuestraDAO.buscarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los tipos de muestra.", e);
        }
    }

    @Override
    public List<AnalisisEntidad> buscarTodosAnalisis() throws NegocioException {
        try {
            return analisisDAO.buscarTodos();
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los analisis.", e);
        }
    }

    @Override
    public List<AnalisisEntidad> buscarAnalisisPorTipo(Integer tipoMuestraId) throws NegocioException {
        try {
            return analisisDAO.buscarPorTipoMuestra(tipoMuestraId);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los analisis del tipo de muestra.", e);
        }
    }

    @Override
    public List<ParametroEntidad> buscarParametrosPorAnalisis(Integer analisisId) throws NegocioException {
        try {
            return parametroDAO.buscarPorAnalisis(analisisId);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los parametros del analisis.", e);
        }
    }

    private TipoMuestraEntidad buscarTipoMuestraPorId(Integer id) throws PersistenciaException {
        for (TipoMuestraEntidad t : tipoMuestraDAO.buscarTodos()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }
}
