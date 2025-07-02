package cl.perfulandia.authentication.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil util;

    @BeforeEach
    void setUp() {
        util = new JwtUtil();
        ReflectionTestUtils.setField(util, "secret", "secretkey");
        ReflectionTestUtils.setField(util, "expirationMs", 3600000L);
        util.init();
    }

    @Test
    void generateAndValidateToken() {
        String token = util.generateToken("user","ADMIN");
        assertTrue(util.validateToken(token));
        Claims claims = util.getAllClaimsFromToken(token);
        assertEquals("user", claims.getSubject());
        assertEquals("ADMIN", claims.get("role", String.class));
    }

    @Test
    void invalidTokenShouldFailValidation() {
        String token = util.generateToken("user","ADMIN");
        assertFalse(util.validateToken(token + "broken"));
    }

    @Test
    void expiredTokenShouldBeInvalid() {
        ReflectionTestUtils.setField(util, "expirationMs", -1L);
        String expired = util.generateToken("user","ADMIN");
        assertFalse(util.validateToken(expired));
    }
}
