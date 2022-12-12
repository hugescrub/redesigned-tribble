package net.newsportal.controller;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.repository.UserRepository;
import net.newsportal.security.CookiesFilter;
import net.newsportal.security.UserDetailsServiceImpl;
import net.newsportal.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final CookieService cookieService;

    @Autowired
    public LoginController(UserRepository userRepository,
                           CookieService cookieService,
                           UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.cookieService = cookieService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(Model model, @RequestBody MultiValueMap<String, String> formData, HttpServletRequest request, HttpServletResponse response) {
        String username = formData.get("username").get(0);
        String password = formData.get("password").get(0);
        if(userRepository.existsByUsername(username)){
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password)
            );
        }

        // get user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // create cookie
        if(!cookieService.requestContainsCookie(CookiesFilter.COOKIE_NAME, request)) {
            Cookie cookie = cookieService.createCookie(username);
            response.addCookie(cookie);
            log.warn("Cookie: " + cookie.getName() + ":" + cookie.getValue());
            log.warn("Logged in with username - " + userDetails.getUsername());
            return "redirect:/";
        }
        log.warn("Failed login attempt was performed");
        model.addAttribute("error", "Failed to login. Wrong username or password.");
        return "login";
    }
}
