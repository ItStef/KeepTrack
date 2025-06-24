function parseJwt(token) {
    if (!token) return null;
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}

function isLoggedIn() {
    const token = localStorage.getItem('token');
    return !!token;
}

function getUserRole() {
    if (!isLoggedIn()) return null;
    const token = localStorage.getItem('token');
    const payload = parseJwt(token);
    return payload?.role;
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    window.location.href = '/login.html';
}

function checkAuth() {
    if (!isLoggedIn()) {
        window.location.href = '/login.html';
        return;
    }

    const requiredRole = document.body.dataset.requiredRole;
    if (requiredRole && getUserRole() !== requiredRole) {
        window.location.href = '/login.html';
    }
}

async function apiFetch(url, options = {}) {
    if (!options.headers) options.headers = {};
    const token = localStorage.getItem('token');
    
    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }
    
    const response = await fetch(url, {
        ...options,
        credentials: 'include'
    });
    
    if (!response.ok) {
        if (response.status === 401) {
            logout();
        }
        throw new Error('API request failed');
    }
    
    return response;
}