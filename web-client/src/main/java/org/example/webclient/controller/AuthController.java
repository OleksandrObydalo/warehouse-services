package org.example.webclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.webclient.dto.UserDTO;
import org.example.webclient.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        // If already logged in, redirect to home
        if (session.getAttribute("userId") != null) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        logger.info("Login attempt for username: {}", username);

        UserDTO user = authenticationService.authenticate(username, password);

        if (user != null) {
            // Store user info in session
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());

            logger.info("User {} logged in successfully", username);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Welcome, " + user.getUsername() + "!");

            return "redirect:/";
        } else {
            logger.warn("Failed login attempt for username: {}", username);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        session.invalidate();

        logger.info("User {} logged out", username);
        redirectAttributes.addFlashAttribute("successMessage",
                "You have been logged out successfully");

        return "redirect:/login";
    }
}

