package br.com.apostas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.apostas.enums.StatusAposta;
import br.com.apostas.model.Aposta;

@Repository
public interface ApostaRepository extends JpaRepository<Aposta, Long> {

    // 🔹 Buscar todas as apostas feitas em uma partida específica
    List<Aposta> findByPartidaId(Long partidaId);

    // 🔹 Buscar todas as apostas feitas por um usuário em uma partida
    List<Aposta> findByPartidaIdAndUsuarioId(Long partidaId, Long usuarioId);

    // 🔹 Contar número de apostas em uma partida específica
    long countByPartidaId(Long partidaId);

    // 🔹 Buscar todas as apostas feitas por um usuário específico
    List<Aposta> findByUsuarioId(Long usuarioId);

    // 🔹 Excluir todas as apostas associadas a uma partida
    void deleteByPartidaId(Long partidaId);
    
    List<Aposta> findByStatus(StatusAposta status);


   }