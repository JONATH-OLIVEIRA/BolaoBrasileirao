package br.com.apostas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.apostas.model.Partida;
import br.com.apostas.model.PartidaUsuario;
import br.com.apostas.model.Usuario;

@Repository
public interface PartidaUsuarioRepository extends JpaRepository<PartidaUsuario, Long> {

    // 🔹 Buscar todas as apostas de um usuário
    List<PartidaUsuario> findByUsuario(Usuario usuario);

    // 🔹 Buscar todas as apostas feitas em uma partida específica
    List<PartidaUsuario> findByPartida(Partida partida);
    
}