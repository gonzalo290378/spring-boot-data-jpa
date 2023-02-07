package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bolsadeideas.springboot.app.models.dao.IUsuarioDAO;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService{

	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	private final Logger log = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	//UserDetails representa a un usuario autenticado
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//Obtenemos el usuario a traves del username
		Usuario usuario = usuarioDAO.findByUsername(username);
		
		if(usuario == null) {
			log.error("Error login: no existe el usuario " + username);
			throw new UsernameNotFoundException("Username " + username + " no existe en el sistena");
		}
		
		//Obtener los roles del usuario y registrarlos en una lista
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role: usuario.getRoles()) {
			log.error("Role ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			log.error("Error login: no existe el usuario " + username + " no tiene un rol asignado");
			throw new UsernameNotFoundException("Error login: no existe el usuario " + username + " no tiene un rol asignado");
		}
	
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
