package cl.perfulandia.user.service;

import cl.perfulandia.user.dto.UserRequest;
import cl.perfulandia.user.dto.UserResponse;
import cl.perfulandia.user.model.User;
import cl.perfulandia.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAll() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getById(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToResponse(u);
    }

    @Transactional
    public UserResponse create(UserRequest req) {
        System.out.println("RAW password recibida: " + req.getPassword());

        User u = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();

        User saved = repo.save(u);
        return mapToResponse(saved);
    }

    @Transactional
    public UserResponse update(Long id, UserRequest req) {
        User u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole());

        User updated = repo.save(u);
        return mapToResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        repo.deleteById(id);
    }

    private UserResponse mapToResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .password("*********")
                .role(u.getRole())
                .build();
    }
}
