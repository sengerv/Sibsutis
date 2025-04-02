package org.openapitools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.openapitools.repository") // Пакет с репозиториями
@EntityScan(basePackages = "org.openapitools.model") // Пакет с сущностями
public class OpenApiGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenApiGeneratorApplication.class, args);
    }
}