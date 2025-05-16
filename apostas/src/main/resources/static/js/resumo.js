document.addEventListener("DOMContentLoaded", function() {
    // Elementos da UI
    const loadingIndicator = document.getElementById("loading-indicator");
    const errorContainer = document.getElementById("error-container");
    const refreshButton = document.getElementById("refresh-button");
    
    // Carrega os dados inicialmente
    carregarResumo();
    
    // Configura botão de atualização
    if (refreshButton) {
        refreshButton.addEventListener("click", carregarResumo);
    }
});

async function carregarResumo() {
    console.log("🔹 Carregando resumo do sistema...");
    const token = localStorage.getItem("token");
    const loadingIndicator = document.getElementById("loading-indicator");
    const errorContainer = document.getElementById("error-container");

    // Mostra loading e esconde erros
    if (loadingIndicator) loadingIndicator.style.display = 'block';
    if (errorContainer) {
        errorContainer.style.display = 'none';
        errorContainer.textContent = '';
    }

    try {
        const response = await fetch("/admin/dashboard-api", {
            method: "GET",
            headers: { 
                "Authorization": `Bearer ${token}`,
                "Accept": "application/json"
            },
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }

        const dados = await response.json();
        console.log("Dados recebidos do backend:", dados);

        // Validação rigorosa dos dados
        const dadosValidados = validarDados(dados);
        console.log("Dados validados:", dadosValidados);

        // Atualiza a interface
        atualizarNumeros(dadosValidados);
        criarGraficos(dadosValidados);

    } catch (error) {
        console.error("❌ Erro ao carregar resumo:", error);
        mostrarErro(error.message || "Erro ao carregar dados do dashboard");
        
        // Se for erro 401 (não autorizado), redireciona para login
        if (error.message.includes("401")) {
            setTimeout(() => {
                window.location.href = "/auth/login?error=session_expired";
            }, 2000);
        }
    } finally {
        if (loadingIndicator) loadingIndicator.style.display = 'none';
    }
}

function validarDados(dados) {
    // Garante que todos os campos numéricos existam e sejam números válidos
    return {
        totalApostas: Math.max(0, parseInt(dados.totalApostas) || 0,
        totalPartidas: Math.max(0, parseInt(dados.totalPartidas) || 0,
        totalUsuarios: Math.max(0, parseInt(dados.totalUsuarios) || 0,
        apostasConfirmadas: Math.max(0, parseInt(dados.apostasConfirmadas) || 0,
        apostasPendentes: Math.max(0, parseInt(dados.apostasPendentes) || 0,
        apostasCanceladas: Math.max(0, parseInt(dados.apostasCanceladas) || 0,
        usuariosAtivos: Math.max(0, parseInt(dados.usuariosAtivos) || 0,
        usuariosInativos: Math.max(0, parseInt(dados.usuariosInativos) || 0
    };
}

function atualizarNumeros(dados) {
    // Atualiza os elementos na página com os dados validados
    document.getElementById("totalApostas").textContent = dados.totalApostas.toLocaleString();
    document.getElementById("totalPartidas").textContent = dados.totalPartidas.toLocaleString();
    document.getElementById("totalUsuarios").textContent = dados.totalUsuarios.toLocaleString();
}

function criarGraficos(dados) {
    // Destrói gráficos existentes para evitar sobreposição
    destruirGraficos();

    // Cria gráfico de distribuição de apostas
    criarGraficoApostas(dados);
    
    // Cria gráfico de status dos usuários
    criarGraficoUsuarios(dados);
}

function destruirGraficos() {
    // Lista de todos os gráficos na página
    const graficos = [
        Chart.getChart("graficoApostas"),
        Chart.getChart("graficoUsuarios")
    ];
    
    // Destrói cada gráfico existente
    graficos.forEach(grafico => {
        if (grafico) grafico.destroy();
    });
}

function criarGraficoApostas(dados) {
    const ctx = document.getElementById("graficoApostas").getContext("2d");
    
    new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Confirmadas", "Pendentes", "Canceladas"],
            datasets: [{
                label: "Quantidade de Apostas",
                data: [
                    dados.apostasConfirmadas,
                    dados.apostasPendentes,
                    dados.apostasCanceladas
                ],
                backgroundColor: [
                    "rgba(0, 123, 255, 0.7)",
                    "rgba(255, 193, 7, 0.7)",
                    "rgba(220, 53, 69, 0.7)"
                ],
                borderColor: [
                    "rgba(0, 123, 255, 1)",
                    "rgba(255, 193, 7, 1)",
                    "rgba(220, 53, 69, 1)"
                ],
                borderWidth: 1
            }]
        },
        options: getOpcoesGraficoBarras()
    });
}

function criarGraficoUsuarios(dados) {
    const ctx = document.getElementById("graficoUsuarios").getContext("2d");
    
    new Chart(ctx, {
        type: "pie",
        data: {
            labels: ["Ativos", "Inativos"],
            datasets: [{
                label: "Status dos Usuários",
                data: [
                    dados.usuariosAtivos,
                    dados.usuariosInativos
                ],
                backgroundColor: [
                    "rgba(40, 167, 69, 0.7)", // Verde para ativos
                    "rgba(220, 53, 69, 0.7)"   // Vermelho para inativos
                ],
                borderWidth: 1
            }]
        },
        options: getOpcoesGraficoPizza()
    });
}

function getOpcoesGraficoBarras() {
    return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        return `${context.dataset.label}: ${context.raw.toLocaleString()}`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    precision: 0,
                    callback: function(value) {
                        return Number.isInteger(value) ? value : '';
                    }
                }
            }
        }
    };
}

function getOpcoesGraficoPizza() {
    return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'top',
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        const total = context.dataset.data.reduce((a, b) => a + b, 0);
                        const percentage = total > 0 ? Math.round((context.raw / total) * 100) : 0;
                        return `${context.label}: ${context.raw} (${percentage}%)`;
                    }
                }
            }
        }
    };
}

function mostrarErro(mensagem) {
    const errorContainer = document.getElementById("error-container");
    if (errorContainer) {
        errorContainer.textContent = mensagem;
        errorContainer.style.display = 'block';
    }
}