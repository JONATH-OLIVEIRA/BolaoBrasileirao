document.addEventListener("DOMContentLoaded", function() {
    carregarUsuarios();
});

async function carregarUsuarios() {
    console.log("🔹 Carregando lista de usuários...");
    const token = localStorage.getItem("token");

    try {
        const response = await fetch("/admin/usuarios-api", {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Erro ao carregar usuários");
        }

        const usuarios = await response.json();
        atualizarTabelaUsuarios(usuarios);
    } catch (error) {
        console.error("❌ Erro ao carregar usuários:", error);
        document.getElementById("erro-usuarios").classList.remove("d-none");
    }
}

function atualizarTabelaUsuarios(usuarios) {
    const tbody = document.getElementById("usuarios-tabela");
    tbody.innerHTML = ""; // Limpa a tabela

    usuarios.forEach(usuario => {
        const row = document.createElement("tr");
        row.setAttribute("data-id", usuario.id);
        row.innerHTML = `
            <td>${usuario.nome}</td>
            <td>${usuario.email}</td>
            <td>${usuario.ativo ? "Ativo" : "Inativo"}</td>
            <td>
                <select id="role_${usuario.id}" class="form-select form-select-sm" data-id="${usuario.id}">
                    <option value="USER" ${usuario.tipo === "USER" ? "selected" : ""}>Usuário</option>
                    <option value="ADMIN" ${usuario.tipo === "ADMIN" ? "selected" : ""}>Admin</option>
                </select>
            </td>
            <td>
                ${!usuario.ativo 
                    ? `<button onclick="ativarUsuario(${usuario.id})" class="btn btn-success btn-sm">Ativar</button>` 
                    : `<button onclick="desativarUsuario(${usuario.id})" class="btn btn-danger btn-sm">Desativar</button>`
                }
                <button onclick="alterarPermissaoUsuario(${usuario.id}, document.getElementById('role_${usuario.id}').value)" 
                    class="btn btn-warning btn-sm">Salvar</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function ativarUsuario(usuarioId) {
    console.log(`🔹 Ativando usuário ID: ${usuarioId}`);
    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`/admin/usuarios-api/${usuarioId}/ativar`, {
            method: "PUT",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Erro ao ativar usuário");
        }

        alert("✅ Usuário ativado com sucesso!");
        carregarUsuarios();
    } catch (error) {
        console.error("❌ Erro ao ativar usuário:", error);
        alert("❌ Erro ao ativar usuário!");
    }
}

async function desativarUsuario(usuarioId) {
    console.log(`🔹 Desativando usuário ID: ${usuarioId}`);
    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`/admin/usuarios-api/${usuarioId}/desativar`, {
            method: "PUT",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Erro ao desativar usuário");
        }

        alert("✅ Usuário desativado com sucesso!");
        carregarUsuarios();
    } catch (error) {
        console.error("❌ Erro ao desativar usuário:", error);
        alert("❌ Erro ao desativar usuário!");
    }
}

async function alterarPermissaoUsuario(usuarioId, novoTipo) {
    console.log(`🔹 Alterando permissão do usuário ID: ${usuarioId} para ${novoTipo}`);
    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`/admin/usuarios-api/${usuarioId}/role?novoTipo=${novoTipo}`, {
            method: "PUT",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Erro ao alterar permissão");
        }

        alert("✅ Permissão alterada com sucesso!");
        carregarUsuarios();
    } catch (error) {
        console.error("❌ Erro ao alterar permissão do usuário:", error);
        alert("❌ Erro ao alterar permissão do usuário!");
    }
}