document.getElementById("cadastroUsuario").addEventListener("submit", async (event) => {
    event.preventDefault();
    
    const usuario = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        senha: document.getElementById("senha").value,
        cpf: document.getElementById("cpf").value,
        dtNascimento: document.getElementById("dataNascimento").value
    };

    // Validação manual da senha no frontend (opcional, mas recomendado)
    const senhaRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    if (!senhaRegex.test(usuario.senha)) {
        alert("A senha deve conter:\n- Pelo menos 8 caracteres\n- 1 letra maiúscula\n- 1 letra minúscula\n- 1 número");
        return;
    }

    console.log("Dados sendo enviados:", usuario);

    try {
        const response = await fetch("/usuarios", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuario)
        });

        if (response.ok) {
            alert("✅ Cadastro realizado com sucesso!");
            window.location.href = "/auth/login";
        } else {
            const errorMessage = await response.text();
            alert(`❌ Erro no cadastro: ${errorMessage}`);
        }
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro ao conectar com o servidor");
    }
});