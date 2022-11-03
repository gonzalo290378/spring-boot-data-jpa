package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping("/factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;

	// Lo que pasamos en el id es el clienteId
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

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable(value = "term")  String term) {
		List<Producto> listaAutocomplete = clienteService.findByNombre("%" + term + "%");
		return listaAutocomplete;
	}
}
