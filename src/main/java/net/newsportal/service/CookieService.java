package net.newsportal.service;

import net.newsportal.models.ERole;
import net.newsportal.models.Role;
import net.newsportal.models.User;
import net.newsportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;

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
        if(userRepository.existsByUsername(username)) {
            Cookie cookie = new Cookie("CkieUsrSessionID", userRepository.findByUsername(username).getId().toString());
            cookie.setMaxAge(3600); // seconds
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            return cookie;
        } else
            throw new UsernameNotFoundException("No user found with such username");
    }

    public String getRoleFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "CkieUsrSessionID");
        String userRole = "";
        if (cookie != null) {
            User user = userRepository.findById(Long.valueOf(cookie.getValue())).get();
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
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
