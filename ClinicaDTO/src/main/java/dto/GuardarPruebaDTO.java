package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos para crear una solicitud (prueba). El folio NO viene aquí: lo genera la
 * capa de negocio. El doctor es opcional (puede ser null).
 *
 * @author Cristian Devora
 */
public class GuardarPruebaDTO {

    private Integer clienteId;
    private Integer doctorId;
    private List<Integer> listaAnalisisIds;

    public GuardarPruebaDTO() {
        this.listaAnalisisIds = new ArrayList<>();
    }

    public GuardarPruebaDTO(Integer clienteId, Integer doctorId, List<Integer> listaAnalisisIds) {
        this.clienteId = clienteId;
        this.doctorId = doctorId;
        this.listaAnalisisIds = listaAnalisisIds != null ? listaAnalisisIds : new ArrayList<>();
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public List<Integer> getListaAnalisisIds() {
        return listaAnalisisIds;
    }

    public void setListaAnalisisIds(List<Integer> listaAnalisisIds) {
        this.listaAnalisisIds = listaAnalisisIds;
    }
}
