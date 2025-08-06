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
public class AuthController extends HttpServlet {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordService passwordService;


    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        String encodedPassword = passwordEncoder.encode(password);
        String pepperPassword = passwordService.encodeWithPepper(encodedPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(pepperPassword);
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
