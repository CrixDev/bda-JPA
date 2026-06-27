package daos;

import entidad.PruebaEntidad;
import java.util.List;
import persistencia.PersistenciaException;

/**
 * Acceso a datos de pruebas (solicitudes).
 *
 * @author Cristian Devora
 */
public interface IPruebaDAO {

    PruebaEntidad guardar(PruebaEntidad prueba) throws PersistenciaException;

    PruebaEntidad buscarPorId(Integer id) throws PersistenciaException;

    PruebaEntidad buscarPorFolio(String folio) throws PersistenciaException;

    List<PruebaEntidad> buscarPorCliente(Integer clienteId) throws PersistenciaException;

    /**
     * @return cuántas pruebas existen para una fecha dada (para numerar el folio).
     */
    long contarPorFecha(String fechaYYYYMMDD) throws PersistenciaException;
}
