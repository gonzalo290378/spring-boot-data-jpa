package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, Principal principal, 
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout
			) {
		
		//Ya habia iniciado session, y el if es para evitar doble inicio sesion
		if(principal != null) {
			model.addAttribute("inicioSesion", "El usuario ha iniciado sesion");
			return "redirect:/";
		}
		
		if(error != null) {
			model.addAttribute("error", "Error en el login: Nombre de usuario o contrasenia incorrecta. Vuelva a intentarlo.");
		}
		
		if(logout != null) {
			model.addAttribute("success", "Ha cerrado sesion con exito");
		}
		return "login";
	}

}
