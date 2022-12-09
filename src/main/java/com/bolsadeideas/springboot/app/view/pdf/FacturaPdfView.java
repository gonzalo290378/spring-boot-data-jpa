package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

//El component va a ser un Bean que sera inyectado en todo el contenedor y refiere al String factura/ver que es 
//la salida del metodo /ver de FacturaController.
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView{

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Aca lo que hacemos es obtener los datos del model que se encuentra en la vista
		Factura factura = (Factura)model.get("factura");
		
		//DATOS DEL CLIENTE ---------------------------------------------------------------
		//Una sola columna para los datos del cliente
		PdfPTable tabla = new PdfPTable(1);
		
		//Genera un espacio
		tabla.setSpacingAfter(20);
		
		//Customizamos a la celda del titulo Datos del cliente un color
		PdfPCell cell = null;		
		cell = new PdfPCell(new Phrase("Datos del cliente: "));
		cell.setBackgroundColor(new Color(184,218,254));
		cell.setPadding(8f);
		tabla.addCell(cell);
		
		//Seteamos los datos del cliente
		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());
		
		//Agregamos la tabla al documento
		document.add(tabla);

		//Dos columnas para los datos de la factura
		PdfPTable tabla2 = new PdfPTable(1);
		
		//Genera un espacio
		tabla2.setSpacingAfter(20);

		//DATOS DE LA FACTURA ----------------------------------------------------------------
		
		//Customizamos a la celda del titulo Datos de la factura color
		cell = new PdfPCell(new Phrase("Datos de la factura: "));
		cell.setBackgroundColor(new Color(195,230,203));
		cell.setPadding(8f);
		tabla2.addCell(cell);
		
		//Seteamos los datos de la factura
		tabla2.addCell("Folio: " + factura.getId());
		tabla2.addCell("Descripcion: " + factura.getDescripcion());
		tabla2.addCell("Fecha: " + factura.getCreateAt());
		
		//Agregamos la tabla al documento
		document.add(tabla2);
		
		//DATOS DE LOS ITEMS DE LA FACTURA -----------------------------------------------------
		//Cuatro columnas para los items de la factura
		PdfPTable tabla3 = new PdfPTable(4);
		
		//Cambiamos el tamano de los titulos de la tabla
		tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
		
		//Genera un espacio
		tabla3.setSpacingAfter(20);
		
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		tabla3.addCell("Total");
		
		//Agrega los items de las columnas en este bucle for
		for (ItemFactura item : factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());
			tabla3.addCell(item.getCantidad().toString());
			tabla3.addCell(item.calcularImporte().toString());
			
		}
		
		//GRAN TOTAL ----------------------------------------------------------------------------
		
		//Celda para el footer del total
		cell = new PdfPCell(new Phrase("Total: "));
		
		//Seteamos el objeto cell para que ocupe 3 columnas (producto, precio y cantidad
		cell.setColspan(3);
		
		//Alineamos el texto a la derecha
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		
		//Y ahora cubrimos la ultima columna que es el gran total
		tabla3.addCell(factura.obtenerTotal().toString());
		
		document.add(tabla3);
		
	}




	

}
