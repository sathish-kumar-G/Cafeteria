package net.breezewere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
/*@EnableJpaRepositories(basePackages = { "net.breezewere.repository" })
@ComponentScan(basePackages = { "net.breezewere" })
// @EnableJpaRepositories(basePackages={"net.breezewere"})
@EntityScan(basePackages = { "net.breezewere.entity" })*/
public class CafeteriaManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeteriaManagementSystemApplication.class, args);
    }

}
