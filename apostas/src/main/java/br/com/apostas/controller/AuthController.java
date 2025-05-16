package br.com.apostas.controller;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String email,
            @RequestParam String senha,
            HttpServletResponse response) {

        try {
            // Validação dos parâmetros
            if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
                logger.warn("Tentativa de login com credenciais vazias");
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "Email e senha são obrigatórios");
            }

            logger.info("Tentativa de login para: {}", email);

            Map<String, String> userInfo = usuarioService.autenticarUsuario(email, senha);
            
            // Verificação mais robusta dos dados retornados
            if (userInfo == null || 
                userInfo.get("token") == null || 
                userInfo.get("role") == null || 
                userInfo.get("id") == null) {
                logger.error("Dados de autenticação incompletos para: {}", email);
                return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro no processo de autenticação");
            }

            String token = userInfo.get("token");
            String role = userInfo.get("role");
            String userId = userInfo.get("id");

            // Verificação de role válido
            if (!isValidRole(role)) {
                logger.error("Tipo de usuário inválido: {}", role);
                return buildErrorResponse(HttpStatus.FORBIDDEN, "Tipo de usuário não permitido");
            }

            // Configuração do cookie seguro
            configureAuthCookie(response, token);

            // Redirecionamento baseado no role
            String redirectUrl = determineRedirectUrl(role);
            logger.info("Login bem-sucedido para: {}. Redirecionando para: {}", email, redirectUrl);

            return ResponseEntity.ok(buildSuccessResponse(redirectUrl, token, userId));

        } catch (SecurityException e) {
            logger.warn("Tentativa de login não autorizada: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado durante login para: {}", email, e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor");
        }
    }

    private boolean isValidRole(String role) {
        return "ADMIN".equalsIgnoreCase(role) || "USER".equalsIgnoreCase(role);
    }

    private String determineRedirectUrl(String role) {
        return switch (role.toUpperCase()) {
            case "ADMIN" -> "/admin/dashboard";
            case "USER" -> "/user/dashboard";
            default -> throw new IllegalStateException("Tipo de usuário não suportado: " + role);
        };
    }

    private void configureAuthCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // SEMPRE true em produção
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias
        response.addCookie(cookie);
    }

    private Map<String, Object> buildSuccessResponse(String redirectUrl, String token, String userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("redirect", redirectUrl);
        response.put("token", token);
        response.put("userId", userId);
        return response;
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return ResponseEntity.status(status).body(response);
    }
}