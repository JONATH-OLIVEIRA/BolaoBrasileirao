package br.com.apostas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.apostas.model.Partida;
import br.com.apostas.enums.TimesBrasileirao;
import br.com.apostas.repository.PartidaRepository;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;

    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    // 🔹 Criar uma nova partida (APENAS ADMIN)
    public Partida cadastrarPartida(Partida partida) {
        if (partida.getTimeCasa().equals(partida.getTimeFora())) {
            throw new IllegalArgumentException("Os times da partida devem ser diferentes!");
        }
        return partidaRepository.save(partida);
    }

    // 🔹 Listar todas as partidas disponíveis
    public List<Partida> listarTodas() {
        return partidaRepository.findAll();
    }

    // 🔹 Buscar uma partida por ID
    public Optional<Partida> buscarPorId(Long id) {
        return partidaRepository.findById(id);
    }

    // 🔹 Buscar a última partida ativa para apostas
    public Optional<Partida> buscarPartidaAtual() {
        return partidaRepository.findTopByResultadoIsNullOrderByIdDesc();
    }

    // 🔹 Buscar todas as partidas já finalizadas
    public List<Partida> listarFinalizadas() {
        return partidaRepository.findByResultadoIsNotNull();
    }

    // 🔹 Buscar partidas de um time específico
    public List<Partida> buscarPorTime(TimesBrasileirao time) {
        return partidaRepository.findByTimeCasaOrTimeFora(time, time);
    }

    // 🔹 Atualizar informações de uma partida existente
    public Partida editarPartida(Long id, Partida partidaAtualizada) {
        return partidaRepository.findById(id).map(partida -> {
            if (partidaAtualizada.getTimeCasa().equals(partidaAtualizada.getTimeFora())) {
                throw new IllegalArgumentException("Os times da partida devem ser diferentes!");
            }
            partida.setTimeCasa(partidaAtualizada.getTimeCasa());
            partida.setTimeFora(partidaAtualizada.getTimeFora());
            partida.setDataJogo(partidaAtualizada.getDataJogo());
            return partidaRepository.save(partida);
        }).orElseThrow(() -> new RuntimeException("Partida não encontrada"));
    }

    // 🔹 Finalizar uma partida e definir o resultado (APENAS ADMIN)
    public void finalizarPartida(Long id, Partida partidaFinalizada) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        partida.definirResultado(partidaFinalizada.getResultado());
        partidaRepository.save(partida);
    }

    // 🔹 Remover uma partida (APENAS ADMIN)
    public void removerPartida(Long id) {
        partidaRepository.deleteById(id);
    }
}