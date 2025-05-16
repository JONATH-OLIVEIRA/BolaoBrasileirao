package br.com.apostas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.enums.TipoUsuario;
import br.com.apostas.model.Usuario;
import br.com.apostas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/usuarios-api")
@Tag(name = "Admin - Usuários", description = "Gerenciamento de usuários pelo administrador")
@PreAuthorize("hasRole('ADMIN')") // 🔹 Apenas administradores podem acessar este controlador
public class AdminController {

	private final UsuarioService usuarioService;

	public AdminController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	// 🔹 Listar todos os usuários
	@GetMapping
	@Operation(summary = "Listar usuários", description = "Retorna todos os usuários cadastrados no sistema.")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		return ResponseEntity.ok(usuarioService.listarTodos());
	}

	// 🔹 Ativar um usuário desativado
	@PutMapping("/{id}/ativar")
	@Operation(summary = "Ativar usuário", description = "Reativa um usuário que foi desativado anteriormente.")
	public ResponseEntity<?> ativarUsuario(@PathVariable Long id) {
		return usuarioService.buscarPorId(id).map(usuario -> {
			
			usuario.restaurarConta();
			
			usuarioService.atualizarUsuario(usuario);
			
			SecurityContextHolder.clearContext();
			
			return ResponseEntity.ok("Usuário ativado com sucesso!");
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 Desativa um usuário desativado
	@PutMapping("/{id}/desativar")
	@Operation(summary = "Desativar usuário", description = "Desativa um usuário do sistema.")
	public ResponseEntity<?> desativarUsuario(@PathVariable Long id) {
		return usuarioService.buscarPorId(id).map(usuario -> {
			usuario.desativarConta();
			usuarioService.atualizarUsuario(usuario);
			return ResponseEntity.ok("Usuário desativado com sucesso!");
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 Alterar perfil de usuário (Admin ou Usuário)
	@PutMapping("/{id}/role")
	@Operation(summary = "Alterar papel do usuário", description = "Muda a permissão do usuário para ADMIN ou USUARIO.")
	public ResponseEntity<?> alterarPermissaoUsuario(@PathVariable Long id, @RequestParam TipoUsuario novoTipo) {
		return usuarioService.buscarPorId(id).map(usuario -> {
			usuario.setTipo(novoTipo);
			usuarioService.atualizarUsuario(usuario);
			return ResponseEntity.ok("Permissão do usuário alterada para " + novoTipo.name());
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
