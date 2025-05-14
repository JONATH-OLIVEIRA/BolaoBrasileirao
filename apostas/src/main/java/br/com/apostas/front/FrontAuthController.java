package br.com.apostas.front;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.apostas.dto.DashboardResumoDTO;
import br.com.apostas.enums.TimesBrasileirao;
import br.com.apostas.model.Partida;
import br.com.apostas.model.Usuario;
import br.com.apostas.service.AdminDashboardService;
import br.com.apostas.service.PartidaService;
import br.com.apostas.service.PartidaUsuarioService;

@Controller
public class FrontAuthController {

    private final AdminDashboardService adminDashboardService;
    private final PartidaService partidaService;
    private final PartidaUsuarioService partidaUsuarioService;
    
    public FrontAuthController(AdminDashboardService adminDashboardService, PartidaService partidaService, PartidaUsuarioService partidaUsuarioService) {
        this.adminDashboardService = adminDashboardService;
        this.partidaService = partidaService;
        this.partidaUsuarioService = partidaUsuarioService;
    }
    
    @GetMapping("/user/apostar")
    public String apostar(Model model, @AuthenticationPrincipal Usuario usuario) {
        model.addAttribute("usuario", usuario); // 🔹 Passa os dados do usuário autenticado
        model.addAttribute("partidas", partidaService.listarTodas()); // 🔹 Carrega as partidas cadastradas no banco
        return "aposta"; // 🔹 Retorna a página de apostas
    }   
   

    @GetMapping("/") // Página inicial
    public String homePage() {
        return "index"; // Carrega home.html
    }
    
    @GetMapping("/user/dashboard") // Página do usuário
    public String userDashboard(Model model) {
        return "user-dashboard"; // Carrega o arquivo user-dashboard.html
    }
    
    @GetMapping("/auth/cadastro") // Página de cadastro de usuário
    public String registerPage() {
        return "cadastro"; // Carrega register.html
    }

    @GetMapping("/auth/login") // Página de login
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin/dashboard") // Dashboard principal
    public String dashboard(Model model) {
        DashboardResumoDTO resumo = adminDashboardService.getResumoDashboard();
        List<Partida> partidas = partidaService.listarTodas();

        model.addAttribute("dashboard", resumo);
        model.addAttribute("partidas", partidas);
        model.addAttribute("timesBrasileirao", TimesBrasileirao.values());

        return "dashboard";
    }

    @GetMapping("/admin/resumo") // Resumo separado
    public String resumoDashboard(Model model) {
        model.addAttribute("dashboard", adminDashboardService.getResumoDashboard());
        return "dashboard-summary";
    }

    @GetMapping("/admin/partidas") // Página de gerenciamento de partidas
    public String gerenciarPartidas(Model model) {
        model.addAttribute("partidas", partidaService.listarTodas());
        model.addAttribute("timesBrasileirao", TimesBrasileirao.values());
        return "partidas";
    }

    @GetMapping("/admin/usuarios") // Página de gerenciamento de usuários
    public String gerenciarUsuarios(Model model) {
        return "usuarios";
    }
}