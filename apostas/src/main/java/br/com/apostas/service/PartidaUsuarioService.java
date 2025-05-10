package br.com.apostas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.apostas.model.Partida;
import br.com.apostas.model.PartidaUsuario;
import br.com.apostas.model.Usuario;
import br.com.apostas.repository.PartidaUsuarioRepository;

@Service
public class PartidaUsuarioService {

    private final PartidaUsuarioRepository partidaUsuarioRepository;

    public PartidaUsuarioService(PartidaUsuarioRepository partidaUsuarioRepository) {
        this.partidaUsuarioRepository = partidaUsuarioRepository;
    }

    // 🔹 Registrar a participação do usuário em uma partida (apostar)
    public PartidaUsuario cadastrarAposta(PartidaUsuario partidaUsuario) {
        if (partidaUsuario.getPartida().getResultado() != null) {
            throw new IllegalArgumentException("Não é possível apostar em uma partida já finalizada!");
        }
        return partidaUsuarioRepository.save(partidaUsuario);
    }

    // 🔹 Listar todas as apostas feitas por um usuário
    public List<PartidaUsuario> listarApostasPorUsuario(Usuario usuario) {
        return partidaUsuarioRepository.findByUsuario(usuario);
    }

    // 🔹 Buscar uma aposta específica do usuário
    public Optional<PartidaUsuario> buscarApostaPorId(Long id) {
        return partidaUsuarioRepository.findById(id);
    }

    // 🔹 Buscar todas as apostas feitas em uma partida específica
    public List<PartidaUsuario> listarApostasPorPartida(Partida partida) {
        return partidaUsuarioRepository.findByPartida(partida);
    }
}