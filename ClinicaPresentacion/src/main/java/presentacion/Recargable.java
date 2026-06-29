package presentacion;

/**
 * Implementada por los paneles de cada módulo que necesitan refrescar sus datos
 * desde la base de datos cada vez que el usuario los selecciona en el sidebar
 * (por ejemplo, para ver los análisis recién creados en el catálogo).
 */
public interface Recargable {

    void recargar();
}
