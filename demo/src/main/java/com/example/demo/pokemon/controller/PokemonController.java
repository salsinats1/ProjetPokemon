package com.example.demo.pokemon.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.pokemon.service.PokemonService;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {
    
    private final PokemonService pokemonService;
    
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }
    
    /**
     * Récupérer tous les Pokémons
     */
    @GetMapping
    public ResponseEntity<List<PokemonDto>> getAllPokemons() {
        List<PokemonDto> pokemons = pokemonService.getAllPokemons();
        return ResponseEntity.ok(pokemons);
    }
    
    /**
     * Récupérer un Pokémon par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PokemonDto> getPokemonById(@PathVariable Long id) {
        Optional<PokemonDto> pokemon = pokemonService.getPokemonById(id);
        return pokemon.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Rechercher des Pokémons par type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PokemonDto>> getPokemonsByType(@PathVariable String type) {
        List<PokemonDto> pokemons = pokemonService.getPokemonsByType(type);
        return ResponseEntity.ok(pokemons);
    }
    
    /**
     * Rechercher des Pokémons par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<PokemonDto>> searchPokemons(@RequestParam String nom) {
        List<PokemonDto> pokemons = pokemonService.searchPokemonsByName(nom);
        return ResponseEntity.ok(pokemons);
    }
    
    /**
     * Récupérer des Pokémons abordables
     */
    @GetMapping("/abordables")
    public ResponseEntity<List<PokemonDto>> getPokemonsAbordables(@RequestParam Integer coutMax) {
        List<PokemonDto> pokemons = pokemonService.getPokemonsAbordables(coutMax);
        return ResponseEntity.ok(pokemons);
    }
    
    /**
     * Charger des Pokémons depuis l'API (pour les administrateurs)
     */
    @PostMapping("/charger")
    public ResponseEntity<String> chargerPokemons(@RequestParam int debut, @RequestParam int fin) {
        try {
            pokemonService.chargerPokemonsFromApi(debut, fin);
            return ResponseEntity.ok("Pokémons chargés avec succès de " + debut + " à " + fin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors du chargement : " + e.getMessage());
        }
    }
}
