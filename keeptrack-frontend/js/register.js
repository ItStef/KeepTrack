document.getElementById('registerForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    const userPayload = {
        username,
        email,
        password,
        role
    };

    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userPayload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Error while registering..');
        }

        const message = await response.text();
        const msgEl = document.getElementById('registerMessage');
        msgEl.textContent = 'Registration successful! Redirecting to the login page';
        msgEl.style.color = 'green';

        this.reset();

        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);

    } catch (error) {
        const msgEl = document.getElementById('registerMessage');
        msgEl.textContent = error.message;
        msgEl.style.color = 'red';
    }
});