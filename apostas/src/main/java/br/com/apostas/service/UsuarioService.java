package br.com.apostas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.apostas.model.Usuario;
import br.com.apostas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
        // Verifica se já existe um usuário com o mesmo email ou CPF
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já está em uso!");
        }
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new RuntimeException("CPF já está cadastrado!");
        }

        // Usa o método isMaiorDeIdade() da própria entidade
        if (!usuario.isMaiorDeIdade()) {
            throw new RuntimeException("O usuário deve ter pelo menos 18 anos!");
        }

        // Usa o método existente para criptografar a senha
        usuario.criptografarSenha();

        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    public boolean autenticarUsuario(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Comparação direta com o encoder já definido na classe
            return usuario.validarSenha(senha, passwordEncoder);
        }
        return false;
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void atualizarUsuario(Usuario usuario) {
        // Implementação futura
    }
}