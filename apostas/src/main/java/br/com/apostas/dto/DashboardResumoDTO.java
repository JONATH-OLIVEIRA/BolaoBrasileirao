package br.com.apostas.dto;

public class DashboardResumoDTO {
	private long totalApostas;
	private long totalPartidas;
	private long totalUsuarios;

	public DashboardResumoDTO(long totalApostas, long totalPartidas, long totalUsuarios) {
		this.totalApostas = totalApostas;
		this.totalPartidas = totalPartidas;
		this.totalUsuarios = totalUsuarios;
	}

	public long getTotalApostas() {
		return totalApostas;
	}

	public long getTotalPartidas() {
		return totalPartidas;
	}

	public long getTotalUsuarios() {
		return totalUsuarios;
	}

	public void setTotalApostas(long totalApostas) {
		this.totalApostas = totalApostas;
	}

	public void setTotalPartidas(long totalPartidas) {
		this.totalPartidas = totalPartidas;
	}

	public void setTotalUsuarios(long totalUsuarios) {
		this.totalUsuarios = totalUsuarios;
	}
}