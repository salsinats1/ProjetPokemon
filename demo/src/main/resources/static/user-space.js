// Variables globales
let currentUser = null;
let marketCartes = [];
let userCollection = [];
let isLoading = false;
let isMarketLoading = false;

// Initialisation
document.addEventListener('DOMContentLoaded', function() {
    // Vérifier si l'utilisateur est connecté
    if (!checkAuthentication()) {
        return; // Arrêter l'exécution si pas connecté
    }
    
    // Charger les informations utilisateur actualisées
    loadUserInfo();
    
    // Event listeners
    setupEventListeners();
    
    console.log('Page user-space initialisée');
});

// Vérifier l'authentification
function checkAuthentication() {
    const userData = localStorage.getItem('currentUser');
    if (!userData) {
        // Rediriger vers la page de connexion
        console.log('Utilisateur non connecté, redirection...');
        window.location.href = 'auth.html';
        return false;
    }
    
    currentUser = JSON.parse(userData);
    return true;
}

// Charger les informations utilisateur depuis le serveur
async function loadUserInfo() {
    if (!currentUser) return;
    
    try {
        // Récupérer les infos actualisées depuis le serveur
        const response = await fetch(`/api/users/${currentUser.id}`);
        if (response.ok) {
            const updatedUser = await response.json();
            currentUser = updatedUser;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
        }
        
        // Mettre à jour l'affichage
        const usernameEl = document.getElementById('username');
        const argentEl = document.getElementById('argent');
        
        if (usernameEl) {
            usernameEl.textContent = currentUser.username;
        }
        
        if (argentEl) {
            argentEl.textContent = currentUser.argent;
        }
        
        console.log(`Utilisateur connecté: ${currentUser.username} (${currentUser.argent} pièces)`);
    } catch (error) {
        console.error('Erreur lors du chargement des infos utilisateur:', error);
        // Fallback sur les données locales
        const usernameEl = document.getElementById('username');
        const argentEl = document.getElementById('argent');
        
        if (usernameEl) {
            usernameEl.textContent = currentUser.username;
        }
        
        if (argentEl) {
            argentEl.textContent = currentUser.argent;
        }
    }
}

// Configuration des event listeners
function setupEventListeners() {
    try {
        // Navigation
        const collectionTab = document.getElementById('collection-tab');
        const marketTab = document.getElementById('market-tab');
        
        if (collectionTab) {
            collectionTab.addEventListener('click', showCollection);
        }
        
        if (marketTab) {
            marketTab.addEventListener('click', showMarket);
        }
        
        // Market controls
        const chargerMarketBtn = document.getElementById('charger-market');
        if (chargerMarketBtn) {
            chargerMarketBtn.addEventListener('click', function(e) {
                e.preventDefault();
                loadMarket();
            });
        }
        
        const typeFilter = document.getElementById('type-filter');
        if (typeFilter) {
            typeFilter.addEventListener('change', filterMarket);
        }
        
        const priceFilter = document.getElementById('price-filter');
        if (priceFilter) {
            priceFilter.addEventListener('change', filterMarket);
        }
        
        console.log('Event listeners configurés');
    } catch (error) {
        console.error('Erreur lors de la configuration des event listeners:', error);
    }
}

// Afficher la section collection
function showCollection(event) {
    event.preventDefault();
    
    // Mettre à jour la navigation
    document.querySelectorAll('.user-nav a').forEach(a => a.classList.remove('active'));
    event.target.classList.add('active');
    
    // Afficher la section
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    document.getElementById('collection-section').classList.add('active');
    
    // Charger la collection (pour l'instant vide)
    loadUserCollection();
}

