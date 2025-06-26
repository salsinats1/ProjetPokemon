package com.example.demo.pokemon.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.pokemon.dto.PokemonDto;

public interface PokemonService {
    
    /**
     * Récupérer tous les Pokémons
     */
    List<PokemonDto> getAllPokemons();
    
    /**
     * Récupérer un Pokémon par son ID
     */
    Optional<PokemonDto> getPokemonById(Long id);
    
    /**
     * Récupérer des Pokémons par type
     */
    List<PokemonDto> getPokemonsByType(String type);
    
    /**
     * Rechercher des Pokémons par nom
     */
    List<PokemonDto> searchPokemonsByName(String nom);
    
    /**
     * Récupérer des Pokémons abordables (coût <= coutMax)
     */
    List<PokemonDto> getPokemonsAbordables(Integer coutMax);
    
    /**
     * Sauvegarder un Pokémon
     */
    PokemonDto savePokemon(PokemonDto pokemonDto);
    
    /**
     * Charger des Pokémons depuis l'API PokéAPI
     */
    void chargerPokemonsFromApi(int debut, int fin);
    
    /**
     * Initialiser les données de base
     */
    void initData();
}
