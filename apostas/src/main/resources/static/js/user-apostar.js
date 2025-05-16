document.addEventListener('DOMContentLoaded', function() {
    const token = document.cookie.split('; ').find(row => row.startsWith('JWT='))?.split('=')[1];
    
    if (!token) {
        window.location.href = "/auth/login";
        return;
    }
});

async function checkAuthStatus() {
    try {
        const response = await fetch('/auth/check', {
            method: 'GET',
            credentials: 'include' // Importante para enviar cookies
        });

        if (!response.ok) {
            window.location.href = "/auth/login";
            return;
        }
    } catch (error) {
        console.error("Erro ao verificar autenticação:", error);
        window.location.href = "/auth/login";
    }
}

document.getElementById("apostaForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    if (document.querySelectorAll("input[type=radio]:checked").length === 0) {
        alert("❌ Selecione pelo menos uma aposta antes de confirmar!");
        return;
    }

    const usuarioId = document.cookie.split('; ').find(row => row.startsWith('userId='))?.split('=')[1];

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
            usuario: { id: usuarioId }, 
            partida: { id: partidaId }, 
            resultadoEscolhido: escolha 
        });
    });

    console.log("✅ JSON enviado:", JSON.stringify(apostas));

    try {
        const response = await fetch(`/apostas/registrar`, {
            method: "POST",
            headers: { 
                "Content-Type": "application/json"
                // O cookie JWT é enviado automaticamente (HTTP-Only)
            },
            credentials: 'include', // Garante que os cookies sejam enviados
            body: JSON.stringify(apostas)
        });

        if (response.status === 401) {
            alert("❌ Sessão expirada. Faça login novamente.");
            window.location.href = "/auth/login";
            return;
        }

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert("✅ Apostas registradas com sucesso!");
        window.location.href = "/user/dashboard";
    } catch (error) {
        alert(`❌ Erro ao registrar apostas: ${error.message}`);
    }
});