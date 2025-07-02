package cl.perfulandia.user.service;

import cl.perfulandia.user.dto.UserRequest;
import cl.perfulandia.user.dto.UserResponse;
import cl.perfulandia.user.model.User;
import cl.perfulandia.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repo;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEncodesPasswordAndReturnsResponse() {
        UserRequest req = new UserRequest();
        req.setUsername("u1");
        req.setPassword("pw");
        req.setRole("USER");

        when(encoder.encode("pw")).thenReturn("hash");

        User saved = User.builder().id(1L).username("u1").password("hash").role("USER").build();
        when(repo.save(any())).thenReturn(saved);

        UserResponse resp = service.create(req);
        assertEquals(1L, resp.getId());
        verify(repo).save(any(User.class));
    }

    @Test
    void getByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(1L));
    }

    @Test
    void updateExistingUser() {
        User existing = User.builder().id(2L).username("old").password("h").role("USER").build();
        when(repo.findById(2L)).thenReturn(Optional.of(existing));
        when(encoder.encode("newpw")).thenReturn("newh");

        User saved = User.builder().id(2L).username("new").password("newh").role("ADMIN").build();
        when(repo.save(existing)).thenReturn(saved);

        UserRequest req = new UserRequest();
        req.setUsername("new");
        req.setPassword("newpw");
        req.setRole("ADMIN");

        UserResponse resp = service.update(2L, req);
        assertEquals("new", resp.getUsername());
    }
}
