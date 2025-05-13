document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("cadastrarPartida").addEventListener("submit", cadastrarPartida);
});

// Chamadas de funções AJAX ao clicar nos botões
function setupEventListeners() {
    document.querySelectorAll(".excluir-btn").forEach(button => {
        button.addEventListener("click", () => {
            const partidaId = button.getAttribute("data-id");
            excluirPartida(partidaId);
        });
    });

    document.querySelectorAll(".finalizar-btn").forEach(button => {
        button.addEventListener("click", () => {
            const partidaId = button.getAttribute("data-id");
            const resultado = document.getElementById(`resultadoFinal_${partidaId}`).value;
            finalizarPartida(partidaId, resultado);
        });
    });

    document.querySelectorAll(".corrigir-btn").forEach(button => {
        button.addEventListener("click", () => {
            const partidaId = button.getAttribute("data-id");
            corrigirResultado(partidaId);
        });
    });
}

setupEventListeners(); // Configurar eventos ao carregar a página