// Afficher la section market
function showMarket(event) {
    event.preventDefault();
    
    // Mettre à jour la navigation
    document.querySelectorAll('.user-nav a').forEach(a => a.classList.remove('active'));
    event.target.classList.add('active');
    
    // Afficher la section
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    document.getElementById('market-section').classList.add('active');
    
    // Charger le market personnalisé si pas déjà fait et pas en cours de chargement
    if (marketCartes.length === 0 && !isMarketLoading) {
        console.log('Première ouverture du market, chargement...');
        loadMarket();
    }
}

// Charger la collection utilisateur depuis le serveur
async function loadUserCollection() {
    try {
        // Charger les statistiques
        const statsResponse = await fetch(`/api/users/${currentUser.id}/collection/stats`);
        if (statsResponse.ok) {
            const stats = await statsResponse.json();
            
            const totalCartesEl = document.getElementById('total-cartes');
            const typesUniquesEl = document.getElementById('types-uniques');
            const valeurCollectionEl = document.getElementById('valeur-collection');
            
            if (totalCartesEl) totalCartesEl.textContent = stats.totalCartes;
            if (typesUniquesEl) typesUniquesEl.textContent = stats.typesUniques;
            if (valeurCollectionEl) valeurCollectionEl.textContent = stats.valeurTotale;
        }
        
        // Charger la collection
        const collectionResponse = await fetch(`/api/users/${currentUser.id}/collection`);
        if (collectionResponse.ok) {
            userCollection = await collectionResponse.json();
            displayUserCollection(userCollection);
        } else {
            throw new Error('Erreur lors du chargement de la collection');
        }
        
    } catch (error) {
        console.error('Erreur lors du chargement de la collection:', error);
        const container = document.getElementById('ma-collection');
        if (container) {
            container.innerHTML = `
                <div class="error-message">
                    <h3>⚠️ Erreur</h3>
                    <p>Impossible de charger votre collection.</p>
                </div>
            `;
        }
    }
}

