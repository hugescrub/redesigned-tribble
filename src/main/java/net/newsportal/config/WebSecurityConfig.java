package net.newsportal.config;

import net.newsportal.security.CookiesFilter;
import net.newsportal.security.UserAuthenticationEntryPoint;
import net.newsportal.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetails;
    private final CookiesFilter cookiesFilter;
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetails,
                             CookiesFilter cookiesFilter,
                             UserAuthenticationEntryPoint userAuthenticationEntryPoint) {
        this.userDetails = userDetails;
        this.cookiesFilter = cookiesFilter;
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .sessionManagement()
                    .maximumSessions(1)
                    .expiredUrl("/expired")
                    .and()
                    .invalidSessionUrl("/login")
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation()
                    .migrateSession()
                    .and()
                    .logout().logoutUrl("/logout")
                    .deleteCookies(cookiesFilter.COOKIE_NAME, "JSESSIONID")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                .and()
                .authorizeRequests()
                // public endpoints
                .antMatchers("/portal/auth/**", "/", "/login", "/article/**", "/about", "/register")
                .permitAll()
                // other endpoints private
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(cookiesFilter, ChannelProcessingFilter.class)
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint);

        http.csrf().disable();
    }
}
