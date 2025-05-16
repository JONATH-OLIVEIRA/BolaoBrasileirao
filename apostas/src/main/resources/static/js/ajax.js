async function cadastrarPartida(event) {
    event.preventDefault();
    console.log("✅ Cadastro de partida acionado!");

    const partida = {
        timeCasa: document.getElementById("timeCasa").value,
        timeFora: document.getElementById("timeFora").value,
        dataJogo: document.getElementById("dataJogo").value,
        rodada: document.getElementById("rodada").value
    };

    const token = localStorage.getItem("token");
    const response = await fetch("/admin/partidas-api", { // 🔹 Caminho atualizado
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(partida)
    });

    console.log(`Código HTTP da resposta do cadastro: ${response.status}`);

    if (response.ok) {
        alert("✅ Partida cadastrada com sucesso!");
        location.reload();
    } else {
        alert("❌ Erro ao cadastrar partida!");
        console.log(await response.text());
    }
}

async function excluirPartida(partidaId) {
    console.log(`✅ Excluir partida acionado! ID: ${partidaId}`);

    const token = localStorage.getItem("token");
    const response = await fetch(`/admin/partidas-api/${partidaId}`, { // 🔹 Caminho atualizado
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token }
    });

    if (response.ok) {
        alert("✅ Partida excluída com sucesso!");
        location.reload();
    } else {
        alert("❌ Erro ao excluir partida!");
        console.log(await response.text());
    }
}

async function finalizarPartida(partidaId, resultado) {
    if (!resultado) {
        alert("Por favor, selecione um resultado antes de finalizar.");
        return;
    }

    console.log("Finalizando partida:", { partidaId, resultado });

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Sessão expirada. Redirecionando para login...");
        window.location.href = "/auth/login";
        return;
    }

    try {
        const response = await fetch(`/admin/partidas-api/${partidaId}/finalizar`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(resultado.toUpperCase()) // ✅ apenas string, não objeto
        });

        if (response.status === 401) {
            localStorage.removeItem("token");
            throw new Error("Sessão expirada. Faça login novamente.");
        }

        const responseData = await response.json().catch(() => ({}));

        if (!response.ok) {
            throw new Error(responseData.message || `Erro ${response.status}`);
        }

        alert("Partida finalizada com sucesso!");
        location.reload();

    } catch (error) {
        console.error("Erro na finalização:", error);
        alert(error.message);

        if (error.message.includes("Sessão expirada")) {
            window.location.href = "/auth/login";
        }
    }
}
async function corrigirResultado(partidaId) {
    console.log("✅ Correção de resultado acionada!");

    const novoResultado = prompt("Digite o novo resultado: CASA, FORA ou EMPATE").toUpperCase();
    const justificativa = prompt("Digite a justificativa para a correção:");
    const token = localStorage.getItem("token");

    if (!token) {
        alert("❌ Token de autenticação não encontrado. Faça login novamente!");
        return;
    }

    console.log(`Partida ID: ${partidaId}, Novo Resultado: ${novoResultado}, Justificativa: ${justificativa}`);

    try {
        const response = await fetch(`/admin/partidas-api/${partidaId}/corrigir-resultado`, { // 🔹 Caminho atualizado
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({ novoResultado, justificativa })
        });

        if (response.ok) {
            alert("✅ Resultado corrigido com sucesso!");
            setTimeout(() => {
                location.reload();
            }, 1000);
        } else {
            alert("❌ Erro ao corrigir resultado!");
            console.log(await response.text());
        }
    } catch (error) {
        alert("❌ Ocorreu um erro inesperado ao corrigir o resultado.");
    }
}