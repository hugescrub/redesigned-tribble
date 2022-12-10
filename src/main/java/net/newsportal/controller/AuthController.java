package net.newsportal.controller;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.payload.request.UserLoginRequest;
import net.newsportal.payload.request.UserRegisterRequest;
import net.newsportal.payload.response.MessageResponse;
import net.newsportal.payload.response.UserDetailsResponse;
import net.newsportal.repository.UserRepository;
import net.newsportal.security.UserDetailsImpl;
import net.newsportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/portal/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepository userRepository,
                          UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        if (userService.registerUser(request)) {
            return ResponseEntity.ok()
                    .body(new UserDetailsResponse(
                            request.getUsername(),
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getRole()
                    ));
        } else {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Such username or email already exists.")
                    );
        }
    }

    @PostMapping("/login")
    // TODO delegate to UserService
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok()
                .body(new MessageResponse("Logged in successfully with id _" + userDetails.getId()));
    }
}
