package org.example.webclient.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page and navigation.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        // Explicitly add session attributes to model for Thymeleaf
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("username", session.getAttribute("username"));
        return "index";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        // Explicitly add session attributes to model for Thymeleaf
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("username", session.getAttribute("username"));
        return "index";
    }
}

