package br.com.apostas.controller;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String senha,
			HttpServletResponse response) {

		try {
			// Validação básica dos parâmetros
			if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
				return buildErrorResponse(HttpStatus.BAD_REQUEST, "Email e senha são obrigatórios");
			}

			logger.info("Tentativa de login para o email: {}", email);

			Map<String, String> userInfo = usuarioService.autenticarUsuario(email, senha);
			String token = userInfo.get("token");
			String role = userInfo.get("role");
			String userId = userInfo.get("id");

			if (token == null || role == null || userId == null) {
				logger.error("Dados de autenticação incompletos para o usuário: {}", email);
				return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Dados de autenticação incompletos");
			}

			// Verificação de role válido
			if (!"ADMIN".equals(role) && !"USER".equals(role)) {
				logger.error("Tipo de usuário inválido: {}", role);
				return buildErrorResponse(HttpStatus.FORBIDDEN, "Tipo de usuário inválido");
			}

			// Configuração do cookie seguro
			configureAuthCookie(response, token);

			String redirectUrl = switch (role.toUpperCase()) {
			case "ADMIN" -> "/admin/dashboard";
			case "USER" -> "/user/dashboard";
			default -> throw new RuntimeException("Tipo de usuário não suportado");

			};
			logger.info("Login bem-sucedido para o usuário: {}. Redirecionando para: {}", email, redirectUrl);

			return ResponseEntity.ok(buildSuccessResponse(redirectUrl, token, userId));

		} catch (RuntimeException e) {
			logger.error("Erro durante o login para o email: {}", email, e);
			return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	private void configureAuthCookie(HttpServletResponse response, String token) {
		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // Em produção, deve ser true (HTTPS)
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