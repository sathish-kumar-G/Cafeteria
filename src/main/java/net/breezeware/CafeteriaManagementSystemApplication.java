package net.breezeware;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Class
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = { "net.breezeware.repository" })
@ComponentScan(basePackages = { "net.breezeware" })
// @EnableJpaRepositories(basePackages={"net.breezeware"})
@EntityScan(basePackages = { "net.breezeware.entity" })
public class CafeteriaManagementSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(CafeteriaManagementSystemApplication.class, args);
    }

}
