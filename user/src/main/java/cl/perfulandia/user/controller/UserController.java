package cl.perfulandia.user.controller;

import cl.perfulandia.user.dto.*;
import cl.perfulandia.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @GetMapping
    public List<UserResponse> all() { return svc.getAll(); }

    @GetMapping("/{id}")
    public UserResponse one(@PathVariable Long id) { return svc.getById(id); }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest req) {
        return ResponseEntity.ok(svc.create(req));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody UserRequest req) {
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

}