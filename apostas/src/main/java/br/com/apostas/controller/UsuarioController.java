package br.com.apostas.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.enums.TipoUsuario;
import br.com.apostas.model.Usuario;
import br.com.apostas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 🔹 Cadastro de usuário
    @PostMapping
    @Operation(summary = "Criar um novo usuário", description = "Registra um novo usuário no sistema com perfil de apostador.")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        usuario.setTipo(TipoUsuario.USUARIO);
        Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // 🔹 Busca usuário por e-mail
    @GetMapping("/{email}")
    @Operation(summary = "Buscar usuário por e-mail", description = "Retorna os detalhes do usuário pelo endereço de e-mail fornecido.")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Exclusão lógica da conta (tornar usuário inativo)
    @PutMapping("/{id}/desativar")
    @Operation(summary = "Desativar usuário", description = "Marca o usuário como inativo no sistema, sem excluir seus dados permanentemente.")
    public ResponseEntity<?> desativarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.desativarConta();
            usuarioService.atualizarUsuario(usuario);
            return ResponseEntity.ok("Usuário desativado com sucesso!");
        }
        return ResponseEntity.notFound().build();
    }

}