// Afficher la collection de l'utilisateur
function displayUserCollection(collection) {
    const container = document.getElementById('ma-collection');
    if (!container) return;
    
    if (collection.length === 0) {
        container.innerHTML = `
            <div class="empty-collection">
                <h3>🎯 Collection vide</h3>
                <p>Vous n'avez pas encore de cartes dans votre collection.</p>
                <p>Rendez-vous sur le <strong>Market</strong> pour acheter vos premières cartes !</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = '';
    collection.forEach(carte => {
        const carteElement = createCollectionCarteElement(carte);
        container.appendChild(carteElement);
    });
}

// Créer l'élément HTML pour une carte de la collection
function createCollectionCarteElement(carte) {
    const carteDiv = document.createElement('div');
    carteDiv.className = 'carte collection-carte';
    
    // Déterminer le sprite à afficher
    let spriteUrl = '/images/pokemon-default.png';
    if (carte.spriteFront) {
        spriteUrl = carte.spriteFront;
    } else if (carte.spriteBack) {
        spriteUrl = carte.spriteBack;
    }
    
    // Créer les badges de types
    let typesBadges = '';
    if (carte.types && carte.types.length > 0) {
        const typesArray = Array.isArray(carte.types) ? carte.types : carte.types.split(',');
        typesBadges = typesArray.map(type => 
            `<span class="type-badge type-${type.trim().toLowerCase()}">${type.trim()}</span>`
        ).join('');
    }
    
    carteDiv.innerHTML = `
        <div class="carte-header">
            <h3 class="carte-nom">${carte.nom || 'Nom inconnu'}</h3>
            ${carte.pokemonId ? `<div class="carte-id">#${carte.pokemonId}</div>` : ''}
            ${carte.niveau ? `<div class="carte-niveau">Niveau ${carte.niveau}</div>` : ''}
        </div>
        
        <div class="carte-sprite">
            <img src="${spriteUrl}" 
                 alt="${carte.nom}" 
                 class="sprite-img"
                 onerror="this.src='/images/pokemon-default.png'">
        </div>
        
        <div class="carte-info">
            ${typesBadges ? `<div class="carte-types">${typesBadges}</div>` : ''}
            
            <div class="carte-stats">
                ${carte.cout !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Coût</div>
                        <div class="stat-value">⭐ ${carte.cout}</div>
                    </div>
                ` : ''}
                ${carte.hp !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">PV</div>
                        <div class="stat-value">❤️ ${carte.hp}</div>
                    </div>
                ` : ''}
                ${carte.attaque !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Attaque</div>
                        <div class="stat-value">⚔️ ${carte.attaque}</div>
                    </div>
                ` : ''}
                ${carte.defense !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Défense</div>
                        <div class="stat-value">🛡️ ${carte.defense}</div>
                    </div>
                ` : ''}
            </div>
            
            <div class="owned-badge">
                ✅ Possédée
            </div>
        </div>
    `;
    
    return carteDiv;
}

// Charger le market personnalisé pour l'utilisateur
async function loadMarket() {
    if (isMarketLoading) {
        console.log('Market déjà en cours de chargement...');
        return;
    }
    
    isMarketLoading = true;
    const container = document.getElementById('market-cartes');
    
    if (!container) {
        console.error('Container market-cartes non trouvé');
        isMarketLoading = false;
        return;
    }
    
    container.innerHTML = '<div class="loading">Chargement du market...</div>';
    
    try {
        console.log('Chargement du market personnalisé...');
        // Utiliser l'endpoint qui exclut les cartes déjà possédées
        const response = await fetch(`/api/users/${currentUser.id}/market`);
        if (!response.ok) {
            throw new Error(`Erreur HTTP: ${response.status}`);
        }
        
        marketCartes = await response.json();
        console.log(`${marketCartes.length} cartes disponibles au market`);
        displayMarketCartes(marketCartes);
        
    } catch (error) {
        console.error('Erreur lors du chargement du market:', error);
        container.innerHTML = '<div class="error-message">Erreur lors du chargement du market</div>';
    } finally {
        isMarketLoading = false;
    }
}

// Afficher les cartes du market
function displayMarketCartes(cartes) {
    const container = document.getElementById('market-cartes');
    container.innerHTML = '';
    
    if (cartes.length === 0) {
        container.innerHTML = '<div class="loading">Aucune carte disponible</div>';
        return;
    }
    
    cartes.forEach(carte => {
        const carteElement = createMarketCarteElement(carte);
        container.appendChild(carteElement);
    });
}

// Créer l'élément HTML pour une carte du market
function createMarketCarteElement(carte) {
    const carteDiv = document.createElement('div');
    carteDiv.className = 'carte market-carte';
    
    // Déterminer le sprite à afficher
    let spriteUrl = '/images/pokemon-default.png';
    if (carte.spriteFront) {
        spriteUrl = carte.spriteFront;
    } else if (carte.spriteBack) {
        spriteUrl = carte.spriteBack;
    }
    
    // Créer les badges de types
    let typesBadges = '';
    if (carte.types && carte.types.length > 0) {
        const typesArray = Array.isArray(carte.types) ? carte.types : carte.types.split(',');
        typesBadges = typesArray.map(type => 
            `<span class="type-badge type-${type.trim().toLowerCase()}">${type.trim()}</span>`
        ).join('');
    }
    
    // Prix de la carte (coût * 10 pour le market)
    const prix = (carte.cout || 1) * 10;
    const peutAcheter = currentUser.argent >= prix;
    
    carteDiv.innerHTML = `
        <div class="carte-header">
            <h3 class="carte-nom">${carte.nom || 'Nom inconnu'}</h3>
            ${carte.pokemonId ? `<div class="carte-id">#${carte.pokemonId}</div>` : ''}
            ${carte.niveau ? `<div class="carte-niveau">Niveau ${carte.niveau}</div>` : ''}
        </div>
        
        <div class="carte-sprite">
            <img src="${spriteUrl}" 
                 alt="${carte.nom}" 
                 class="sprite-img"
                 onerror="this.src='/images/pokemon-default.png'">
        </div>
        
        <div class="carte-info">
            ${typesBadges ? `<div class="carte-types">${typesBadges}</div>` : ''}
            
            <div class="carte-price">
                <div class="price-value">💰 ${prix} pièces</div>
            </div>
            
            <div class="carte-stats">
                ${carte.cout !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Coût</div>
                        <div class="stat-value">⭐ ${carte.cout}</div>
                    </div>
                ` : ''}
                ${carte.hp !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">PV</div>
                        <div class="stat-value">❤️ ${carte.hp}</div>
                    </div>
                ` : ''}
                ${carte.attaque !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Attaque</div>
                        <div class="stat-value">⚔️ ${carte.attaque}</div>
                    </div>
                ` : ''}
                ${carte.defense !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Défense</div>
                        <div class="stat-value">🛡️ ${carte.defense}</div>
                    </div>
                ` : ''}
            </div>
            
            <button class="buy-button" 
                    onclick="buyCard(${carte.id}, ${prix})"
                    ${!peutAcheter ? 'disabled' : ''}>
                ${peutAcheter ? '🛒 Acheter' : '💸 Pas assez d\'argent'}
            </button>
        </div>
    `;
    
    return carteDiv;
}

// Acheter une carte
async function buyCard(carteId, prix) {
    if (currentUser.argent < prix) {
        showMessage('Vous n\'avez pas assez d\'argent !', 'error');
        return;
    }
    
    try {
        console.log(`Achat de la carte ${carteId} pour ${prix} pièces...`);
        
        const response = await fetch(`/api/users/${currentUser.id}/acheter/${carteId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // Mettre à jour les données utilisateur
            currentUser = result.user;
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            
            // Mettre à jour l'affichage de l'argent
            document.getElementById('argent').textContent = currentUser.argent;
            
            showMessage(result.message || 'Carte achetée avec succès !', 'success');
            
            // Recharger le market pour exclure la carte achetée
            setTimeout(() => {
                loadMarket();
            }, 1000);
            
            // Si on est sur l'onglet collection, recharger aussi la collection
            const collectionSection = document.getElementById('collection-section');
            if (collectionSection && collectionSection.classList.contains('active')) {
                setTimeout(() => {
                    loadUserCollection();
                }, 1000);
            }
            
        } else {
            showMessage(result.error || 'Erreur lors de l\'achat', 'error');
        }
        
    } catch (error) {
        console.error('Erreur lors de l\'achat:', error);
        showMessage('Erreur lors de l\'achat', 'error');
    }
}

