package net.breezeware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = { "net.breezeware.repository" })
@ComponentScan(basePackages = { "net.breezeware" })
// @EnableJpaRepositories(basePackages={"net.breezeware"})
@EntityScan(basePackages = { "net.breezeware.entity" })
public class CafeteriaManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeteriaManagementSystemApplication.class, args);
    }

}
