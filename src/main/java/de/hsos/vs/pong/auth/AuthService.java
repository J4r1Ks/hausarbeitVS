package de.hsos.vs.pong.auth;

import de.hsos.vs.pong.model.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    /**
     *  @Value wird verwendet, um ein sicheres Pepper zu generieren. Ist sehr wichtig,
     *  dass alles sicher gespeichert wird und der Pepper auch nicht im Quellcode vorkommt.
     */

    public AuthService(UserRepository userRepository, @Value("${security.pepper}") String pepper) {
        this.userRepository = userRepository;
        this.passwordUtil = new PasswordUtil(pepper);
    }
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u  -> passwordUtil.verifyPassword(password, u.getSalt(), u.getPasswordHash()));
    }

    /**
     * Registriet einen neuen Benutzer mit Salt+Hash
     */
    public User register(String username, String rawPassword) {
        String salt = passwordUtil.generateSalt();
        String hash = passwordUtil.hashPassword(rawPassword, salt);
        User u = new User();
        u.setUsername(username);
        u.setSalt(salt);
        u.setPasswordHash(hash);
        return userRepository.save(u);
    }
}
