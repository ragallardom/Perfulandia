package cl.perfulandia.authentication.util;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private JwtParser parser;

    @PostConstruct
    public void init() {
        this.parser = Jwts.parserBuilder().setSigningKey(secret.getBytes()).build();
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return parser.parseClaimsJws(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        return parseToken(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Token expirado
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // Token inválido
            return false;
        }
    }
}