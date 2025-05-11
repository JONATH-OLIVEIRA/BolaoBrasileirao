package br.com.apostas.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse; // Importe esta classe

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UsuarioService usuarioService;

	@Autowired
	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha,
			HttpServletResponse response) {
		try {
			String token = usuarioService.autenticarUsuario(email, senha);

			// Configura o cookie de forma segura
			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false); // true em produção com HTTPS
			cookie.setPath("/");
			cookie.setMaxAge(7 * 24 * 60 * 60); // 1 semana
			response.addCookie(cookie);

			return ResponseEntity.ok(Map.of("redirect", "/dashboard-summary"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}