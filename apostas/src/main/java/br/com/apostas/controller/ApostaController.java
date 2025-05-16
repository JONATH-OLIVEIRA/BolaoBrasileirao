package br.com.apostas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.model.Aposta;
import br.com.apostas.model.Partida;
import br.com.apostas.model.Usuario;
import br.com.apostas.service.ApostaService;
import br.com.apostas.service.PartidaService;


@RestController
@RequestMapping("/apostas")
@PreAuthorize("hasRole('USER')")
public class ApostaController {

	private final ApostaService apostaService;
	private final PartidaService partidaService;

	public ApostaController(PartidaService partidaService, ApostaService apostaService) {
		this.apostaService = apostaService;
		this.partidaService = partidaService;
	}

	// 🔹 Registrar uma nova aposta
	@PostMapping("/registrar")
	public ResponseEntity<Aposta> fazerAposta(@RequestBody Aposta aposta) {
		if (aposta.getUsuario() == null || aposta.getPartida() == null) {
			return ResponseEntity.badRequest().body(null); // 🔹 Retorna erro se usuário ou partida forem nulos
		}
		return ResponseEntity.ok(apostaService.registrarAposta(aposta));
	}

	// 🔹 Listar apostas por usuário
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Aposta>> listarApostasPorUsuario(@PathVariable Long usuarioId) {
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId); // 🔹 Criando um objeto de usuário com ID
		return ResponseEntity.ok(apostaService.listarApostasPorUsuario(usuario));
	}

	// 🔹 Listar apostas por partida
	@GetMapping("/partida/{partidaId}")
	public ResponseEntity<List<Aposta>> listarApostasPorPartida(@PathVariable Long partidaId) {
		Partida partida = partidaService.buscarPorId(partidaId);
		if (partida == null) {
			return ResponseEntity.notFound().build(); // 🔹 Retorna erro se a partida não existir
		}
		return ResponseEntity.ok(apostaService.listarApostasPorPartida(partida));
	}

	// 🔹 Listar todas as apostas
	@GetMapping("/todas")
	public ResponseEntity<List<Aposta>> listarTodasApostas() {
		return ResponseEntity.ok(apostaService.listarTodasApostas());
	}

	// 🔹 Buscar uma aposta específica pelo ID
	@GetMapping("/{id}")
	public ResponseEntity<Aposta> buscarApostaPorId(@PathVariable Long id) {
		return apostaService.buscarApostaPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}