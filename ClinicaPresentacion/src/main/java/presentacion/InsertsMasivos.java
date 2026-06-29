package presentacion;

import dto.GuardarClienteDTO;
import dto.GuardarDoctorDTO;
import dto.GuardarTipoMuestraDTO;
import java.util.Calendar;
import java.util.Date;
import negocio.CatalogoNegocio;
import negocio.ClienteNegocio;
import negocio.DoctorNegocio;
import negocio.ICatalogoNegocio;
import negocio.IClienteNegocio;
import negocio.IDoctorNegocio;
import negocio.NegocioException;
import persistencia.ConexionBD;
import persistencia.IConexionBD;

/**
 * Programa de INSERTS MASIVOS (carga inicial / "instalación por primera vez").
 *
 * <p>Al ejecutarse sobre una base de datos vacía, la primera conexión hace que
 * EclipseLink genere TODAS las tablas automáticamente (ddl-generation =
 * create-tables). Después siembra únicamente las tablas que NO se llenan con las
 * funciones del sistema:</p>
 *
 * <ul>
 *   <li>Tipos de muestra (12)</li>
 *   <li>Clientes (10) — con variedad de sexos y tipos de sangre</li>
 *   <li>Doctores (10) — con variedad de sexos</li>
 * </ul>
 *
 * <p>Los análisis, las solicitudes (pruebas), los resultados y el reporte se
 * generan después con sus respectivas funciones dentro de la aplicación.</p>
 *
 * <p>El programa es seguro de re-ejecutar: si una tabla ya tiene datos, la
 * omite para no duplicar.</p>
 *
 * @author Cristian Devora
 */
public class InsertsMasivos {

    // Tipos de muestra solicitados.
    private static final String[] TIPOS_MUESTRA = {
        "Sangre",
        "Orina",
        "Heces fecales",
        "Saliva",
        "Esputo (flema)",
        "Hisopado nasal",
        "Hisopado faríngeo",
        "Biopsia de tejido",
        "Líquido cefalorraquídeo",
        "Semen",
        "Cabello",
        "Uñas"
    };

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("   CARGA INICIAL - Laboratorio Clinico Salud Total");
        System.out.println("====================================================");

        IConexionBD conexion = new ConexionBD();
        ICatalogoNegocio catalogo = new CatalogoNegocio(conexion);
        IClienteNegocio clientes = new ClienteNegocio(conexion);
        IDoctorNegocio doctores = new DoctorNegocio(conexion);

        try {
            System.out.println("\n> Inicializando conexion (esto crea las tablas si no existen)...");
            // La primera llamada a la BD dispara la creacion automatica de tablas.
            sembrarTiposMuestra(catalogo);
            sembrarClientes(clientes);
            sembrarDoctores(doctores);

            System.out.println("\n====================================================");
            System.out.println("   CARGA INICIAL TERMINADA CORRECTAMENTE");
            System.out.println("====================================================");
        } catch (NegocioException e) {
            System.err.println("\n[ERROR] No se pudo completar la carga: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar();
        }
        // Cierre limpio (EclipseLink mantiene hilos del pool de conexiones).
        System.exit(0);
    }

    private static void sembrarTiposMuestra(ICatalogoNegocio catalogo) throws NegocioException {
        System.out.println("\n--- Tipos de muestra ---");
        if (!catalogo.buscarTiposMuestra().isEmpty()) {
            System.out.println("Ya existen tipos de muestra registrados. Se omite esta seccion.");
            return;
        }
        for (String nombre : TIPOS_MUESTRA) {
            catalogo.registrarTipoMuestra(new GuardarTipoMuestraDTO(nombre));
            System.out.println("  + " + nombre);
        }
        System.out.println("Total insertados: " + TIPOS_MUESTRA.length);
    }

    private static void sembrarClientes(IClienteNegocio clientes) throws NegocioException {
        System.out.println("\n--- Clientes ---");
        if (!clientes.buscarTodos().isEmpty()) {
            System.out.println("Ya existen clientes registrados. Se omite esta seccion.");
            return;
        }
        // nombre, fecha nacimiento, sexo (M/F), tipo de sangre.
        registrarCliente(clientes, "María Fernanda López García", fecha(1990, 3, 15), "F", "O+");
        registrarCliente(clientes, "Juan Carlos Ramírez Soto", fecha(1985, 7, 22), "M", "A+");
        registrarCliente(clientes, "Ana Sofía Martínez Ruiz", fecha(2015, 11, 5), "F", "B+");
        registrarCliente(clientes, "Luis Ángel Hernández Díaz", fecha(1978, 1, 30), "M", "O-");
        registrarCliente(clientes, "Gabriela Torres Mendoza", fecha(1995, 9, 12), "F", "AB+");
        registrarCliente(clientes, "Roberto Carlos Flores Vega", fecha(2008, 6, 18), "M", "A-");
        registrarCliente(clientes, "Patricia Jiménez Castro", fecha(1962, 12, 3), "F", "B-");
        registrarCliente(clientes, "Diego Alejandro Morales Cruz", fecha(2000, 4, 25), "M", "O+");
        registrarCliente(clientes, "Valeria Guadalupe Sánchez Ríos", fecha(1988, 8, 9), "F", "AB-");
        registrarCliente(clientes, "Fernando Aguilar Núñez", fecha(1973, 2, 14), "M", "A+");
        System.out.println("Total insertados: 10");
    }

    private static void sembrarDoctores(IDoctorNegocio doctores) throws NegocioException {
        System.out.println("\n--- Doctores ---");
        if (!doctores.buscarTodos().isEmpty()) {
            System.out.println("Ya existen doctores registrados. Se omite esta seccion.");
            return;
        }
        registrarDoctor(doctores, "Dr. Alberto Medina Salas", "M");
        registrarDoctor(doctores, "Dra. Carmen Robles Ortega", "F");
        registrarDoctor(doctores, "Dr. Sergio Domínguez Lara", "M");
        registrarDoctor(doctores, "Dra. Lucía Ferreira Campos", "F");
        registrarDoctor(doctores, "Dr. Ricardo Beltrán Quiroz", "M");
        registrarDoctor(doctores, "Dra. Mónica Estrada Pineda", "F");
        registrarDoctor(doctores, "Dr. Héctor Navarro Lozano", "M");
        registrarDoctor(doctores, "Dra. Adriana Cervantes Mora", "F");
        registrarDoctor(doctores, "Dr. Pablo Guerrero Fuentes", "M");
        registrarDoctor(doctores, "Dra. Rosa Elena Vargas León", "F");
        System.out.println("Total insertados: 10");
    }

    private static void registrarCliente(IClienteNegocio clientes, String nombre, Date nacimiento,
            String sexo, String tipoSangre) throws NegocioException {
        clientes.registrarCliente(new GuardarClienteDTO(nombre, nacimiento, sexo, tipoSangre));
        System.out.println("  + " + nombre + " (" + sexo + ", " + tipoSangre + ")");
    }

    private static void registrarDoctor(IDoctorNegocio doctores, String nombre, String sexo)
            throws NegocioException {
        doctores.registrarDoctor(new GuardarDoctorDTO(nombre, sexo));
        System.out.println("  + " + nombre + " (" + sexo + ")");
    }

    /** Construye una fecha (mes en base 1, como se lee de forma natural). */
    private static Date fecha(int anio, int mes, int dia) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(anio, mes - 1, dia);
        return c.getTime();
    }
}
