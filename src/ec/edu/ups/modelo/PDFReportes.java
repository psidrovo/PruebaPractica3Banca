package ec.edu.ups.modelo;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paul idrovo
 */
public class PDFReportes {

    // Fonts definitions (Definición de fuentes).
    private static Font tituloFont;
    private static Font textoFont;
    private static Font textoFontPar;
    private static Font textoFontBold;

    private int saltoInicial;
    private int saltoFinal;

    private Chapter chapter = new Chapter(new Paragraph("", textoFont), 0);

    private static String iTextExampleImage = "";

    /**
     * We create a PDF document with iText using different elements to learn to
     * use this library.Creamos un documento PDF con iText usando diferentes
     * elementos para aprender a usar esta librería.
     *
     * @param pdfNewFile  <code>String</code> pdf File we are going to write.
     * Fichero pdf en el que vamos a escribir.
     * @param listaCuotas
     * @param valorTotal
     * @param interesTotal
     * @param mes

     *
     */

    public void createPDF(File pdfNewFile,List<DatosSistemaAleman> listaCuotas, String valorTotal, String interesTotal, int mes) {
        // We create the document and set the file name.        
        // Creamos el documento e indicamos el nombre del fichero.
        saltoInicial = 10;
        saltoFinal = 30;
        int letraPDF = 20;
        int letraPDF1 = letraPDF + 4;
        int letraPDF2 = letraPDF + 2;
        tituloFont = FontFactory.getFont(FontFactory.HELVETICA, letraPDF1, Font.BOLD);
        textoFont = FontFactory.getFont(FontFactory.HELVETICA, letraPDF, Font.NORMAL);
        textoFontPar = FontFactory.getFont(FontFactory.HELVETICA, letraPDF, Font.BOLD);
        textoFontBold = FontFactory.getFont(FontFactory.HELVETICA, letraPDF2, Font.BOLD);
        try {
            Document document = new Document();
            try {

                PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
                iTextExampleImage = "C:\\Programas Java\\UPS\\3ROCICLO\\Parqueadero\\Reportes\\fondoBanco.jpg";
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No such file was found to generate the PDF "
                        + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
            }

            document.setPageSize(new Rectangle(1594, 2256));
            document.open();
            // We add metadata to PDF
            // Añadimos los metadatos del PDF

            document.addTitle("REPORTE CUENTAS");

            boolean one = true;
            int i = 1;
            PdfPCell cell;
            PdfPTable tabla = new PdfPTable(5);     //Numero de columnas 
            tabla.setWidthPercentage(90); // Porcentaje de la pagina que ocupa
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);//Alineacion horizontal
            tabla.setWidths(new float[]{40, 40, 40, 40, 40});
            Fondo_Encabezado( valorTotal,  interesTotal,  mes);
            boolean primero = false;
            for (DatosSistemaAleman listaCuota : listaCuotas) {                          

                cell = new PdfPCell(new Paragraph(listaCuota.getNumeroCuota() + "", textoFontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
                tabla.addCell(cell);

                cell = new PdfPCell(new Paragraph(listaCuota.getCapital()+"", textoFontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
                tabla.addCell(cell);

                cell = new PdfPCell(new Paragraph(listaCuota.getAmortizacion()+"", textoFontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
                tabla.addCell(cell);

                cell = new PdfPCell(new Paragraph(listaCuota.getInteresPeriodo()+"", textoFontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
                tabla.addCell(cell);
                
                cell = new PdfPCell(new Paragraph(listaCuota.getCouta() + "", textoFontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
                tabla.addCell(cell);
               
                if (i > saltoFinal) {
                    i = 0;
                    if (primero) {
                        chapter.add(tabla);
                        document.add(chapter);
                        one = false;
                    }
                    chapter.clear();
                    Fondo_Encabezado(valorTotal, interesTotal, mes);
                }
                i++;
            }
            if (one) {
                chapter.add(tabla);
            }
            document.add(chapter);
            
            document.close();
        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        }
    }

    private void Fondo_Encabezado(String valorTotal, String interesTotal, int mes) {
        try {
            chapter.clear();
            //DATOS DEL PACIENTE
            chapter.setNumberDepth(0);
            for (int i = 0; i < saltoInicial; i++) {
                chapter.add(new Paragraph(" "));
            }
            chapter.add(new Paragraph("                                TABLA DE PRESTAMO", FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD, BaseColor.WHITE)));
            chapter.add(new Paragraph(""));
            chapter.add(new Paragraph("VALOR TOTAL: $"+valorTotal, FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD, BaseColor.WHITE)));
            chapter.add(new Paragraph("INTERES: %"+interesTotal, FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD, BaseColor.WHITE)));
            chapter.add(new Paragraph("CUOTAS: "+mes, FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD, BaseColor.WHITE)));

            for (int i = 0; i < 16; i++) {
                chapter.add(new Paragraph(" "));
            }

            Image image;
            try {
                image = Image.getInstance(iTextExampleImage);
                image.setAbsolutePosition(0, 2256 - image.getHeight());
                chapter.add(image);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            chapter.add(new Paragraph(" "));
            PdfPCell cell;
            PdfPTable tabla = new PdfPTable(5);     //Numero de columnas
             tabla.setWidthPercentage(90); // Porcentaje de la pagina que ocupa
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);//Alineacion horizontal
            tabla.setWidths(new float[]{40, 40, 40, 40, 40});

            cell = new PdfPCell(new Paragraph("N. DE CUOTA", textoFontBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph("CAPITAL AL INICIO DEL PERIODO", textoFontBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph("AMORTIZACION", textoFontBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
            tabla.addCell(cell);

            cell = new PdfPCell(new Paragraph("INTERESES DEL PERIODO", textoFontBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
            tabla.addCell(cell);
            cell = new PdfPCell(new Paragraph("CUOTA", textoFontBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
            tabla.addCell(cell);
            
            chapter.add(tabla);
        } catch (DocumentException ex) {
            Logger.getLogger(PDFReportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
