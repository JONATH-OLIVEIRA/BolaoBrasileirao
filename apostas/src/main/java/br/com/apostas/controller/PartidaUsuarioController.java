package br.com.apostas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.model.PartidaUsuario;
import br.com.apostas.model.Usuario;
import br.com.apostas.repository.UsuarioRepository;
import br.com.apostas.service.PartidaUsuarioService;

@RestController
@RequestMapping("/usuario/partidas-api")
public class PartidaUsuarioController {

    private final PartidaUsuarioService partidaUsuarioService;
    private final UsuarioRepository usuarioRepository;

    public PartidaUsuarioController(PartidaUsuarioService partidaUsuarioService, UsuarioRepository usuarioRepository) {
        this.partidaUsuarioService = partidaUsuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    // 🔹 Registrar a participação do usuário em uma partida
    @PostMapping("/apostar")
    public ResponseEntity<?> criarApostas(@RequestBody List<PartidaUsuario> apostas) { 
        try {
            for (PartidaUsuario aposta : apostas) {
                if (aposta.getUsuario() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Erro: O usuário não foi incluído na requisição.");
                }
                System.out.println("Recebida aposta de usuário ID: " + aposta.getUsuario().getId()); // 🔹 Log para confirmar o usuário

                partidaUsuarioService.cadastrarAposta(aposta.getUsuario().getId(), aposta.getPartida().getId(), aposta.getResultadoEscolhido().name());
            }
            return ResponseEntity.ok("✅ Apostas registradas com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Erro ao cadastrar aposta: " + e.getMessage());
        }
    }

    // 🔹 Listar todas as apostas feitas pelo usuário
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<PartidaUsuario>> listarApostasPorUsuario(@PathVariable Long id) {
    	Usuario usuario = usuarioRepository.findById(id)
    	        .orElseThrow(() -> new IllegalArgumentException("❌ Usuário não encontrado"));
    	return ResponseEntity.ok(partidaUsuarioService.listarApostasPorUsuario(usuario));
    }

    // 🔹 Buscar uma aposta específica do usuário
    @GetMapping("/{id}")
    public ResponseEntity<Optional<PartidaUsuario>> buscarApostaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(partidaUsuarioService.buscarApostaPorId(id));
    }
}