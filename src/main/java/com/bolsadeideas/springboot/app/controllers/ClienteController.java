package com.bolsadeideas.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.ClienteServiceImp;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private ClienteServiceImp clienteService;
	
	private final static String UPLOAD_FOLDER = "uploads";
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	// Este metodo lista los clientes
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		// Paginador
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		// Atributos que se le pasan a la vista
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	// Este metodo se encarga de mostrar el formulario
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Formulario de cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@RequestMapping(value = "/ver/{id}", method = RequestMethod.GET)
	public String ver(@PathVariable(name = "id") Long id, Model model) {

		Cliente cliente = clienteService.findOne(id);
		if (cliente.getId() == null) {
			model.addAttribute("titulo", "El cliente no existe en la base de datos");
			return "/redirect:/listar";
		} else {
			model.addAttribute("cliente", cliente);
			model.addAttribute("titulo", "Detalle clientes: " + cliente.getNombre());

		}

		return "ver";

	}

	// Este metodo se encarga de persistir el formulario
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model,
			@RequestParam(name = "file") MultipartFile foto, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}

		// Logica de si hay o no foto al guardar el usuario
		if (!foto.isEmpty()) {

			//Constatamos que el cliente exista y a su vez que tambien tenga foto (por lo que eliminamos la foto)
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				// Busco la ruta absoluta de la foto
				Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();

				// Ahora a partir de la ruta obtengo el objeto File de la foto
				File archivo = rootPath.toFile();

				// Si la foto existe o se puede leer, la elimino
				if (archivo.exists() && archivo.canRead()) {
					if (archivo.delete()) {
						model.addAttribute("titulo", "La imagen fue eliminada");
					}

				}
			}

			// Implementcion de nombre de la foto
			String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

			// Ruta relativo del proyecto + nombre del archivo (en este caso una foto)
			Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(uniqueFileName);

			// Ruta absoluta (es decir ruta completa (no la relativa)
			Path rootAbsolutePath = rootPath.toAbsolutePath();

			log.info("uniqueFileName: " + uniqueFileName);
			log.info("rootAbsolutePath: " + rootAbsolutePath);
			log.info("rootPath: " + rootPath);

			// Obtenemos los bytes de la imagen
			try {
				// Contenido de la imagen como un array de bytes
				byte[] bytes = foto.getBytes();

				// Escribe la foto a la ruta completa
				Files.write(rootAbsolutePath, bytes);

				cliente.setFoto(uniqueFileName);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		clienteService.save(cliente);
		status.setComplete();
		return "redirect:listar";
	}

	// Este metodo se encarga de mostrar a cada cliente
	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(value = "id") Long id, Model model) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);

		} else {
			return "redirect:/listar";

		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar Cliente");
		return "form";

	}

	// Este metodo se encarga de eliminar un cliente
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model) {

		if (id > 0) {

			// Paso a eliminar la foto si se elimina el usuario, por lo que voy a buscar al
			// usuario antes de eliminarlo
			Cliente cliente = clienteService.findOne(id);

			clienteService.delete(id);

			// Busco la ruta absoluta de la foto
			Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();

			// Ahora a partir de la ruta obtengo el objeto File de la foto
			File archivo = rootPath.toFile();

			// Si la foto existe o se puede leer, la elimino
			if (archivo.exists() && archivo.canRead()) {
				if (archivo.delete()) {
					model.addAttribute("titulo", "La imagen fue eliminada");
				}

			}
		}

		return "redirect:/listar";
	}
}
