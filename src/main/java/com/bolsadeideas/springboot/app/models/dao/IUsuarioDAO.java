package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Repository
public interface IUsuarioDAO extends CrudRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);

}
