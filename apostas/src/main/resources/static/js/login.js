document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                email: document.getElementById('email').value,
                senha: document.getElementById('senha').value
            })
        });

        const data = await response.json(); // Mova isso para fora da verificação
        
        if (response.status === 200) {
            localStorage.setItem("token", data.token);
            localStorage.setItem("userId", data.userId);
            
            console.log("Token:", data.token);
            console.log("UserID:", data.userId);
            console.log("Redirecionando para:", data.redirect);
            
            window.location.href = data.redirect;
        } else {
            console.error("Erro no login:", data);
             System.out.println("Tentativa de login com email: " + email);
        System.out.println("Senha recebida: " + senha);
            window.location.href = '/auth/login?error=' + encodeURIComponent(data.error || 'Erro desconhecido');
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        window.location.href = '/auth/login?error=' + encodeURIComponent('Erro na conexão com o servidor');
    }
});