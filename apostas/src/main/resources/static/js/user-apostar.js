document.getElementById("apostaForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const usuarioId = localStorage.getItem("usuarioId"); // 🔹 Recupera o ID do usuário
    if (!usuarioId) {
        alert("❌ Erro: Usuário não identificado! Faça login novamente.");
        window.location.href = "/auth/login";
        return;
    }

    const apostas = [];
    document.querySelectorAll("input[type=radio]:checked").forEach((radio) => {
        const partidaId = radio.name.split("_")[1];
        const escolha = radio.value;

        apostas.push({
            usuario: { id: usuarioId }, // 🔹 Agora `usuarioId` está dentro do objeto `usuario`
            partida: { id: partidaId },
            resultadoEscolhido: escolha
        });
    });

    console.log("✅ JSON enviado:", JSON.stringify(apostas)); // 🔹 Confirma antes de enviar

    try {
        const response = await fetch(`/usuario/partidas-api/apostar`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(apostas)
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }

        alert("✅ Apostas registradas com sucesso!");
        window.location.href = "/user/dashboard";
    } catch (error) {
        alert(`❌ Erro ao registrar apostas: ${error.message}`);
    }
});