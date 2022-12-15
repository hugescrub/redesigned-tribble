package net.newsportal.service;

import net.newsportal.models.ERole;
import net.newsportal.models.Role;
import net.newsportal.models.User;
import net.newsportal.payload.request.UserRegisterRequest;
import net.newsportal.repository.RoleRepository;
import net.newsportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    public boolean registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return false;
        } else if (userRepository.existsByEmail(request.getEmail())) {
            return false;
        }

        User user = new User(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                encoder.encode(request.getPassword())
        );

        String role = request.getRole();
        Role assignedRole;
        if (role == null) {
            assignedRole = roleRepository.findByRole(ERole.ROLE_CREATOR)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        } else if (role.equals("admin")) {
            assignedRole = roleRepository.findByRole(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        } else {
            assignedRole = roleRepository.findByRole(ERole.ROLE_CREATOR)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        }
        user.getRoles().add(assignedRole);
        userRepository.save(user);
        return true;
    }
}
