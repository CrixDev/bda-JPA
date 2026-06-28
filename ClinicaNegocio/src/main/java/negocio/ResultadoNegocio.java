package negocio;

import daos.IParametroDAO;
import daos.IPruebaDAO;
import daos.IResultadoDAO;
import daos.ParametroDAO;
import daos.PruebaDAO;
import daos.ResultadoDAO;
import dto.GuardarResultadoDTO;
import entidad.ParametroEntidad;
import entidad.PruebaEntidad;
import entidad.ResultadoEntidad;
import java.util.List;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación de {@link IResultadoNegocio}. Al ingresar un resultado,
 * actualiza el renglón pre-creado para ese parámetro dentro de la prueba; si no
 * existiera, lo crea.
 *
 * @author Cristian Devora
 */
public class ResultadoNegocio implements IResultadoNegocio {

    private final IResultadoDAO resultadoDAO;
    private final IPruebaDAO pruebaDAO;
    private final IParametroDAO parametroDAO;

    public ResultadoNegocio(IConexionBD conexion) {
        this.resultadoDAO = new ResultadoDAO(conexion);
        this.pruebaDAO = new PruebaDAO(conexion);
        this.parametroDAO = new ParametroDAO(conexion);
    }

    @Override
    public ResultadoEntidad ingresarResultado(GuardarResultadoDTO datos) throws NegocioException {
        // FEATURE INGRESO DE RESULTADOS: deshabilitada / rota a proposito.
        roto roto roto;
        if (datos == null || datos.getPruebaId() == null || datos.getParametroId() == null) {
            throw new NegocioException("Faltan datos para ingresar el resultado.");
        }
        try {
            // Buscar el renglon pre-creado de este parametro en la prueba.
            ResultadoEntidad existente = null;
            for (ResultadoEntidad r : resultadoDAO.buscarPorPrueba(datos.getPruebaId())) {
                if (r.getParametro() != null && r.getParametro().getId().equals(datos.getParametroId())) {
                    existente = r;
                    break;
                }
            }

            if (existente != null) {
                existente.setValor(datos.getValor());
                existente.setObservacion(datos.getObservacion());
                return resultadoDAO.actualizar(existente);
            }

            // Si no existia (ej. parametro agregado despues), se crea uno nuevo.
            PruebaEntidad prueba = pruebaDAO.buscarPorId(datos.getPruebaId());
            ParametroEntidad parametro = parametroDAO.buscarPorId(datos.getParametroId());
            if (prueba == null || parametro == null) {
                throw new NegocioException("La prueba o el parametro no existen.");
            }
            ResultadoEntidad nuevo = new ResultadoEntidad(
                    datos.getValor(), datos.getObservacion(), prueba, parametro);
            return resultadoDAO.guardar(nuevo);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo ingresar el resultado.", e);
        }
    }

    @Override
    public void ingresarResultados(List<GuardarResultadoDTO> datos) throws NegocioException {
        if (datos == null || datos.isEmpty()) {
            throw new NegocioException("No hay resultados para ingresar.");
        }
        for (GuardarResultadoDTO d : datos) {
            ingresarResultado(d);
        }
    }

    @Override
    public List<ResultadoEntidad> buscarResultadosPorPrueba(Integer pruebaId) throws NegocioException {
        if (pruebaId == null) {
            throw new NegocioException("Debe indicar la prueba.");
        }
        try {
            return resultadoDAO.buscarPorPrueba(pruebaId);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron obtener los resultados de la prueba.", e);
        }
    }
}
