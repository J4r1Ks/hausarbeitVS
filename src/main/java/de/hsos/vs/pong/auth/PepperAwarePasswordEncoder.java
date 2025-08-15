package de.hsos.vs.pong.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PepperAwarePasswordEncoder implements PasswordEncoder {
    private final String pepper;
    private final BCryptPasswordEncoder bcrypt;

    public PepperAwarePasswordEncoder(@Value("${security.pepper}") String pepper) {
        this.pepper = pepper;
        this.bcrypt = new BCryptPasswordEncoder();
    }

    private String applyPepper(CharSequence raw) {
        return raw == null ? null : raw.toString() + pepper;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return bcrypt.encode(applyPepper(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bcrypt.matches(applyPepper(rawPassword), encodedPassword);
    }
}