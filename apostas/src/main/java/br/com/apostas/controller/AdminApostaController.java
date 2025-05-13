package br.com.apostas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.model.Aposta;
import br.com.apostas.service.ApostaService;

@RestController
@RequestMapping("/admin/apostas")
@PreAuthorize("hasRole('ADMIN')") // 🔒 Protege a rota para apenas administradores
public class AdminApostaController {

    private final ApostaService apostaService;

    public AdminApostaController(ApostaService apostaService) {
        this.apostaService = apostaService;
    }

    // 🔹 Cadastrar nova aposta (Apenas o administrador pode criar apostas)
    @PostMapping
    public ResponseEntity<Aposta> criarAposta(@RequestBody Aposta aposta) {
        Aposta novaAposta = apostaService.registrarAposta(aposta);
        return ResponseEntity.ok(novaAposta);
    }

    // 🔹 Listar todas as apostas do sistema
    @GetMapping
    public ResponseEntity<List<Aposta>> listarTodasApostas() {
        return ResponseEntity.ok(apostaService.listarTodasApostas());
    }

    // 🔹 Buscar uma aposta específica
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Aposta>> buscarApostaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(apostaService.buscarApostaPorId(id));
    }

    // 🔹 Filtrar apostas por status (PENDENTE, CONFIRMADA, CANCELADA)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Aposta>> listarApostasPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(apostaService.listarApostasPorStatus(status));
    }
    
}