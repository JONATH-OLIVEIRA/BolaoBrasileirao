package br.com.apostas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.apostas.enums.ResultadoPartida;
import br.com.apostas.enums.StatusAposta;
import br.com.apostas.model.Aposta;
import br.com.apostas.model.Partida;
import br.com.apostas.model.Usuario;
import br.com.apostas.repository.ApostaRepository;

@Service
public class ApostaService {

    private final ApostaRepository apostaRepository;

    public ApostaService(ApostaRepository apostaRepository) {
        this.apostaRepository = apostaRepository;
    }

    // 🔹 Registrar nova aposta
    public Aposta registrarAposta(Aposta aposta) {
        if (aposta.getPartida().getResultado() != ResultadoPartida.PENDENTE) {
            throw new IllegalArgumentException("❌ Não é possível apostar em uma partida já finalizada!");
        }

        return apostaRepository.save(aposta);
    }


    // 🔹 Buscar todas as apostas de um usuário
    public List<Aposta> listarApostasPorUsuario(Usuario usuario) {
        return apostaRepository.findByUsuarioId(usuario.getId());
    }

    // 🔹 Buscar todas as apostas feitas em uma partida específica
    public List<Aposta> listarApostasPorPartida(Partida partida) {
        return apostaRepository.findByPartidaId(partida.getId());
    }

    // 🔹 Buscar apostas por status
    public List<Aposta> listarApostasPorStatus(String status) {
        return apostaRepository.findByStatus(StatusAposta.valueOf(status));
    }

    // 🔹 Buscar uma aposta específica
    public Optional<Aposta> buscarApostaPorId(Long id) {
        return apostaRepository.findById(id);
    }
    // Busca todas as apostas
    public List<Aposta> listarTodasApostas() {
        return apostaRepository.findAll();
    }

}