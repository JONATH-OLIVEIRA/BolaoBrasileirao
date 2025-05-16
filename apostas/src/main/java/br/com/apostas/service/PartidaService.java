package br.com.apostas.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.apostas.enums.ResultadoPartida;
import br.com.apostas.model.Aposta;
import br.com.apostas.model.Partida;
import br.com.apostas.repository.ApostaRepository;
import br.com.apostas.repository.PartidaRepository;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final ApostaRepository apostaRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(PartidaService.class);

    public PartidaService(ApostaRepository apostaRepository,PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
        this.apostaRepository = apostaRepository;
    }

    // 🔹 Criar uma nova partida (APENAS ADMIN)
    public Partida cadastrarPartida(Partida partida) {
        if (partida.getDataJogo().getDayOfWeek().getValue() < 6) {
            throw new IllegalArgumentException("❌ Só permitimos rodadas do final de semana!");
        }

        if (partida.getRodada() == null || partida.getRodada() < 1) {
            throw new IllegalArgumentException("❌ A rodada da partida deve ser informada corretamente!");
        }

        return partidaRepository.save(partida);
    }

    // 🔹 Listar todas as partidas disponíveis
    public List<Partida> listarTodas() {
        return partidaRepository.findAll();
    }

    // 🔹 Buscar partidas ativas (sem resultado definido)
    public List<Partida> buscarPartidasAtivas() {
        return partidaRepository.findByResultadoIsNull();
    }

    // 🔹 Buscar uma partida por ID
    public Partida buscarPorId(Long id) {
        return partidaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("❌ Partida não encontrada! ID: " + id));
    }

    // 🔹 Editar informações de uma partida existente
    public Partida editarPartida(Long id, Partida partidaAtualizada) {
        Partida partida = buscarPorId(id);

        if (partida.getDataJogo().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("❌ Não é possível editar uma partida já realizada!");
        }

        partida.setTimeCasa(partidaAtualizada.getTimeCasa());
        partida.setTimeFora(partidaAtualizada.getTimeFora());
        partida.setDataJogo(partidaAtualizada.getDataJogo());
        partida.setRodada(partidaAtualizada.getRodada()); // Agora rodada pode ser atualizada

        return partidaRepository.save(partida);
    }

    // 🔹 Finalizar uma partida e definir o resultado (APENAS ADMIN)
    public void finalizarPartida(Long id, ResultadoPartida resultado) {
        Partida partida = buscarPorId(id);

        if (partida.getResultado() != ResultadoPartida.PENDENTE) {
            throw new IllegalArgumentException("❌ Partida já finalizada! Não pode ser alterada.");
        }

        partida.setResultado(resultado);
        partidaRepository.save(partida);

        // 🔹 Atualizar pontuação das apostas relacionadas à partida
        List<Aposta> apostas = apostaRepository.findByPartida(partida);
        System.out.println("🔹 Apostas encontradas para partida ID " + id + ": " + apostas);
        for (Aposta aposta : apostas) {
            aposta.calcularPontuacao(); // 🏆 Aplica a pontuação
            apostaRepository.save(aposta); // 🔹 Atualiza no banco
        }

        System.out.println("✅ Pontuações calculadas para todas as apostas da partida " + id);
    }
    
    // 🔹 Remover uma partida (APENAS ADMIN)
    public void removerPartida(Long id) {
        Partida partida = buscarPorId(id);

        if (partida.getResultado() != ResultadoPartida.PENDENTE) {
            throw new IllegalArgumentException("❌ Partida já finalizada! Não pode ser removida.");
        }

        partidaRepository.deleteById(id);
        logger.warn("❌ Partida removida! ID: {}", id);
    }

    // 🔹 Atualizar o resultado de uma partida (APENAS ADMIN)
    public void atualizarResultado(Long id, String resultado) {
        Partida partida = buscarPorId(id);

        if (partida.getResultado() != ResultadoPartida.PENDENTE) {
            throw new IllegalArgumentException("❌ Partida já finalizada! Não pode ser alterada.");
        }

        ResultadoPartida resultadoFinal;
        try {
            resultadoFinal = ResultadoPartida.valueOf(resultado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("❌ Resultado inválido! Use CASA, FORA ou EMPATE.");
        }

        partida.setResultado(resultadoFinal);
        partidaRepository.save(partida);
    }
    public void corrigirResultado(Long id, String novoResultado, String justificativa) {
        Partida partida = buscarPorId(id);

        if (partida.getResultado() == ResultadoPartida.PENDENTE) {
            throw new IllegalArgumentException("❌ A partida ainda não foi finalizada! Use a função de encerramento normal.");
        }

        if (justificativa == null || justificativa.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ É obrigatório fornecer uma justificativa para a correção.");
        }

        // Converte a String para um valor válido da enum ResultadoPartida
        ResultadoPartida resultadoCorrigido;
        try {
            resultadoCorrigido = ResultadoPartida.valueOf(novoResultado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("❌ Resultado inválido! Use CASA, FORA ou EMPATE.");
        }

        // Salva o novo resultado e registra a correção
        partida.setResultado(resultadoCorrigido);
        logger.warn("⚠ Correção de resultado! Partida ID: {}, Novo Resultado: {}, Justificativa: {}", id, novoResultado, justificativa);

        partidaRepository.save(partida);
    }
}