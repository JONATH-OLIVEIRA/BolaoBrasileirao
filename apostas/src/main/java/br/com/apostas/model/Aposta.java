package br.com.apostas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import br.com.apostas.enums.EscolhaAposta;
import br.com.apostas.enums.StatusAposta;
import jakarta.persistence.*;

@Entity
@Table(name = "apostas")
public class Aposta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EscolhaAposta escolha;

	@Column(nullable = false)
	private Integer pontuacao = 0;

	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusAposta status;

	@Column(nullable = false)
	private BigDecimal valorAposta;

	@Column(nullable = false)
	private String formaPagamento;

	// 🔹 Construtor
	public Aposta() {
		this.dataCriacao = LocalDateTime.now();
		this.status = StatusAposta.PENDENTE;
		this.valorAposta = BigDecimal.valueOf(10.00);
	}

	public Aposta(Usuario usuario, Partida partida, EscolhaAposta escolha, String formaPagamento) {
		this.usuario = usuario;
		this.partida = partida;
		this.escolha = escolha;
		this.formaPagamento = formaPagamento;
		this.valorAposta = BigDecimal.valueOf(10.00);
		this.dataCriacao = LocalDateTime.now();
		this.status = StatusAposta.PENDENTE;
	}

	// 🔹 Método para calcular a pontuação após a partida ser finalizada
	public void calcularPontuacao() {
		if (this.partida.getResultado().name().equals(this.escolha.name())) {
			this.pontuacao = (this.escolha == EscolhaAposta.EMPATE) ? 1 : 3;
			this.status = StatusAposta.CONFIRMADA;
		} else {
			this.pontuacao = 0;
			this.status = StatusAposta.CANCELADA;
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

	public EscolhaAposta getEscolha() {
		return escolha;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public StatusAposta getStatus() {
		return status;
	}

	public BigDecimal getValorAposta() {
		return valorAposta;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public void setEscolha(EscolhaAposta escolha) {
		this.escolha = escolha;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
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
}