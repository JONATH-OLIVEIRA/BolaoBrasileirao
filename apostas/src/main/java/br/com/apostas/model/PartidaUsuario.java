package br.com.apostas.model;

import java.io.Serializable;
import java.util.Objects;

import br.com.apostas.enums.ResultadoEscolhido;
import br.com.apostas.enums.ResultadoPartida;
import jakarta.persistence.Column;
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
@Table(name = "partida_usuario")
public class PartidaUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario; // Relacionamento com o usuário que participa da partida

	@ManyToOne
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida; // A partida que o usuário está interagindo

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O resultado escolhido é obrigatório")
	private ResultadoEscolhido resultadoEscolhido; // CASA_VENCE, FORA_VENCE ou EMPATE

	@Column(nullable = false)
	private final Double valorAposta = 10.0; // Valor fixo de R$10,00

	@Column(nullable = false)
	private Integer pontuacao = 0; // Inicialmente zero, calculado após o jogo

	// 🔹 Construtor padrão
	public PartidaUsuario() {
	}

	// 🔹 Construtor personalizado
	public PartidaUsuario(Usuario usuario, Partida partida, ResultadoEscolhido resultadoEscolhido) {
		this.usuario = usuario;
		this.partida = partida;
		this.resultadoEscolhido = resultadoEscolhido;
	}

	// 🔹 Método para calcular a pontuação após o jogo ser finalizado
	public void calcularPontuacao() {
	    if (partida.getResultado() == null) {
	        throw new IllegalStateException("A partida ainda não foi finalizada!");
	    }

	    if (resultadoEscolhido == ResultadoEscolhido.EMPATE && partida.getResultado() == ResultadoPartida.EMPATE) {
	        pontuacao = 1; // Se escolheu empate e deu empate, ganha 1 ponto
	    } else if (resultadoEscolhido.name().equals(partida.getResultado().name())) {
	        pontuacao = 3; // Se acertou o vencedor, ganha 3 pontos
	    } else {
	        pontuacao = 0; // Caso contrário, não ganha pontos
	    }
	}

	// 🔹 Getters e Setters
	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Partida getPartida() {
		return partida;
	}

	public ResultadoEscolhido getResultadoEscolhido() {
		return resultadoEscolhido;
	}

	public Double getValorAposta() {
		return valorAposta;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public void setResultadoEscolhido(ResultadoEscolhido resultadoEscolhido) {
		this.resultadoEscolhido = resultadoEscolhido;
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
		PartidaUsuario other = (PartidaUsuario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "PartidaUsuario [id=" + id + ", usuario=" + usuario.getId() + ", partida=" + partida.getId()
				+ ", resultadoEscolhido=" + resultadoEscolhido + ", valorAposta=" + valorAposta + ", pontuacao="
				+ pontuacao + "]";
	}
}