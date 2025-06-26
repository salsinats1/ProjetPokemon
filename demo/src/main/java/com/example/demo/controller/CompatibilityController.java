package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.market.service.MarketService;
import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.pokemon.service.PokemonService;
import com.example.demo.user.dto.UserDto;

/**
 * Controller de compatibilité pour maintenir les anciennes routes
 * Redirige vers les nouveaux services organisés par domaine
 */
@RestController
@RequestMapping("/api")
public class CompatibilityController {
    
    private final PokemonService pokemonService;
    private final MarketService marketService;
    
    public CompatibilityController(PokemonService pokemonService, MarketService marketService) {
        this.pokemonService = pokemonService;
        this.marketService = marketService;
    }
    
    /**
     * Ancienne route pour récupérer toutes les cartes -> redirige vers Pokémons
     */
    @GetMapping("/cartes")
    public ResponseEntity<List<PokemonDto>> getAllCartes() {
        List<PokemonDto> pokemons = pokemonService.getAllPokemons();
        return ResponseEntity.ok(pokemons);
    }
    
    /**
     * Ancienne route pour charger de nouveaux Pokémons
     */
    @PostMapping("/cartes/charger-pokemons")
    public ResponseEntity<String> chargerNouveauxPokemons(@RequestParam int debut, @RequestParam int fin) {
        try {
            pokemonService.chargerPokemonsFromApi(debut, fin);
            return ResponseEntity.ok("Pokémons chargés avec succès de " + debut + " à " + fin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors du chargement : " + e.getMessage());
        }
    }
    
    /**
     * Anciennes routes utilisateur avec achat/collection
     */
    @PostMapping("/users/{userId}/acheter/{carteId}")
    public ResponseEntity<?> acheterCarte(@PathVariable Long userId, @PathVariable Long carteId) {
        try {
            UserDto user = marketService.acheterPokemon(userId, carteId);
            return ResponseEntity.ok(Map.of(
                "message", "Carte achetée avec succès !",
                "user", user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/users/{userId}/collection")
    public ResponseEntity<List<PokemonDto>> getCollection(@PathVariable Long userId) {
        List<PokemonDto> collection = marketService.getCollectionUtilisateur(userId);
        return ResponseEntity.ok(collection);
    }
    
    @GetMapping("/users/{userId}/collection/stats")
    public ResponseEntity<MarketService.CollectionStats> getCollectionStats(@PathVariable Long userId) {
        MarketService.CollectionStats stats = marketService.getStatsCollection(userId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users/{userId}/market")
    public ResponseEntity<List<PokemonDto>> getMarketPourUtilisateur(@PathVariable Long userId) {
        List<PokemonDto> pokemonsDisponibles = marketService.getPokemonsMarketPourUtilisateur(userId);
        return ResponseEntity.ok(pokemonsDisponibles);
    }
}
