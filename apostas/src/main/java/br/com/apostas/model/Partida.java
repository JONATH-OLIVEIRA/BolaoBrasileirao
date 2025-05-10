package br.com.apostas.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Time da casa é obrigatório")
    private TimesBrasileirao timeCasa;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Time visitante é obrigatório")
    private TimesBrasileirao timeFora;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private ResultadoPartida resultado = ResultadoPartida.PENDENTE; // Definir padrão

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dataJogo;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aposta> apostas;

    public Partida() {}

    public Partida(TimesBrasileirao timeCasa, TimesBrasileirao timeFora, LocalDateTime dataJogo) {
        if (timeCasa == timeFora) {
            throw new IllegalArgumentException("Os times da partida devem ser diferentes!");
        }
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.dataJogo = validarDataJogo(dataJogo);
    }

    private LocalDateTime validarDataJogo(LocalDateTime data) {
        if (data.isBefore(LocalDateTime.now())) {
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

    // 🔹 Getters e Setters
    public Long getId() { return id; }
    public TimesBrasileirao getTimeCasa() { return timeCasa; }
    public TimesBrasileirao getTimeFora() { return timeFora; }
    public ResultadoPartida getResultado() { return resultado; }
    public LocalDateTime getDataJogo() { return dataJogo; }
    public List<Aposta> getApostas() { return apostas; }

    public void setTimeCasa(TimesBrasileirao timeCasa) { this.timeCasa = timeCasa; }
    public void setTimeFora(TimesBrasileirao timeFora) { this.timeFora = timeFora; }
    public void setDataJogo(LocalDateTime dataJogo) { this.dataJogo = validarDataJogo(dataJogo); }
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
                ", resultado=" + resultado + ", dataJogo=" + dataJogo + "]";
    }
}
