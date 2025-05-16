package br.com.apostas.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.apostas.dto.DashboardResumoDTO;
import br.com.apostas.enums.StatusAposta;
import br.com.apostas.repository.ApostaRepository;
import br.com.apostas.repository.PartidaRepository;
import br.com.apostas.repository.UsuarioRepository;

@Service
public class AdminDashboardService {

    private final ApostaRepository apostaRepository;
    private final PartidaRepository partidaRepository;
    private final UsuarioRepository usuarioRepository;

    public AdminDashboardService(ApostaRepository apostaRepository, PartidaRepository partidaRepository,
                                 UsuarioRepository usuarioRepository) {
        this.apostaRepository = apostaRepository;
        this.partidaRepository = partidaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // 🔹 Método para buscar o resumo do dashboard
    public DashboardResumoDTO getResumoDashboard() {
        long totalApostas = apostaRepository.count();
        long totalPartidas = partidaRepository.count();
        long totalUsuarios = usuarioRepository.count();

        // 🔹 Buscar a quantidade de apostas por status garantindo que não retornem valores nulos
        long apostasConfirmadas = Optional.ofNullable(apostaRepository.countByStatus(StatusAposta.CONFIRMADA)).orElse(0L);
        long apostasPendentes = Optional.ofNullable(apostaRepository.countByStatus(StatusAposta.PENDENTE)).orElse(0L);
        long apostasCanceladas = Optional.ofNullable(apostaRepository.countByStatus(StatusAposta.CANCELADA)).orElse(0L);

        // 🔹 Buscar usuários ativos e inativos com validação
        long usuariosAtivos = Optional.ofNullable(usuarioRepository.countByAtivoTrue()).orElse(0L);
        long usuariosInativos = Optional.ofNullable(usuarioRepository.countByAtivoFalse()).orElse(0L);

        return new DashboardResumoDTO(
                totalApostas, totalPartidas, totalUsuarios,
                apostasConfirmadas, apostasPendentes, apostasCanceladas,
                usuariosAtivos, usuariosInativos
        );
    }
}