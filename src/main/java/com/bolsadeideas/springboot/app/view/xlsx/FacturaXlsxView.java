package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

//Diferenciamos de FacturaPdfView el nombre, agregando .xlsx
@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Factura factura = (Factura) model.get("factura");
		
		//Creamos nuestra hoja de excel y seteamos el nombre
		Sheet sheet = workbook.createSheet("Factura Spring");
		
		
		//DATOS DEL CLIENTE -----------------------------------------------------------------
		//1ra fila para los datos del cliente
		Row row = sheet.createRow(0);
		//1ra fila de la celda 1
		Cell cell = row.createCell(0);
		cell.setCellValue("Datos del cliente");
		
		//2da fila
		row = sheet.createRow(1);
		//1ra celda de 2da fila
		cell = row.createCell(0);
		//A la celda le seteamos los datos del cliente
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		
		//3ra fila
		row = sheet.createRow(2);
		//1ra columna de la 3ra file
		cell = row.createCell(0);
		//A la celda le seteamos los datos del email
		cell.setCellValue(factura.getCliente().getEmail());
		
		
		//DATOS DE LA FACTURA----------------------------------------------------------------
		//Forma mas directa de crear las filas y setear los valores
		
		//4ta fila para los datos de la factura
		sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");

		//5ta fila de la primera celda
		sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
		
		//6ta fila de la primera celda
		sheet.createRow(6).createCell(0).setCellValue("Descripcion: " + factura.getDescripcion());

		//7ma fila de la primera celda
		sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());
		
		//ITEMS DE LA FACTURA----------------------------------------------------------------
		
		//Creamos los nombres de las columnas de la tabla y sus titulos
		Row header = sheet.createRow(9);
		
		header.createCell(0).setCellValue("Producto");
		header.createCell(1).setCellValue("Precio");
		header.createCell(2).setCellValue("Cantidad");
		header.createCell(3).setCellValue("Total");

		//Ahora creamos cada item de la factura
		int rowContador = 10;
		
		for (ItemFactura item: factura.getItems()) {
			Row fila = sheet.createRow(rowContador++);
			fila.createCell(0).setCellValue(item.getProducto().getNombre());
			fila.createCell(1).setCellValue(item.getProducto().getPrecio());
			fila.createCell(2).setCellValue(item.getCantidad());
			fila.createCell(3).setCellValue(item.calcularImporte());

		}
		
		//GRAN TOTAL ------------------------------------------------------------------------
		
		Row filaTotal = sheet.createRow(rowContador);
		filaTotal.createCell(2).setCellValue("Gran total");
		filaTotal.createCell(3).setCellValue(factura.obtenerTotal());
		
	}

}
