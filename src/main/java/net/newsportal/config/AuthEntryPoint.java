package net.newsportal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        final String genericMessage = "http 401_" + exception.getMessage();

        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");

        PrintWriter writer = response.getWriter();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.println(genericMessage);
        log.warn(genericMessage);
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("news-portal");
        super.afterPropertiesSet();
    }
}
