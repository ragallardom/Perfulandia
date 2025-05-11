package cl.perfulandia.authentication.controller;

import cl.perfulandia.authentication.dto.AuthRequest;
import cl.perfulandia.authentication.dto.AuthResponse;
import cl.perfulandia.authentication.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request,
            HttpServletResponse response
    ) {
        String jwt = String.valueOf(authService.authenticate(request.getUsername(), request.getPassword()));

        // Lo enviamos también como Cookie HttpOnly
        Cookie cookie = new Cookie("token", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
