package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource dataSocurce;
	
	@Autowired
	private JpaUserDetailsService jpaUserDetailsService;
		
//	Esto es para JDBC
//	@Bean
//	public UserDetailsService userDetailsService() {
//
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(User.withUsername("gonzalo").password(passwordEncoder.encode("123456")).roles("USER").build());
//		manager.createUser(User.withUsername("admin").password(passwordEncoder.encode("123456")).roles("ADMIN", "USER").build());
//		return manager;
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Rutas Publicas
		http.authorizeHttpRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/list-rest", "/api/clientes/**").permitAll()
		.anyRequest().authenticated()

		//Esto lo hacemos ahora por medio de anotaciones:
		
		//Rutas Privadas donde podemos comentar el codigo pues podemos hacerlo mediante anotaciones
		/*.antMatchers("/ver/**").hasAnyRole("USER")
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()*/
		
		//Implementacion del formulario de login y logout
		.and()
			.formLogin().loginPage("/login").permitAll()
		.and()
			.logout().permitAll()
		
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");

	}
	
	//Esto es de JDBC:
	
	/*@Autorired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
		
		build
		.jdbcAuthentication()
		.dataSource(dataSocurce)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("SELECT  username, passsword, enabled FROM users WHERE username = ?")
		.authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON a.user_id = u.id WHERE u.username = ?");
		
	}*/
	
	//Esto es para JPA
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
		build.userDetailsService(jpaUserDetailsService)
		.passwordEncoder(passwordEncoder);
	
	}
	
}
