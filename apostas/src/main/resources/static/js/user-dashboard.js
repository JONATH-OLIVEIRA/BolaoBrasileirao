document.addEventListener("DOMContentLoaded", async () => {
    try {
        // 🔹 Elementos da UI
        const tabelaApostas = document.getElementById("tabelaApostas");
        const loadingIndicator = document.getElementById("loading-indicator");
        const errorContainer = document.getElementById("error-container");
        
        // 🔹 Mostra loading
        loadingIndicator.style.display = 'block';
        tabelaApostas.style.display = 'none';
        errorContainer.style.display = 'none';

        // 🔹 Recupera credenciais
        const token = localStorage.getItem("token");
        const usuarioId = localStorage.getItem("userId");

        console.log("🔹 Token no localStorage:", token);
        console.log("🔹 ID do usuário:", usuarioId);

        // 🔹 Validação básica
        if (!token || !usuarioId) {
            throw new Error("Usuário não autenticado");
        }

        // 🔹 Verifica token no backend
        const validateResponse = await fetch("/auth/validate-token", {
            method: "GET",
            headers: { 
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!validateResponse.ok) {
            throw new Error("Token inválido ou expirado");
        }

        // 🔹 Carrega apostas
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

        // 🔹 Preenche tabela
        apostas.forEach(aposta => {
            const row = tabelaApostas.insertRow();
            row.innerHTML = `
                <td>${aposta.partida.timeCasa} vs ${aposta.partida.timeFora}</td>
                <td>${new Date(aposta.partida.dataJogo).toLocaleDateString('pt-BR')}</td>
                <td>${aposta.escolha}</td>
                <td>${aposta.partida.resultado || 'Aguardando'}</td>
            `;
        });

        // 🔹 Mostra resultados
        tabelaApostas.style.display = 'table';
        loadingIndicator.style.display = 'none';

    } catch (error) {
        console.error("Erro:", error);
        
        // 🔹 Mostra erro na UI
        document.getElementById("loading-indicator").style.display = 'none';
        document.getElementById("error-container").style.display = 'block';
        document.getElementById("error-message").textContent = error.message;

        // 🔹 Limpa storage e redireciona se for erro de autenticação
        if (error.message.includes("autenticado") || error.message.includes("Token")) {
            localStorage.removeItem("token");
            localStorage.removeItem("userId");
            setTimeout(() => {
                window.location.href = "/auth/login";
            }, 1500);
        }
    }
});