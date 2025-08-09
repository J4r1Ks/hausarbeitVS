package de.hsos.vs.pong.rest;

import de.hsos.vs.pong.auth.PepperAwarePasswordEncoder;
import de.hsos.vs.pong.repository.UserRepository;
import de.hsos.vs.pong.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PepperAwarePasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PepperAwarePasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Ungültige Anmeldedaten.");
        if (logout != null) model.addAttribute("msg", "Erfolgreich ausgeloggt.");
        return "login"; // view: /WEB-INF/views/login.jsp
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Benutzername bereits vorhanden");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/lobby")
    public String lobby() {
        return "lobby";
    }
}