// Filtrer le market
function filterMarket() {
    const typeFilter = document.getElementById('type-filter').value;
    const priceFilter = document.getElementById('price-filter').value;
    
    let filteredCartes = [...marketCartes];
    
    // Filtre par type
    if (typeFilter) {
        filteredCartes = filteredCartes.filter(carte => 
            carte.type && carte.type.toLowerCase() === typeFilter.toLowerCase()
        );
    }
    
    // Filtre par prix
    if (priceFilter) {
        filteredCartes = filteredCartes.filter(carte => {
            const prix = (carte.cout || 1) * 10;
            switch (priceFilter) {
                case '0-5':
                    return prix <= 50;
                case '6-10':
                    return prix > 50 && prix <= 100;
                case '11+':
                    return prix > 100;
                default:
                    return true;
            }
        });
    }
    
    displayMarketCartes(filteredCartes);
}

// Afficher un message
function showMessage(text, type) {
    // Supprimer les anciens messages
    const existingMessages = document.querySelectorAll('.success-message, .error-message');
    existingMessages.forEach(msg => msg.remove());
    
    // Créer le nouveau message
    const messageDiv = document.createElement('div');
    messageDiv.className = `${type}-message`;
    messageDiv.textContent = text;
    document.body.appendChild(messageDiv);
    
    // Supprimer le message après 3 secondes
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}
