package br.com.apostas.model;

import java.io.Serializable;
import java.util.Objects;

import br.com.apostas.enums.EscolhaAposta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "apostas")
public class Aposta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario; // Quem fez a aposta

	@ManyToOne
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida; // Qual partida foi apostada

	@Enumerated(EnumType.STRING)
	@NotNull(message = "A escolha da aposta é obrigatória")
	private EscolhaAposta escolha; // CASA, FORA, EMPATE

	private Integer pontuacao = 0; // Pontuação calculada após o resultado oficial da partida

	// Construtores
	public Aposta() {
	}

	public Aposta(Usuario usuario, Partida partida, EscolhaAposta escolha) {
		this.usuario = usuario;
		this.partida = partida;
		this.escolha = escolha;
	}

	// Método para atualizar a pontuação com base no resultado da partida
	public void calcularPontuacao() {
		if (this.partida.getResultado().name().equals(this.escolha.name())) {
			if (this.escolha == EscolhaAposta.EMPATE) {
				this.pontuacao = 1; // Empates valem apenas 1 ponto
			} else {
				this.pontuacao = 3; // Acertos normais valem 3 pontos
			}
		} else {
			this.pontuacao = 0; // Nenhum ponto se errar
		}
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public EscolhaAposta getEscolha() {
		return escolha;
	}

	public void setEscolha(EscolhaAposta escolha) {
		this.escolha = escolha;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Aposta other = (Aposta) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Aposta [id=" + id + ", usuario=" + usuario.getNome() + ", partida=" + partida.getTimeCasa() + " vs "
				+ partida.getTimeFora() + ", escolha=" + escolha + ", pontuacao=" + pontuacao + "]";
	}
}