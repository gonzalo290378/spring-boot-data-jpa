package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	//Metodo handler del controlador que va a responder en formato Json y es de tipo REST                                                
		@RequestMapping(value = "/listar", method = RequestMethod.GET)
		public List<Cliente> listar(){
			
			return clienteService.findAll();
			
		}

}
