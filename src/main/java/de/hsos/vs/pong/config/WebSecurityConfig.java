package de.hsos.vs.pong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**")
                .requestMatchers("/js/**")
                .requestMatchers("/images/**")
                .requestMatchers("/WEB-INF/**")  // Ignoriert alle WEB-INF Requests komplett
                .requestMatchers("/h2-console/**");
    }
}
