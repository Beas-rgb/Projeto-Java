package com.projeto.copanheiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.projeto.copanheiro.repositories")
public class CopanheiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(CopanheiroApplication.class, args);
    }
}