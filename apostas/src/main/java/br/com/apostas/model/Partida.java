package br.com.apostas.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.apostas.enums.ResultadoPartida;
import br.com.apostas.enums.TimesBrasileirao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "partidas")
public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Time da casa é obrigatório")
    private TimesBrasileirao timeCasa;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Time visitante é obrigatório")
    private TimesBrasileirao timeFora;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private ResultadoPartida resultado = ResultadoPartida.PENDENTE; // Definir padrão

    @Column(nullable = false)
    @NotNull
    private LocalDate dataJogo;

    @Column(nullable = false)
    @NotNull(message = "A rodada da partida é obrigatória")
    private Integer rodada;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aposta> apostas;

    public Partida() {}

    public Partida(TimesBrasileirao timeCasa, TimesBrasileirao timeFora, LocalDate dataJogo, Integer rodada) {
        if (timeCasa == timeFora) {
            throw new IllegalArgumentException("Os times da partida devem ser diferentes!");
        }
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.dataJogo = validarDataJogo(dataJogo);
        setRodada(rodada); // Rodada digitada pelo usuário
    }

    private LocalDate validarDataJogo(LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data da partida não pode estar no passado!");
        }
        return data;
    }

    public void definirResultado(ResultadoPartida resultado) {
        this.resultado = resultado;
        processarApostas();
    }

    private void processarApostas() {
        if (apostas != null && !apostas.isEmpty()) {
            apostas.forEach(Aposta::calcularPontuacao);
        }
    }

    // Getters e Setters
    public void setResultado(ResultadoPartida resultado) { 
        this.resultado = resultado; 
    }

    public void setRodada(Integer rodada) {
        if (rodada == null || rodada < 1) {
            throw new IllegalArgumentException("❌ Rodada inválida! Deve ser um número maior que 0.");
        }
        this.rodada = rodada;
    }

    public Long getId() { return id; }
    public TimesBrasileirao getTimeCasa() { return timeCasa; }
    public TimesBrasileirao getTimeFora() { return timeFora; }
    public ResultadoPartida getResultado() { return resultado; }
    public LocalDate getDataJogo() { return dataJogo; }
    public Integer getRodada() { return rodada; }
    public List<Aposta> getApostas() { return apostas; }

    public void setTimeCasa(TimesBrasileirao timeCasa) { this.timeCasa = timeCasa; }
    public void setTimeFora(TimesBrasileirao timeFora) { this.timeFora = timeFora; }
    public void setDataJogo(LocalDate dataJogo) { this.dataJogo = validarDataJogo(dataJogo); }
    public void setApostas(List<Aposta> apostas) { this.apostas = apostas; }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(id, ((Partida) obj).id);
    }

    @Override
    public String toString() {
        return "Partida [id=" + id + ", timeCasa=" + timeCasa + ", timeFora=" + timeFora + 
               ", resultado=" + resultado + ", dataJogo=" + dataJogo + ", rodada=" + rodada + "]";
    }
}