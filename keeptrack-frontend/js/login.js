document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    try {
        const response = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            const err = await response.json();
            throw new Error(err.error || 'Wrong username or password!!');
        }

        const data = await response.json();

        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        localStorage.setItem('role', data.role);

        location.assign('index.html'); 
        
    } catch (error) {
        const msg = document.getElementById('loginMessage');
        if (msg) {
            msg.textContent = error.message || 'Error while logging in';
            msg.style.color = 'red';
        }
    }
});