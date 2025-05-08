package br.com.apostas.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import br.com.apostas.enums.ResultadoPartida;
import br.com.apostas.enums.TimesBrasileirao;

@Entity
@Table(name = "partidas")
public class Partida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Rodada é obrigatória")
	private Integer rodada;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Time da casa é obrigatório")
	private TimesBrasileirao timeCasa;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Time visitante é obrigatório")
	private TimesBrasileirao timeFora;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Resultado da partida é obrigatório")
	private ResultadoPartida resultado; // CASA, EMPATE, FORA

	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Aposta> apostas;

	// Construtor padrão
	public Partida() {
	}

	// Construtor personalizado
	public Partida(Integer rodada, TimesBrasileirao timeCasa, TimesBrasileirao timeFora, ResultadoPartida resultado) {
		this.rodada = rodada;
		this.timeCasa = timeCasa;
		this.timeFora = timeFora;
		this.resultado = resultado;
	}
	
	// Getters e Setters
	public Long getId() {
		return id;
	}

	public Integer getRodada() {
		return rodada;
	}

	public TimesBrasileirao getTimeCasa() {
		return timeCasa;
	}

	public TimesBrasileirao getTimeFora() {
		return timeFora;
	}

	public ResultadoPartida getResultado() {
		return resultado;
	}

	public List<Aposta> getApostas() {
		return apostas;
	}

	public void setRodada(Integer rodada) {
		this.rodada = rodada;
	}

	public void setTimeCasa(TimesBrasileirao timeCasa) {
		this.timeCasa = timeCasa;
	}

	public void setTimeFora(TimesBrasileirao timeFora) {
		this.timeFora = timeFora;
	}

	public void setResultado(ResultadoPartida resultado) {
		this.resultado = resultado;
	}

	public void setApostas(List<Aposta> apostas) {
		this.apostas = apostas;
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
		Partida other = (Partida) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Partida [id=" + id + ", rodada=" + rodada + ", timeCasa=" + timeCasa + ", timeFora=" + timeFora
				+ ", resultado=" + resultado + "]";
	}
}