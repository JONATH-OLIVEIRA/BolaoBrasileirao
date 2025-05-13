package br.com.apostas.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.dto.CorreçãoResultadoDTO;
import br.com.apostas.enums.ResultadoPartida;
import br.com.apostas.model.Partida;
import br.com.apostas.service.PartidaService;

@RestController
@RequestMapping("/admin/partidas-api")
@PreAuthorize("hasRole('ADMIN')")
public class PartidaAdminController {

    private final PartidaService partidaService;

    public PartidaAdminController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }
  
    // 🔹 Listar todas as partidas disponíveis
    @GetMapping
    public ResponseEntity<List<Partida>> listarTodas() {
        return ResponseEntity.ok(partidaService.listarTodas());
    }

    // 🔹 Listar partidas ativas (sem resultado definido)
    @GetMapping("/ativas")
    public ResponseEntity<List<Partida>> listarAtivas() {
        return ResponseEntity.ok(partidaService.buscarPartidasAtivas());
    }

    // 🔹 Buscar uma partida por ID
    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(partidaService.buscarPorId(id));
    }

    // 🔹 Editar uma partida existente (APENAS ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<Partida> editarPartida(@PathVariable Long id, @RequestBody Partida partidaAtualizada) {
        return ResponseEntity.ok(partidaService.editarPartida(id, partidaAtualizada));
    }

    // 🔹 Finalizar uma partida e definir o resultado (APENAS ADMIN)
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarPartida(@PathVariable Long id, @RequestBody ResultadoPartida resultado) {
        partidaService.finalizarPartida(id, resultado);
        return ResponseEntity.ok().build();
    }


    // 🔹 Remover uma partida (APENAS ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPartida(@PathVariable Long id) {
        partidaService.removerPartida(id);
        return ResponseEntity.ok().build();
    }
    
    // 🔹 Criar uma nova partida (APENAS ADMIN)
    @PostMapping
    public ResponseEntity<Partida> criarPartida(@RequestBody Partida partida) {
        if (partida.getRodada() == null) {
            partida.setRodada(definirRodada(partida.getDataJogo())); // Define rodada automaticamente
        }
        if (partida.getResultado() == null) {
            partida.setResultado(ResultadoPartida.PENDENTE); // Define como PENDENTE
        }
        return ResponseEntity.ok(partidaService.cadastrarPartida(partida));
    }

    private Integer definirRodada(LocalDate dataJogo) {
        DayOfWeek diaSemana = dataJogo.getDayOfWeek();
        if (diaSemana == DayOfWeek.SATURDAY) return 1; // Rodada de sábado
        if (diaSemana == DayOfWeek.SUNDAY) return 2; // Rodada de domingo
        throw new IllegalArgumentException("❌ Apenas partidas de final de semana são permitidas!");
    }
    @PutMapping("/{id}/corrigir-resultado")
    public ResponseEntity<Void> corrigirResultado(@PathVariable Long id, @RequestBody CorreçãoResultadoDTO dados) {
        partidaService.corrigirResultado(id, dados.getNovoResultado(), dados.getJustificativa());
        return ResponseEntity.ok().build();
    }
}