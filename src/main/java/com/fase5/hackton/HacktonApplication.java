package com.fase5.hackton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HacktonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HacktonApplication.class, args);
	}

}
