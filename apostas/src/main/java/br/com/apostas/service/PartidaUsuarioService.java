package br.com.apostas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.apostas.enums.ResultadoEscolhido;
import br.com.apostas.enums.ResultadoPartida;
import br.com.apostas.model.Partida;
import br.com.apostas.model.PartidaUsuario;
import br.com.apostas.model.Usuario;
import br.com.apostas.repository.PartidaRepository;
import br.com.apostas.repository.PartidaUsuarioRepository;
import br.com.apostas.repository.UsuarioRepository;

@Service
public class PartidaUsuarioService {

	private final UsuarioRepository usuarioRepository;
    private final PartidaRepository partidaRepository;
    private final PartidaUsuarioRepository partidaUsuarioRepository;

    public PartidaUsuarioService(UsuarioRepository usuarioRepository, PartidaRepository partidaRepository, PartidaUsuarioRepository partidaUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.partidaRepository = partidaRepository;
        this.partidaUsuarioRepository = partidaUsuarioRepository;
    }

    // 🔹 Registrar a participação do usuário em uma partida (apostar)
    public PartidaUsuario cadastrarAposta(Long usuarioId, Long partidaId, String escolha) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Usuário não encontrado"));
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Partida não encontrada"));

        System.out.println("⚠️ Verificando status da partida ID: " + partidaId + " | Resultado atual: " + partida.getResultado());

        if (partida.getResultado() != null && !partida.getResultado().equals(ResultadoPartida.PENDENTE)) { 
            throw new IllegalArgumentException("❌ Não é possível apostar em uma partida já finalizada!");
        }


        ResultadoEscolhido resultadoEscolhido;
        try {
            resultadoEscolhido = ResultadoEscolhido.valueOf(escolha.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("❌ Escolha inválida! Use: CASA_VENCE ou FORA_VENCE.");
        }

        PartidaUsuario partidaUsuario = new PartidaUsuario();
        partidaUsuario.setUsuario(usuario);
        partidaUsuario.setPartida(partida);
        partidaUsuario.setResultadoEscolhido(resultadoEscolhido);

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