package com.prix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.prix.user")
@EnableJpaRepositories("com.prix.user")
public class PrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrixApplication.class, args);
    }
}