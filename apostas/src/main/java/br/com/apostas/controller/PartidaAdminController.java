package br.com.apostas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.apostas.model.Partida;
import br.com.apostas.enums.TimesBrasileirao;
import br.com.apostas.service.PartidaService;

@RestController
@RequestMapping("/admin/partidas")
public class PartidaAdminController {

    private final PartidaService partidaService;

    public PartidaAdminController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    // 🔹 Criar uma nova partida (APENAS ADMIN)
    @PostMapping
    public ResponseEntity<Partida> criarPartida(@RequestBody Partida partida) {
        Partida novaPartida = partidaService.cadastrarPartida(partida);
        return ResponseEntity.ok(novaPartida);
    }

    // 🔹 Listar todas as partidas disponíveis
    @GetMapping
    public ResponseEntity<List<Partida>> listarTodas() {
        return ResponseEntity.ok(partidaService.listarTodas());
    }

    // 🔹 Buscar uma partida por ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Partida>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(partidaService.buscarPorId(id));
    }

    // 🔹 Buscar partidas de um time específico
    @GetMapping("/time/{time}")
    public ResponseEntity<List<Partida>> buscarPorTime(@PathVariable TimesBrasileirao time) {
        return ResponseEntity.ok(partidaService.buscarPorTime(time));
    }

    // 🔹 Editar uma partida existente (APENAS ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<Partida> editarPartida(@PathVariable Long id, @RequestBody Partida partidaAtualizada) {
        return ResponseEntity.ok(partidaService.editarPartida(id, partidaAtualizada));
    }

    // 🔹 Finalizar uma partida e definir o resultado (APENAS ADMIN)
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarPartida(@PathVariable Long id, @RequestBody Partida partidaFinalizada) {
        partidaService.finalizarPartida(id, partidaFinalizada);
        return ResponseEntity.ok().build();
    }

    // 🔹 Remover uma partida (APENAS ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPartida(@PathVariable Long id) {
        partidaService.removerPartida(id);
        return ResponseEntity.ok().build();
    }
}