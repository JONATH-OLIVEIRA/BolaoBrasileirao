package br.com.apostas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.apostas.dto.DashboardResumoDTO;
import br.com.apostas.service.AdminDashboardService;

@RestController
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')") // 🔒 Apenas administradores podem acessar este controlador
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    // 🔹 Obter resumo geral do dashboard
    @GetMapping
    public ResponseEntity<DashboardResumoDTO> getDashboardResumo() {
        return ResponseEntity.ok(adminDashboardService.getResumoDashboard());
    }
}