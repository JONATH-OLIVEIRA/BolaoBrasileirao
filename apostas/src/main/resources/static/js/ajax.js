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
    console.log("✅ Finalizar partida acionado!");

    const token = localStorage.getItem("token");

    const response = await fetch(`/admin/partidas-api/${partidaId}/finalizar`, { // 🔹 Caminho atualizado
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ resultado })
    });

    if (response.ok) {
        alert("✅ Partida finalizada com sucesso!");
        location.reload();
    } else {
        alert("❌ Erro ao finalizar partida!");
        console.log(await response.text());
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