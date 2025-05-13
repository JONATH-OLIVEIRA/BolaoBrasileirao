document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const response = await fetch('/auth/login', {
        method: 'POST',
        credentials: 'include', // CRUCIAL para cookies
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            email: document.getElementById('email').value,
            senha: document.getElementById('senha').value
        })
    });

    if (response.ok) {
        const data = await response.json();
        window.location.href = data.redirect; // Usa a URL do backend
    } else {
        const error = await response.text();
        window.location.href = '/auth/login?error=' + encodeURIComponent(error);
    }
});