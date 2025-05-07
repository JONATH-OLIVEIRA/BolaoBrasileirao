package br.com.apostas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 🔹 Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        boolean autenticado = usuarioService.autenticarUsuario(email, senha);
        if (!autenticado) {
            return ResponseEntity.status(401).body("Credenciais inválidas!");
        }

        // Futuro: gerar e retornar um token JWT para acesso protegido
        return ResponseEntity.ok("Login realizado com sucesso!");
    }
}