package de.hsos.vs.pong.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Value("${security.pepper}")
    private String pepper;

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encodeWithPepper(String password) {
        return passwordEncoder.encode(password + pepper);
    }

    public boolean matchesWithPepper(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword + pepper, encodedPassword);
    }
}