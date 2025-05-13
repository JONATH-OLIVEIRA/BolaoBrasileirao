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
    const response = await fetch("/admin/partidas", {
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
    const response = await fetch(`/admin/partidas/${partidaId}`, {
        method: "DELETE",
        headers: { "Authorization": "Bearer " + token }
    });

    if (response.ok) {
        alert("✅ Partida excluída com sucesso!");

        // Atualizando a página para refletir as mudanças
        location.reload();
    } else {
        alert("❌ Erro ao excluir partida!");
        console.log(await response.text());
    }
}

async function finalizarPartida(partidaId, resultado) {
    console.log("✅ Finalizar partida acionado!");

    const token = localStorage.getItem("token");

    const response = await fetch(`/admin/partidas/${partidaId}/finalizar`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(resultado)
    });

    if (response.ok) {
        alert("✅ Partida finalizada com sucesso!");

        // Atualizando o resultado na tabela dinamicamente
        const resultadoCell = document.querySelector(`tr[data-id='${partidaId}'] td.resultado`);
        if (resultadoCell) {
            resultadoCell.innerText = resultado;
        } else {
            console.error("❌ Elemento do resultado não encontrado.");
        }
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
        const response = await fetch(`/admin/partidas/${partidaId}/corrigir-resultado`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({ novoResultado, justificativa })
        });

        if (response.ok) {
            alert("✅ Resultado corrigido com sucesso!");

            // Atualizando o resultado dinamicamente na tabela antes de recarregar
            const resultadoCell = document.querySelector(`tr[data-id="${partidaId}"] td.resultado`);
            if (resultadoCell) {
                resultadoCell.innerText = novoResultado;
                console.log(`✅ Resultado atualizado na tabela: ${novoResultado}`);
            } else {
                console.error("❌ Elemento do resultado não encontrado.");
            }

            // Recarregar a página após a atualização
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