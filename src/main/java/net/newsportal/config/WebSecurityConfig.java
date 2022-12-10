package net.newsportal.config;

import net.newsportal.repository.UserRepository;
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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final AuthEntryPoint entryPoint;
    private final UserDetailsServiceImpl userDetails;

    @Autowired
    public WebSecurityConfig(UserRepository userRepository, AuthEntryPoint entryPoint, UserDetailsServiceImpl userDetails) {
        this.userRepository = userRepository;
        this.entryPoint = entryPoint;
        this.userDetails = userDetails;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().httpBasic().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // public endpoints
                .authorizeRequests().antMatchers("/portal/auth/**", "/", "/login", "/article/**").permitAll()
                //.regexMatchers("^\\/items\\?id=\\d(?:\\d+)?$", "^\\/menu\\?type=(?:BREAKFAST|GENERIC|DINNER|LUNCH)$").permitAll()
                // other endpoints private
                .anyRequest().authenticated();
    }
}
