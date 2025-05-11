package cl.perfulandia.authentication.config;

import cl.perfulandia.authentication.config.JwtAuthenticationFilter;
import cl.perfulandia.authentication.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Nombre de la cookie donde se almacenará el JWT
        String jwtCookieName = "JWT_TOKEN";

        // Filtro que valida JWT tanto en header Authorization como en cookie
        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtUtil, jwtCookieName);

        http
                // Deshabilitar CSRF porque es API stateless
                .csrf(csrf -> csrf.disable())
                // No crear sesión, cada petición va con su propio token
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Definir accesos públicos y protegidos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/ping").permitAll()
                        .requestMatchers("/ping").permitAll()
                        .anyRequest().authenticated()
                )
                // Insertar nuestro filtro antes de procesar autenticación por formulario
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Para poder inyectar AuthenticationManager si lo necesitas
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * BCrypt para encriptar contraseñas en la base de datos
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
