package br.com.apostas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.apostas.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findByCpf(String cpf);

	boolean existsByEmail(String email);

	boolean existsByCpf(String cpf);

	List<Usuario> findByAtivoTrue(); // Retorna apenas usuários ativos

	long countByAtivoTrue();

	// 🔹 Contar usuários inativos
	long countByAtivoFalse();

}