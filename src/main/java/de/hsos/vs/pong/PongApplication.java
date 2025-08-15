package de.hsos.vs.pong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "de.hsos.vs.pong")
@EnableJpaRepositories("de.hsos.vs.pong.repository")
@EntityScan("de.hsos.vs.pong.model")
public class PongApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PongApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PongApplication.class);
    }
}