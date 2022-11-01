package com.bolsadeideas.springboot.app;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Creo al iniciar el proyecto la carpeta uploads automaticamente
		FileSystemUtils.deleteRecursively(Paths.get("uploads").toFile());

		// Elimino al finalizar el proyecto la carpeta uploads
		Files.createDirectory(Paths.get("uploads"));

	}

}
