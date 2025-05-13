package br.com.apostas.front;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.apostas.dto.DashboardResumoDTO;
import br.com.apostas.enums.TimesBrasileirao;
import br.com.apostas.model.Partida;
import br.com.apostas.service.AdminDashboardService;
import br.com.apostas.service.PartidaService;

@Controller
public class FrontAuthController {

    private final AdminDashboardService adminDashboardService;
    private final PartidaService partidaService;


    // 🔹 Injeção do serviço pelo construtor
    public FrontAuthController(AdminDashboardService adminDashboardService, PartidaService partidaService) {
        this.adminDashboardService = adminDashboardService;
        this.partidaService = partidaService;
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard-summary")
    public String dashboard(Model model) {
        DashboardResumoDTO resumo = adminDashboardService.getResumoDashboard();
        List<Partida> partidas = partidaService.listarTodas();
        model.addAttribute("dashboard", resumo);
        model.addAttribute("partidas", partidas);
        model.addAttribute("timesBrasileirao", TimesBrasileirao.values());
        return "dashboard";
    }
}
