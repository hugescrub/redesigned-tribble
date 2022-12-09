package net.newsportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    String getLoginView() {
        return "login";
    }

    @PostMapping("/login")
    String login(Model model) {
        model.addAttribute("error", "Failed to login");
        return "login";
    }
}
