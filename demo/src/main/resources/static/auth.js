// Configuration de l'API
const API_BASE_URL = '/api/users';

// Variables globales
let currentUser = null;

// Initialisation
document.addEventListener('DOMContentLoaded', function() {
    // Vérifier si l'utilisateur est déjà connecté
    checkCurrentUser();
    
    // Event listeners
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
});

// Vérifier si l'utilisateur est connecté (via localStorage)
function checkCurrentUser() {
    const userData = localStorage.getItem('currentUser');
    if (userData) {
        currentUser = JSON.parse(userData);
        // Rediriger vers l'espace utilisateur
        window.location.href = 'user-space.html';
    }
}

// Afficher le formulaire d'inscription
function showRegister() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('register-form').style.display = 'block';
    hideMessage();
}

// Afficher le formulaire de connexion
function showLogin() {
    document.getElementById('register-form').style.display = 'none';
    document.getElementById('login-form').style.display = 'block';
    hideMessage();
}

// Gérer la connexion
async function handleLogin(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const loginData = {
        username: formData.get('username'),
        password: formData.get('password')
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/connexion`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // Connexion réussie
            currentUser = result.user;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            
            showMessage(result.message, 'success');
            
            // Rediriger vers l'espace utilisateur après 1 seconde
            setTimeout(() => {
                window.location.href = 'user-space.html';
            }, 1000);
            
        } else {
            // Erreur de connexion
            showMessage(result.error, 'error');
        }
        
    } catch (error) {
        console.error('Erreur lors de la connexion:', error);
        showMessage('Erreur de connexion. Veuillez réessayer.', 'error');
    }
}

// Gérer l'inscription
async function handleRegister(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const password = formData.get('password');
    const confirmPassword = formData.get('confirmPassword');
    
    // Vérifier que les mots de passe correspondent
    if (password !== confirmPassword) {
        showMessage('Les mots de passe ne correspondent pas', 'error');
        return;
    }
    
    const registerData = {
        username: formData.get('username'),
        email: formData.get('email'),
        password: password
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/inscription`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(registerData)
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // Inscription réussie
            showMessage(result.message + ' Vous pouvez maintenant vous connecter.', 'success');
            
            // Basculer vers le formulaire de connexion après 2 secondes
            setTimeout(() => {
                showLogin();
                // Pré-remplir le nom d'utilisateur
                document.getElementById('loginUsername').value = registerData.username;
            }, 2000);
            
        } else {
            // Erreur d'inscription
            showMessage(result.error, 'error');
        }
        
    } catch (error) {
        console.error('Erreur lors de l\'inscription:', error);
        showMessage('Erreur d\'inscription. Veuillez réessayer.', 'error');
    }
}

// Afficher un message
function showMessage(text, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = text;
    messageDiv.className = `message ${type}`;
    messageDiv.style.display = 'block';
}

// Masquer le message
function hideMessage() {
    const messageDiv = document.getElementById('message');
    messageDiv.style.display = 'none';
}

// Fonction pour déconnecter l'utilisateur
function logout() {
    localStorage.removeItem('currentUser');
    currentUser = null;
    window.location.href = 'auth.html';
}
