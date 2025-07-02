package cl.perfulandia.authentication.service;

import cl.perfulandia.authentication.dto.AuthResponse;
import cl.perfulandia.authentication.model.User;
import cl.perfulandia.authentication.repository.UserRepository;
import cl.perfulandia.authentication.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateSuccess() {
        User user = User.builder().id(1L).username("john").password("hash").role("ADMIN").build();
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hash")).thenReturn(true);
        when(jwtUtil.generateToken("john", "ADMIN")).thenReturn("token");

        AuthResponse resp = service.authenticate("john", "pass");
        assertEquals("token", resp.getToken());
        verify(jwtUtil).generateToken("john", "ADMIN");
    }

    @Test
    void userNotFoundThrows() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.authenticate("john", "pass"));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void invalidPasswordThrows() {
        User user = User.builder().id(1L).username("john").password("hash").role("ADMIN").build();
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hash")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.authenticate("john", "pass"));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }
}
