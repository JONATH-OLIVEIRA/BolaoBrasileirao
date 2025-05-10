package br.com.apostas.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.apostas.model.Partida;
import br.com.apostas.enums.TimesBrasileirao;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

    // 🔹 Buscar a última partida cadastrada que ainda não foi finalizada
    Optional<Partida> findTopByResultadoIsNullOrderByIdDesc();

    // 🔹 Buscar todas as partidas já finalizadas
    List<Partida> findByResultadoIsNotNull();

    // 🔹 Buscar partidas de um time específico (Casa ou Fora)
    List<Partida> findByTimeCasaOrTimeFora(TimesBrasileirao timeCasa, TimesBrasileirao timeFora);
}
