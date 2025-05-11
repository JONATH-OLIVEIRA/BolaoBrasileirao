package br.com.apostas.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.apostas.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        logger.info("📌 JwtFilter ativado! Verificando requisição: {}", request.getServletPath());

        // Rotas públicas
        if (request.getServletPath().equals("/auth/login") || 
            request.getServletPath().startsWith("/css/") ||
            request.getServletPath().startsWith("/js/")) {
            chain.doFilter(request, response);
            return;
        }

        // Obter token de múltiplas fontes (Header ou Query Parameter)
        String token = getTokenFromRequest(request);
        
        if (token == null) {
            logger.warn("⚠️ Token JWT não encontrado em headers ou parâmetros");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
            return;
        }

        try {
            logger.info("🔹 Token JWT encontrado: {}", token);

            if (!jwtTokenService.validarToken(token)) {
                logger.error("❌ Token inválido ou expirado!");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
                return;
            }

            Claims claims = jwtTokenService.extrairClaims(token);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            logger.info("🟢 Token válido! Usuário: {} | Role: {}", email, role);

            UserDetails userDetails = User.withUsername(email)
                    .password("")
                    .roles(role)
                    .build();

            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
            
        } catch (Exception e) {
            logger.error("❌ Erro na autenticação JWT: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro de autenticação");
        }
    }

    // Novo método para extrair token de múltiplas fontes
    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. Verificar cookies PRIMEIRO (mais importante)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    logger.info("🔵 Token encontrado no cookie: " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        
        // 2. Verificar header Authorization (opcional)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        // 3. Verificar parâmetro token (apenas para desenvolvimento)
        String tokenParam = request.getParameter("token");
        if (tokenParam != null && !tokenParam.isBlank()) {
            return tokenParam;
        }
        
        logger.warn("🔴 Nenhum token encontrado em cookies, headers ou parâmetros");
        return null;
    }
}
