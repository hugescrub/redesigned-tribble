package net.newsportal.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
public class CookiesFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "CkieUsrSessionID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        authCookie.ifPresent(cookie ->
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(cookie.getValue(), null)));

        authCookie.ifPresent(cookie -> log.info("Cookie: " + cookie.getName() + " : " + cookie.getValue()));

        // continue with the filter chain
        filterChain.doFilter(request, response);
    }

}

