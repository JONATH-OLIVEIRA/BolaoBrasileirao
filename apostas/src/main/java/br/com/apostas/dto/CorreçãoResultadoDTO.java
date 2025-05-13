package br.com.apostas.dto;

public class CorreçãoResultadoDTO {
	private String novoResultado;
	private String justificativa;

	// Getters e Setters
	public String getNovoResultado() {
		return novoResultado;
	}

	public void setNovoResultado(String novoResultado) {
		this.novoResultado = novoResultado;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
}