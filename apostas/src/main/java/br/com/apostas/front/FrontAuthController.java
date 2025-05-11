package br.com.apostas.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.apostas.dto.DashboardResumoDTO;
import br.com.apostas.service.AdminDashboardService;

@Controller
public class FrontAuthController {

    private final AdminDashboardService adminDashboardService;

    // 🔹 Injeção do serviço pelo construtor
    public FrontAuthController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard-summary")
    public String dashboard(Model model) {
        DashboardResumoDTO resumo = adminDashboardService.getResumoDashboard();
        model.addAttribute("dashboard", resumo);
        return "dashboard";
    }
}
