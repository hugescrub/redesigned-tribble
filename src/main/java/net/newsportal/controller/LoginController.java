package net.newsportal.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class LoginController {
    @GetMapping("/login")
    String getLoginView() {
        return "login";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String login(Model model, @RequestBody MultiValueMap<String, String> formData) {
        String username = formData.get("username").get(0);
        String password = formData.get("password").get(0);

        // TODO Proper login flow
        if (username.equals("Mario") && password.equals("Luigi")) {
            return "redirect:/";
        }

        model.addAttribute("error", "Failed to login. Wrong username or password.");
        return "login";
    }
}
