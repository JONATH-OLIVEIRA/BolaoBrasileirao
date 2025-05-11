package br.com.apostas.service;

import org.springframework.stereotype.Service;

import br.com.apostas.dto.DashboardResumoDTO;
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

		return new DashboardResumoDTO(totalApostas, totalPartidas, totalUsuarios);
	}
}