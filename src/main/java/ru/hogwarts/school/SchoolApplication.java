package ru.hogwarts.school;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SchoolApplication.class)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}

	@PostConstruct
	public void init() {
		System.out.println(">>> Контекст инициализирован");
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			System.out.println("=== APPLICATION STARTED ===");
		};
	}
}
