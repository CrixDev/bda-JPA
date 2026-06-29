package negocio;

import daos.IPruebaDAO;
import daos.IResultadoDAO;
import daos.PruebaDAO;
import daos.ResultadoDAO;
import dto.ReporteResultadoDTO;
import entidad.ClienteEntidad;
import entidad.ParametroEntidad;
import entidad.PruebaEntidad;
import entidad.ResultadoEntidad;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import persistencia.IConexionBD;
import persistencia.PersistenciaException;

/**
 * Implementación de {@link IReporteNegocio}. Arma el reporte de una prueba y,
 * por cada resultado, determina si el valor capturado quedó fuera del rango
 * normal del parámetro, considerando la edad del cliente.
 *
 * @author dylannvms
 */
public class ReporteNegocio implements IReporteNegocio {

    private final IPruebaDAO pruebaDAO;
    private final IResultadoDAO resultadoDAO;

    public ReporteNegocio(IConexionBD conexion) {
        this.pruebaDAO = new PruebaDAO(conexion);
        this.resultadoDAO = new ResultadoDAO(conexion);
    }

    @Override
    public List<ReporteResultadoDTO> generarReporte(Integer pruebaId) throws NegocioException {
        if (pruebaId == null) {
            throw new NegocioException("Debe indicar la prueba.");
        }
        try {
            PruebaEntidad prueba = pruebaDAO.buscarPorId(pruebaId);
            if (prueba == null) {
                throw new NegocioException("La prueba no existe.");
            }
            return construirReporte(prueba);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo generar el reporte.", e);
        }
    }

    @Override
    public List<ReporteResultadoDTO> generarReportePorFolio(String folio) throws NegocioException {
        if (folio == null || folio.isBlank()) {
            throw new NegocioException("Debe indicar un folio.");
        }
        try {
            PruebaEntidad prueba = pruebaDAO.buscarPorFolio(folio.trim());
            if (prueba == null) {
                throw new NegocioException("No se encontro una prueba con el folio " + folio + ".");
            }
            return construirReporte(prueba);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo generar el reporte.", e);
        }
    }

    private List<ReporteResultadoDTO> construirReporte(PruebaEntidad prueba) throws PersistenciaException {
        ClienteEntidad cliente = prueba.getCliente();
        Integer edadCliente = calcularEdad(cliente != null ? cliente.getFechaNacimiento() : null);

        List<ReporteResultadoDTO> reporte = new ArrayList<>();
        for (ResultadoEntidad resultado : resultadoDAO.buscarPorPrueba(prueba.getId())) {
            ParametroEntidad parametro = resultado.getParametro();
            boolean fueraDeRango = evaluarFueraDeRango(resultado.getValor(), parametro, edadCliente);

            reporte.add(new ReporteResultadoDTO(
                    prueba.getFolio(),
                    prueba.getFechaHora(),
                    cliente != null ? cliente.getNombre() : "",
                    parametro != null ? parametro.getNombre() : "",
                    resultado.getValor(),
                    parametro != null ? parametro.getUnidadMedida() : "",
                    parametro != null ? parametro.getRangoInicial() : null,
                    parametro != null ? parametro.getRangoFinal() : null,
                    fueraDeRango));
        }
        return reporte;
    }

    private boolean evaluarFueraDeRango(String valor, ParametroEntidad parametro, Integer edadCliente) {
        if (valor == null || valor.isBlank() || parametro == null) {
            return false;
        }
        if (parametro.getRangoInicial() == null || parametro.getRangoFinal() == null) {
            return false;
        }
        if (!edadAplica(parametro, edadCliente)) {
            return false;
        }
        try {
            double valorNumerico = Double.parseDouble(valor.trim().replace(",", "."));
            return valorNumerico < parametro.getRangoInicial() || valorNumerico > parametro.getRangoFinal();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean edadAplica(ParametroEntidad parametro, Integer edadCliente) {
        if (edadCliente == null) {
            return true;
        }
        if (parametro.getEdadInicial() != null && edadCliente < parametro.getEdadInicial()) {
            return false;
        }
        if (parametro.getEdadFinal() != null && edadCliente > parametro.getEdadFinal()) {
            return false;
        }
        return true;
    }

    private Integer calcularEdad(Date fechaNacimiento) {
        if (fechaNacimiento == null) {
            return null;
        }
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);
        Calendar hoy = Calendar.getInstance();

        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return edad;
    }
}
