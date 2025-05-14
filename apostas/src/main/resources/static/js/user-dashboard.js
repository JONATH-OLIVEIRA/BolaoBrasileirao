document.addEventListener("DOMContentLoaded", async () => {
    try {
        const tabelaApostas = document.getElementById("tabelaApostas");
        const loadingIndicator = document.getElementById("loading-indicator");
        const errorContainer = document.getElementById("error-container");

        // 🔹 Exibe indicador de carregamento
        loadingIndicator.style.display = 'block';
        tabelaApostas.style.display = 'none';
        errorContainer.style.display = 'none';

        // 🔹 Recupera credenciais
        const token = localStorage.getItem("token");
        const usuarioId = localStorage.getItem("usuarioId");

        console.log("🔹 Token no localStorage:", token);
        console.log("🔹 ID do usuário:", usuarioId);

        if (!token || !usuarioId) {
            throw new Error("Usuário não autenticado");
        }

        // 🔹 Requisição para buscar apostas do usuário
        const response = await fetch(`/usuario/partidas-api/usuario/${usuarioId}`, {
            method: "GET",
            headers: { 
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error("Falha ao carregar apostas");
        }

        const apostas = await response.json();
        console.log("✅ Apostas carregadas:", apostas);

        if (apostas.length === 0) {
            errorContainer.style.display = 'block';
            errorContainer.innerHTML = "<p>Nenhuma aposta encontrada.</p>";
        } else {
            // 🔹 Popula tabela
            apostas.forEach(aposta => {
                const row = tabelaApostas.insertRow();
                row.innerHTML = `
                    <td>${aposta.partida.timeCasa} vs ${aposta.partida.timeFora}</td>
                    <td>${new Date(aposta.partida.dataJogo).toLocaleDateString('pt-BR')}</td>
                    <td>${aposta.resultadoEscolhido}</td>
                    <td>${aposta.partida.resultado || 'Aguardando'}</td>
                `;
            });

            tabelaApostas.style.display = 'table';
        }

        loadingIndicator.style.display = 'none';

    } catch (error) {
        console.error("❌ Erro ao carregar apostas:", error);
        
        // 🔹 Mostra erro na UI
        document.getElementById("loading-indicator").style.display = 'none';
        document.getElementById("error-container").style.display = 'block';
        document.getElementById("error-container").textContent = error.message;

        // 🔹 Se for erro de autenticação, limpa o localStorage e redireciona para login
        if (error.message.includes("autenticado") || error.message.includes("Token")) {
            localStorage.removeItem("token");
            localStorage.removeItem("usuarioId");
            setTimeout(() => {
                window.location.href = "/auth/login";
            }, 1500);
        }
    }
});