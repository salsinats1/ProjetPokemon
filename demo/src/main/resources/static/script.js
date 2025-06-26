// Configuration de l'API
const API_BASE_URL = '/api/cartes';

// Variables globales
let cartesChargees = [];

// Initialisation de la page
document.addEventListener('DOMContentLoaded', function() {
    chargerCartes();
    
    // Event listeners pour les boutons
    document.getElementById('chargerPokemons').addEventListener('click', chargerNouveauxPokemons);
    document.getElementById('rafraichir').addEventListener('click', rafraichirCartes);
});

// Charger toutes les cartes existantes
async function chargerCartes() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE_URL}`);
        if (!response.ok) {
            throw new Error(`Erreur HTTP: ${response.status}`);
        }
        
        const cartes = await response.json();
        cartesChargees = cartes;
        afficherCartes(cartes);
        
    } catch (error) {
        console.error('Erreur lors du chargement des cartes:', error);
        afficherErreur('Erreur lors du chargement des cartes: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Charger de nouveaux Pokémons depuis l'API
async function chargerNouveauxPokemons() {
    showLoading(true);
    const debut = cartesChargees.length + 1;
    const fin = debut + 9; // Charger 10 nouveaux Pokémons
    
    try {
        const response = await fetch(`${API_BASE_URL}/charger-pokemons?debut=${debut}&fin=${fin}`, {
            method: 'POST'
        });
        
        if (!response.ok) {
            throw new Error(`Erreur HTTP: ${response.status}`);
        }
        
        // Recharger toutes les cartes après l'ajout
        await chargerCartes();
        
    } catch (error) {
        console.error('Erreur lors du chargement des nouveaux Pokémons:', error);
        afficherErreur('Erreur lors du chargement des nouveaux Pokémons: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Rafraîchir la liste des cartes
async function rafraichirCartes() {
    await chargerCartes();
}

// Afficher les cartes dans la grille
function afficherCartes(cartes) {
    const container = document.getElementById('cartes-container');
    container.innerHTML = '';
    
    if (cartes.length === 0) {
        container.innerHTML = '<div class="error-message">Aucune carte trouvée. Cliquez sur "Charger plus de Pokémons" pour commencer.</div>';
        return;
    }
    
    cartes.forEach(carte => {
        const carteElement = creerElementCarte(carte);
        container.appendChild(carteElement);
    });
}

// Créer l'élément HTML pour une carte
function creerElementCarte(carte) {
    const carteDiv = document.createElement('div');
    carteDiv.className = 'carte';
    
    // Déterminer le sprite à afficher (front_default en priorité)
    let spriteUrl = '/images/pokemon-default.png'; // Image par défaut
    if (carte.spriteFront) {
        spriteUrl = carte.spriteFront;
    } else if (carte.spriteBack) {
        spriteUrl = carte.spriteBack;
    } else if (carte.spriteShinyFront) {
        spriteUrl = carte.spriteShinyFront;
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
                ${carte.vitesse !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">Vitesse</div>
                        <div class="stat-value">💨 ${carte.vitesse}</div>
                    </div>
                ` : ''}
                ${carte.pointsVie !== undefined ? `
                    <div class="stat-item">
                        <div class="stat-label">PV</div>
                        <div class="stat-value">${carte.pointsVie}</div>
                    </div>
                ` : ''}
            </div>
        </div>
    `;
    
    return carteDiv;
}

// Afficher/masquer le loading
function showLoading(show) {
    const loadingElement = document.getElementById('loading');
    loadingElement.style.display = show ? 'block' : 'none';
}

// Afficher un message d'erreur
function afficherErreur(message) {
    const container = document.getElementById('cartes-container');
    container.innerHTML = `<div class="error-message">${message}</div>`;
}

// Fonction utilitaire pour capitaliser la première lettre
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
