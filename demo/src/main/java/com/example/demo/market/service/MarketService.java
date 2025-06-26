package com.example.demo.market.service;

import java.util.List;

import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.user.dto.UserDto;

public interface MarketService {
    
    /**
     * Acheter un Pokémon
     */
    UserDto acheterPokemon(Long userId, Long pokemonId);
    
    /**
     * Récupérer la collection d'un utilisateur
     */
    List<PokemonDto> getCollectionUtilisateur(Long userId);
    
    /**
     * Récupérer les statistiques de collection d'un utilisateur
     */
    CollectionStats getStatsCollection(Long userId);
    
    /**
     * Récupérer les Pokémons disponibles au market pour un utilisateur
     * (exclut les Pokémons qu'il possède déjà)
     */
    List<PokemonDto> getPokemonsMarketPourUtilisateur(Long userId);
    
    /**
     * Calculer le prix d'un Pokémon
     */
    Integer calculerPrixPokemon(Long pokemonId);
    
    /**
     * Vérifier si un utilisateur possède déjà un Pokémon
     */
    boolean utilisateurPossedePokemon(Long userId, Long pokemonId);
    
    /**
     * Classe pour les statistiques de collection
     */
    public static class CollectionStats {
        public final long totalPokemons;
        public final int typesUniques;
        public final int valeurTotale;
        
        public CollectionStats(long totalPokemons, int typesUniques, int valeurTotale) {
            this.totalPokemons = totalPokemons;
            this.typesUniques = typesUniques;
            this.valeurTotale = valeurTotale;
        }
        
        // Getters
        public long getTotalPokemons() { return totalPokemons; }
        public int getTypesUniques() { return typesUniques; }
        public int getValeurTotale() { return valeurTotale; }
    }
}
