package com.example.demo.market.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.market.service.MarketService;
import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.user.dto.UserDto;

@RestController
@RequestMapping("/api/market")
public class MarketController {
    
    private final MarketService marketService;
    
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }
    
    /**
     * Acheter un Pokémon
     */
    @PostMapping("/users/{userId}/acheter/{pokemonId}")
    public ResponseEntity<?> acheterPokemon(@PathVariable Long userId, @PathVariable Long pokemonId) {
        try {
            UserDto user = marketService.acheterPokemon(userId, pokemonId);
            return ResponseEntity.ok(Map.of(
                "message", "Pokémon acheté avec succès !",
                "user", user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Récupérer la collection de l'utilisateur
     */
    @GetMapping("/users/{userId}/collection")
    public ResponseEntity<List<PokemonDto>> getCollection(@PathVariable Long userId) {
        List<PokemonDto> collection = marketService.getCollectionUtilisateur(userId);
        return ResponseEntity.ok(collection);
    }
    
    /**
     * Récupérer les statistiques de collection de l'utilisateur
     */
    @GetMapping("/users/{userId}/collection/stats")
    public ResponseEntity<MarketService.CollectionStats> getCollectionStats(@PathVariable Long userId) {
        MarketService.CollectionStats stats = marketService.getStatsCollection(userId);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Récupérer le market filtré pour l'utilisateur (Pokémons non possédés)
     */
    @GetMapping("/users/{userId}/available")
    public ResponseEntity<List<PokemonDto>> getMarketPourUtilisateur(@PathVariable Long userId) {
        List<PokemonDto> pokemonsDisponibles = marketService.getPokemonsMarketPourUtilisateur(userId);
        return ResponseEntity.ok(pokemonsDisponibles);
    }
    
    /**
     * Calculer le prix d'un Pokémon
     */
    @GetMapping("/pokemon/{pokemonId}/prix")
    public ResponseEntity<?> getPrixPokemon(@PathVariable Long pokemonId) {
        try {
            Integer prix = marketService.calculerPrixPokemon(pokemonId);
            return ResponseEntity.ok(Map.of("prix", prix));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Vérifier si un utilisateur possède déjà un Pokémon
     */
    @GetMapping("/users/{userId}/possede/{pokemonId}")
    public ResponseEntity<Map<String, Boolean>> utilisateurPossedePokemon(@PathVariable Long userId, @PathVariable Long pokemonId) {
        boolean possede = marketService.utilisateurPossedePokemon(userId, pokemonId);
        return ResponseEntity.ok(Map.of("possede", possede));
    }
}
