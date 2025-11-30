package com.quocchung.dangbai.duandangbai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DuandangbaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuandangbaiApplication.class, args);
	}

}
