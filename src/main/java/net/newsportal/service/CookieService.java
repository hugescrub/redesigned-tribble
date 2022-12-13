package net.newsportal.service;

import net.newsportal.models.ERole;
import net.newsportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
public class CookieService {

    private final UserRepository userRepository;

    @Autowired
    public CookieService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean requestContainsCookie(String key, HttpServletRequest request) {
        if (request.getCookies() != null)
            return Arrays.stream(request.getCookies())
                .anyMatch(cookie -> key.equals(cookie.getName()));
        else
            return false;
    }

    public Cookie createCookie(String username) {
        if(userRepository.existsByUsername(username))
            return new Cookie("CkieUsrSessionID", userRepository.findByUsername(username).getId().toString());
        else
            throw new UsernameNotFoundException("No user found with such username");
    }

    public String getRoleFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "CkieUsrSessionID");
        String userRole = "";
        if (cookie != null) {
            var user = userRepository.findById(Long.valueOf(cookie.getValue()));
            var roles = user.get().getRoles();
            for (var role : roles) {
                if (role.getRole() == ERole.ROLE_ADMIN) {
                    userRole = "admin";
                } else if (role.getRole() == ERole.ROLE_CREATOR) {
                    userRole = "creator";
                }
            }
        }
        return userRole;
    }
}
