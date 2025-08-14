package de.hsos.vs.pong.config;

import de.hsos.vs.pong.auth.PepperAwarePasswordEncoder;
import de.hsos.vs.pong.auth.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PepperAwarePasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          PepperAwarePasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder);
        return p;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
/*
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/chat/**")
                        .ignoringRequestMatchers("/quong/**")
                )

 */

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/register",
                                "/login",
                                "/perform_login",
                                "/error",
                                "/js/**",
                                "/quong"
                        ).permitAll()
                       // .requestMatchers("/api/chat/**").authenticated()
                       // .requestMatchers("/quong/**").authenticated()
                        .anyRequest().authenticated()
                )

                // Headers für H2 Console
                /*
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                )
                 */

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/lobby", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        http.authenticationProvider(authProvider());
        return http.build();
    }
}