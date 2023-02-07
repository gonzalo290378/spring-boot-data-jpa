package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
//Anotamos en la clase para que todos los metodos contemplen esta anotacion
@Secured("ROLE_ADMIN")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	// Me muestra el formulario para crear una factura del cliente al cual le
	// pasamos en el clienteId
	@GetMapping("/form/{id}")
	public String crear(@PathVariable("id") Long id, Model model) {

		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			return "/redirect:/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Crear Factura");

		return "factura/form";
	}

	// Autocomplete del input "Buscar Producto"
	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable(value = "term") String term) {
		List<Producto> listaAutocomplete = clienteService.findByNombre("%" + term + "%");
		return listaAutocomplete;
	}

	// Guarda la factura correspondiente al cliente
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, SessionStatus status) {

		// Si hay errores en los atributos de la plantilla se mostraran los mismos
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";

		}

		// Si no hay ninguna linea agregada el metodo nos llevara de vuelta al
		// formulario
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "Error la factura no puede no tener lineas");
			return "factura/form";

		}

		// Hacemos un for por las lineas de los productos que agregamos
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			log.info("ID :" + itemId[i].toString() + ", cantidad: " + cantidad[i]);
		}

		clienteService.saveFactura(factura);

		status.setComplete();

		return "redirect:/ver/" + factura.getCliente().getId();

	}

	// Ver datos de la factura que se genero en cada cliente
	@RequestMapping(value = "/ver/{id}", method = RequestMethod.GET)
	public String ver(@PathVariable(value = "id") Long id, Model model) {

		//Factura factura = clienteService.findFacturaById(id);
		Factura factura = clienteService.fetchByIdWithClienteWhitItemFacturaWithProducto(id);	
		
		if (factura == null) {
			model.addAttribute("errorFactura", "La factura no existe en la base de datos");
			return "redirect:/listar";
		} else {
			model.addAttribute("factura", factura);
			model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
		}

		return "factura/ver";
	}

	@RequestMapping(value = "/eliminar/{id}", method = RequestMethod.GET)
	public String eliminar(@PathVariable(value = "id") Long id, Model model) {

		Factura factura = clienteService.findFacturaById(id);

		if (factura != null) {
			clienteService.deleteFactura(id);
			return "redirect:/ver/" + factura.getCliente().getId();
		}

		else {
			model.addAttribute("facturaError", "La factura no existe en la base de datos");
		}

		return "redirect:/listar";
	}

}
