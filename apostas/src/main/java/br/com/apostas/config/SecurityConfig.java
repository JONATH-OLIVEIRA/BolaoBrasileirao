package br.com.apostas.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.apostas.security.JwtAuthenticationEntryPoint;
import br.com.apostas.security.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtFilter jwtFilter, 
                        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(auth -> auth
                // 🔹 Permitir acesso público ao login e registro de usuários
                .requestMatchers("/", "/index", "/auth/login", "/auth/cadastro", "/favicon.ico", "/v3/api-docs/**", "/swagger-ui/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
              
                // 🔹 Proteção do dashboard do administrador
                .requestMatchers("/admin/dashboard").hasRole("ADMIN")
                .requestMatchers("/admin/resumo").hasRole("ADMIN")
                .requestMatchers("/admin/partidas").hasRole("ADMIN")
                .requestMatchers("/admin/usuarios").hasRole("ADMIN")

                // 🔹 Permitir acesso ao dashboard do usuário
                .requestMatchers("/user/dashboard").hasRole("USER") 
                .requestMatchers("/usuario/partidas-api/**").hasRole("USER") // 🔹 API de apostas
                
                // 🔹 Qualquer outra requisição exige autenticação
                .anyRequest().authenticated()) 

            // 🔹 Adiciona filtro JWT antes do processo de autenticação padrão do Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}