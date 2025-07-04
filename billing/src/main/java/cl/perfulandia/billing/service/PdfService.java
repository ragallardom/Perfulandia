package cl.perfulandia.billing.service;

import cl.perfulandia.billing.model.Invoice;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(Invoice invoice) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Factura #" + invoice.getId()));
            document.add(new Paragraph("Cliente: " + invoice.getCustomerName()));
            document.add(new Paragraph("Monto: $" + invoice.getAmount()));
            document.add(new Paragraph("Pagado: " + (invoice.getPaid() ? "Sí" : "No")));
            document.add(new Paragraph("Método de pago: " + invoice.getPaymentMethod()));
            document.add(new Paragraph("Fecha: " + invoice.getDateIssued()));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }
}