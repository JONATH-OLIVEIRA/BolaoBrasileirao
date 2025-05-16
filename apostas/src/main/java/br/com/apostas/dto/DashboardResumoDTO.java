package br.com.apostas.dto;

public class DashboardResumoDTO {
    private long totalApostas;
    private long totalPartidas;
    private long totalUsuarios;
    private long apostasConfirmadas;
    private long apostasPendentes;
    private long apostasCanceladas;
    private long usuariosAtivos;
    private long usuariosInativos;
    
    public DashboardResumoDTO() {
    	
    }

    public DashboardResumoDTO(long totalApostas, long totalPartidas, long totalUsuarios,
                              long apostasConfirmadas, long apostasPendentes, long apostasCanceladas,
                              long usuariosAtivos, long usuariosInativos) {
        this.totalApostas = totalApostas;
        this.totalPartidas = totalPartidas;
        this.totalUsuarios = totalUsuarios;
        this.apostasConfirmadas = apostasConfirmadas;
        this.apostasPendentes = apostasPendentes;
        this.apostasCanceladas = apostasCanceladas;
        this.usuariosAtivos = usuariosAtivos;
        this.usuariosInativos = usuariosInativos;
    }

    // Getters
    public long getTotalApostas() { return totalApostas; }
    public long getTotalPartidas() { return totalPartidas; }
    public long getTotalUsuarios() { return totalUsuarios; }
    public long getApostasConfirmadas() { return apostasConfirmadas; }
    public long getApostasPendentes() { return apostasPendentes; }
    public long getApostasCanceladas() { return apostasCanceladas; }
    public long getUsuariosAtivos() { return usuariosAtivos; }
    public long getUsuariosInativos() { return usuariosInativos; }

    // Setters
    public void setTotalApostas(long totalApostas) { this.totalApostas = totalApostas; }
    public void setTotalPartidas(long totalPartidas) { this.totalPartidas = totalPartidas; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    public void setApostasConfirmadas(long apostasConfirmadas) { this.apostasConfirmadas = apostasConfirmadas; }
    public void setApostasPendentes(long apostasPendentes) { this.apostasPendentes = apostasPendentes; }
    public void setApostasCanceladas(long apostasCanceladas) { this.apostasCanceladas = apostasCanceladas; }
    public void setUsuariosAtivos(long usuariosAtivos) { this.usuariosAtivos = usuariosAtivos; }
    public void setUsuariosInativos(long usuariosInativos) { this.usuariosInativos = usuariosInativos; }
}