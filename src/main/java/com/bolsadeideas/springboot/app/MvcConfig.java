package com.bolsadeideas.springboot.app;

import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		//Aca convertimos el resourcesPath para que sea absoluto. toUri() nos agrega el esquema file (que seria como el http://... etc
		String resourcesPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		
		log.info(resourcesPath);
		
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcesPath);
		
	}
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
		
	}
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
