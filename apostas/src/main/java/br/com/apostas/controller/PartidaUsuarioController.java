package br.com.apostas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.model.PartidaUsuario;
import br.com.apostas.model.Usuario;
import br.com.apostas.service.PartidaUsuarioService;

@RestController
@RequestMapping("/usuario/partidas")
public class PartidaUsuarioController {

    private final PartidaUsuarioService partidaUsuarioService;

    public PartidaUsuarioController(PartidaUsuarioService partidaUsuarioService) {
        this.partidaUsuarioService = partidaUsuarioService;
    }

    // 🔹 Registrar a participação do usuário em uma partida
    @PostMapping
    public ResponseEntity<PartidaUsuario> criarAposta(@RequestBody PartidaUsuario partidaUsuario) {
        PartidaUsuario novaAposta = partidaUsuarioService.cadastrarAposta(partidaUsuario);
        return ResponseEntity.ok(novaAposta);
    }

    // 🔹 Listar todas as apostas feitas pelo usuário
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<PartidaUsuario>> listarApostasPorUsuario(@PathVariable Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        return ResponseEntity.ok(partidaUsuarioService.listarApostasPorUsuario(usuario));
    }

    // 🔹 Buscar uma aposta específica do usuário
    @GetMapping("/{id}")
    public ResponseEntity<Optional<PartidaUsuario>> buscarApostaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(partidaUsuarioService.buscarApostaPorId(id));
    }
}