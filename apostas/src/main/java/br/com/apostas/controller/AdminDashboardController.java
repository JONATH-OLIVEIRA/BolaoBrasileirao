package br.com.apostas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.dto.DashboardResumoDTO;
import br.com.apostas.service.AdminDashboardService;

@RestController
@RequestMapping("/admin/dashboard-api")
@PreAuthorize("hasRole('ADMIN')") // 🔒 Apenas administradores podem acessar este controlador
public class AdminDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);
    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    // 🔹 Obter resumo geral do dashboard com tratamento de erro e log
    @GetMapping
    public ResponseEntity<?> getDashboardResumo() {
        try {
            DashboardResumoDTO resumo = adminDashboardService.getResumoDashboard();
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            logger.error("❌ Erro ao carregar resumo do dashboard: ", e);
            return ResponseEntity.status(500).body("❌ Erro ao carregar resumo do dashboard. Verifique os logs.");
        }
    }

    // 🔹 Obter detalhes avançados do dashboard
    @GetMapping("/detalhado")
    public ResponseEntity<?> getDashboardDetalhado() {
        try {
            // Aqui podemos retornar estatísticas mais aprofundadas no futuro
            return ResponseEntity.ok("📊 Dados detalhados do dashboard ainda estão em desenvolvimento!");
        } catch (Exception e) {
            logger.error("❌ Erro ao carregar detalhes do dashboard: ", e);
            return ResponseEntity.status(500).body("❌ Erro ao carregar detalhes do dashboard. Verifique os logs.");
        }
    }
}