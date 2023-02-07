package com.bolsadeideas.springboot.app;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Creo al iniciar el proyecto la carpeta uploads automaticamente
		FileSystemUtils.deleteRecursively(Paths.get("uploads").toFile());

		// Elimino al finalizar el proyecto la carpeta uploads
		Files.createDirectory(Paths.get("uploads"));

//		Este codigo nos sirve unicamente para encriptar las contrasenas, una vez tengamos las mismas encriptadas 
//		podes comentar este codigo

//		Generacion de 2 encriptaciones (ya que tengo 2 usuarios: admin y gonzalo)
//		String password = "123456";
//		
//		for (int i = 0; i < 2; i++) {
//			String bCyptPassword = passwordEncoder.encode(password);
//			System.out.println(bCyptPassword);
//		}

	}

}
