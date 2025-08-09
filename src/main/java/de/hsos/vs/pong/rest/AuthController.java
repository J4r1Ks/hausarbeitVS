package de.hsos.vs.pong.rest;

import de.hsos.vs.pong.auth.PasswordService;
import de.hsos.vs.pong.repository.UserRepository;
import de.hsos.vs.pong.model.User;
import jakarta.servlet.http.HttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    public AuthController(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }


    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if(userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error";
        }

        String hashed = passwordService.encodeWithPepper(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashed);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/lobby")
    public String homePage() {
        return "lobby"; // Geschützte Seite
    }
}
