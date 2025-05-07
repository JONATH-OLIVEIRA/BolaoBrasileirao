package br.com.apostas.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;    

import br.com.apostas.enums.ResultadoPartida;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "partidas")
public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rodada é obrigatória")
    private Integer rodada;

    @NotBlank(message = "Nome do time da casa não pode estar vazio")
    private String timeCasa;

    @NotBlank(message = "Nome do time visitante não pode estar vazio")
    private String timeFora;

    @Enumerated(EnumType.STRING)
    private ResultadoPartida resultado; // CASA, FORA, EMPATE

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aposta> apostas;

    // Construtores
    public Partida() {}

    public Partida(Long id, Integer rodada, String timeCasa, String timeFora, ResultadoPartida resultado) {
        this.id = id;
        this.rodada = rodada;
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.resultado = resultado;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRodada() {
        return rodada;
    }

    public void setRodada(Integer rodada) {
        this.rodada = rodada;
    }

    public String getTimeCasa() {
        return timeCasa;
    }

    public void setTimeCasa(String timeCasa) {
        this.timeCasa = timeCasa;
    }

    public String getTimeFora() {
        return timeFora;
    }

    public void setTimeFora(String timeFora) {
        this.timeFora = timeFora;
    }

    public ResultadoPartida getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoPartida resultado) {
        this.resultado = resultado;
    }

    public List<Aposta> getApostas() {
        return apostas;
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Partida other = (Partida) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Partida [id=" + id + ", rodada=" + rodada + ", timeCasa=" + timeCasa +
               ", timeFora=" + timeFora + ", resultado=" + resultado + "]";
    }
}
