package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView{
	
	@Override
	protected Object filterModel(Map<String, Object> model) {
		
		//Removemos del model el titulo y el page que son agregados en el controller
		model.remove("titulo");
		model.remove("page");
		
		//Ahora filtramos solo los clientes para que en el json el paginador no sea incluido
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		model.remove("clientes");
		model.put("clientes", clientes.getContent());
		
		return super.filterModel(model);
	}

}
