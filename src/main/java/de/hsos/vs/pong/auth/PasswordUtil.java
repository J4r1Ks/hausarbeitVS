package de.hsos.vs.pong.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/*
 * Tutorial für salt und pepper: https://www.baeldung.com/java-password-hashing
 */
public class PasswordUtil {

    private String pepper; // Sollte eventuell noch verbessert werden
    private final SecureRandom random = new SecureRandom();

    public PasswordUtil(String pepper) {
        this.pepper = pepper;
    }
    public String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String input = salt + password + pepper;
            byte[] hashed = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    // Überprüft ein Klartext-Passwort gegen gespeicherte Hash+Salt
    public boolean verifyPassword(String password, String salt, String hash) {
        return hashPassword(password, salt).equals(hash);
    }
}
