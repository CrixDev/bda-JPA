package presentacion;

import dto.ReporteResultadoDTO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Dibuja el informe de resultados con un estilo tipo "Informe de Resultados"
 * (logo, encabezado del laboratorio, folio con código de barras, bloque de datos
 * del paciente y tabla PRUEBA / RESULTADO / UNIDAD / REFERENCIA). El mismo
 * dibujo se usa para mostrar en pantalla y para imprimir.
 */
public class ReporteVista extends JPanel implements Printable {

    private static final int W = 600;       // ancho de diseño del contenido
    private static final int MARGEN = 30;

    // Columnas de la tabla de resultados.
    private static final int COL_PRUEBA = 0;
    private static final int COL_RESULTADO = 250;
    private static final int COL_UNIDAD = 380;
    private static final int COL_REFERENCIA = 470;

    private static final Color GRIS_LINEA = new Color(0xBBBBBB);

    private Image logo;
    private List<ReporteResultadoDTO> datos;

    public ReporteVista() {
        setBackground(Color.WHITE);
        try {
            logo = ImageIO.read(getClass().getResource("/recursos/logoimss.jpeg"));
        } catch (Exception e) {
            logo = null;
        }
    }

    public void setDatos(List<ReporteResultadoDTO> datos) {
        this.datos = datos;
        setPreferredSize(new Dimension(W + 2 * MARGEN, calcularAlto()));
        revalidate();
        repaint();
    }

    private int calcularAlto() {
        int filas = (datos == null) ? 0 : datos.size();
        return 2 * MARGEN + 230 + filas * 22 + 50;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (datos == null || datos.isEmpty()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.translate(MARGEN, MARGEN);
        dibujar(g2);
        g2.dispose();
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) {
        if (page > 0 || datos == null || datos.isEmpty()) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        double escala = pf.getImageableWidth() / (W + 2.0 * MARGEN);
        if (escala > 1) {
            escala = 1;
        }
        g2.scale(escala, escala);
        g2.translate(MARGEN, MARGEN);
        dibujar(g2);
        return PAGE_EXISTS;
    }

    // ── Dibujo del informe ───────────────────────────────────────────────────
    private void dibujar(Graphics2D g) {
        ReporteResultadoDTO h = datos.get(0);
        g.setColor(Color.BLACK);

        // Línea superior con el nombre del laboratorio.
        g.setFont(new Font("Serif", Font.PLAIN, 10));
        centrar(g, "Laboratorio Clínico Salud Total", 12);

        // Logo arriba a la izquierda.
        if (logo != null) {
            g.drawImage(logo, 0, 16, 70, 60, null);
        } else {
            g.drawRect(0, 16, 70, 60);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("IMSS", 12, 52);
        }

        // Títulos centrales.
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 15));
        centrar(g, "LABORATORIO CLÍNICO", 36);
        g.setFont(new Font("Serif", Font.BOLD, 13));
        centrar(g, "INFORME DE RESULTADOS", 54);

        // Folio (derecha) + código de barras.
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        String folioTxt = "FOLIO: " + safe(h.getFolio());
        int fw = g.getFontMetrics().stringWidth(folioTxt);
        g.drawString(folioTxt, W - fw, 24);
        dibujarBarras(g, W - 170, 30, 170, 36, safe(h.getFolio()));
        g.setFont(new Font("Monospaced", Font.PLAIN, 9));
        int cw = g.getFontMetrics().stringWidth(safe(h.getFolio()));
        g.drawString(safe(h.getFolio()), W - 170 + (170 - cw) / 2, 78);

        // ── Bloque de datos del paciente (con recuadro) ──
        int cajaY = 92;
        int cajaH = 70;
        g.setColor(Color.BLACK);
        g.drawRect(0, cajaY, W, cajaH);

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = h.getFechaHora() != null ? fmt.format(h.getFechaHora()) : "-";
        String medico = (h.getNombreDoctor() != null && !h.getNombreDoctor().isBlank())
                ? h.getNombreDoctor() : "A quien corresponda";

        int izq = 8;
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        g.drawString("PACIENTE: " + safe(h.getNombreCliente()), izq, cajaY + 18);
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.drawString("FECHA DE INGRESO: " + fecha, izq, cajaY + 38);
        g.drawString("MÉDICO SOLICITANTE: " + medico, izq, cajaY + 58);

        int der = W - 180;
        g.drawString("SEXO: " + sexoTexto(h.getSexo()), der, cajaY + 18);
        g.drawString("EDAD: " + (h.getEdad() != null ? h.getEdad() + " años" : "-"), der, cajaY + 38);

        // ── Tabla de resultados ──
        int y = cajaY + cajaH + 26;
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        g.drawString("PRUEBA", COL_PRUEBA, y);
        g.drawString("RESULTADO", COL_RESULTADO, y);
        g.drawString("UNIDAD", COL_UNIDAD, y);
        g.drawString("REFERENCIA", COL_REFERENCIA, y);
        y += 5;
        g.setColor(Color.BLACK);
        g.drawLine(0, y, W, y);
        y += 17;

        for (ReporteResultadoDTO r : datos) {
            // PRUEBA
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g.drawString(safe(r.getNombreParametro()), COL_PRUEBA, y);

            // RESULTADO (en negritas y rojo si está fuera de rango)
            if (r.isFueraDeRango()) {
                g.setFont(new Font("SansSerif", Font.BOLD, 10));
                g.setColor(new Color(0xCC0000));
            }
            String valor = safe(r.getValor());
            if (r.isFueraDeRango()) {
                valor += "  (*)";
            }
            g.drawString(valor, COL_RESULTADO, y);

            // UNIDAD y REFERENCIA
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g.drawString(r.getUnidadMedida() != null ? r.getUnidadMedida() : "", COL_UNIDAD, y);
            g.drawString(r.getRangoTexto(), COL_REFERENCIA, y);

            g.setColor(GRIS_LINEA);
            g.drawLine(0, y + 5, W, y + 5);
            y += 22;
        }

        // ── Pie ──
        y += 10;
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.ITALIC, 9));
        boolean hayFuera = datos.stream().anyMatch(ReporteResultadoDTO::isFueraDeRango);
        if (hayFuera) {
            g.drawString("(*) Valor fuera del rango de referencia para la edad del paciente.", 0, y);
            y += 14;
        }
        g.drawString("Liberado: " + fecha + "  —  Sistema Salud Total", 0, y);
    }

    /** Dibuja un código de barras (decorativo) derivado del folio. */
    private void dibujarBarras(Graphics2D g, int x, int y, int ancho, int alto, String folio) {
        String d = folio.replaceAll("\\D", "");
        if (d.isEmpty()) {
            d = "1234567890";
        }
        g.setColor(Color.BLACK);
        int cx = x;
        int i = 0;
        while (cx < x + ancho - 3) {
            int digito = d.charAt(i % d.length()) - '0';
            int barW = 1 + (digito % 3);          // ancho de barra 1..3
            g.fillRect(cx, y, barW, alto);
            cx += barW;
            int gap = 1 + ((digito + i) % 3);     // espacio 1..3
            cx += gap;
            i++;
        }
    }

    private void centrar(Graphics2D g, String s, int baseY) {
        int sw = g.getFontMetrics().stringWidth(s);
        g.drawString(s, (W - sw) / 2, baseY);
    }

    private String sexoTexto(String sexo) {
        if (sexo == null) {
            return "-";
        }
        return switch (sexo.trim().toUpperCase()) {
            case "M" -> "MASCULINO";
            case "F" -> "FEMENINO";
            default -> sexo;
        };
    }

    private String safe(String s) {
        return s != null ? s : "";
    }
}
