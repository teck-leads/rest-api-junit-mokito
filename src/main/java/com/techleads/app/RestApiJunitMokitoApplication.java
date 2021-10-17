package com.techleads.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.techleads.app.repository.LibraryRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {LibraryRepository.class})
public class RestApiJunitMokitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiJunitMokitoApplication.class, args);
	}

}
