package br.com.apostas.service;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.apostas.model.Usuario;
import br.com.apostas.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	@Value("${jwt.expiration}")
	private long jwtExpiration;

	private final Key secretKey;

	public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.expiration}") long jwtExpiration) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
				SignatureAlgorithm.HS256.getJcaName());
		this.jwtExpiration = jwtExpiration;
	}

	// 🔹 Autenticar usuário e gerar token JWT
	public String login(String email, String senha) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
		if (usuarioOpt.isEmpty() || !usuarioOpt.get().validarSenha(senha, passwordEncoder)) {
			throw new RuntimeException("Credenciais inválidas!");
		}

		Usuario usuario = usuarioOpt.get();
		return gerarToken(usuario);
	}

	// 🔹 Gerar token JWT
	private String gerarToken(Usuario usuario) {
		return Jwts.builder().setSubject(usuario.getEmail()).claim("role", usuario.getTipo().name()) // Adiciona o tipo
																										// de usuário
				.setIssuedAt(Date.from(Instant.now())) // Corrigido
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpiration))) // Define expiração corretamente
				.signWith(secretKey, SignatureAlgorithm.HS256) // Novo método correto
				.compact();
	}

	// 🔹 Validar token JWT
	public boolean validarToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 🔹 Extrair e-mail do token
	public String extrairEmail(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	public Claims extrairClaims(String token) {
	    return Jwts.parserBuilder()
	               .setSigningKey(secretKey)
	               .build()
	               .parseClaimsJws(token)
	               .getBody();
	}
}