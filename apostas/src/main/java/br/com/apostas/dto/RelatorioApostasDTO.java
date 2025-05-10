package br.com.apostas.dto;

import java.math.BigDecimal;

public class RelatorioApostasDTO {
    private long totalApostas;
    private BigDecimal valorTotal;
    private BigDecimal mediaValorAposta;

    public RelatorioApostasDTO(Long totalApostas, Double valorTotal, Double mediaValorAposta) {
        this.totalApostas = (totalApostas != null) ? totalApostas : 0;
        this.valorTotal = (valorTotal != null) ? BigDecimal.valueOf(valorTotal) : BigDecimal.ZERO;
        this.mediaValorAposta = (mediaValorAposta != null) ? BigDecimal.valueOf(mediaValorAposta) : BigDecimal.ZERO;
    }

    public long getTotalApostas() { return totalApostas; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public BigDecimal getMediaValorAposta() { return mediaValorAposta; }
}