package cl.perfulandia.catalog.config;

import cl.perfulandia.catalog.util.JwtUtil;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    public SecurityConfig(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String jwtCookie = "token";
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, jwtCookie);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/products/").permitAll()
                        .requestMatchers(HttpMethod.GET,"/products/**").permitAll()

                        .requestMatchers(HttpMethod.POST,"/products").hasRole("ADMIN")
                        .requestMatchers("/products/**").hasRole("ADMIN")
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

