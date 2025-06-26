package com.example.demo.pokemon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.pokemon.entity.Pokemon;
import com.example.demo.pokemon.mapper.PokemonMapper;
import com.example.demo.pokemon.repository.PokemonRepository;

import jakarta.annotation.PostConstruct;

@Service
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokemonMapper pokemonMapper;
    private final PokemonApiService pokemonApiService;
    private final Random random = new Random();

    public PokemonServiceImpl(PokemonRepository pokemonRepository, PokemonMapper pokemonMapper, 
                             PokemonApiService pokemonApiService) {
        this.pokemonRepository = pokemonRepository;
        this.pokemonMapper = pokemonMapper;
        this.pokemonApiService = pokemonApiService;
    }

    @PostConstruct
    @Override
    public void initData() {
        // Charger des Pokémons au démarrage uniquement
        if (pokemonRepository.count() == 0) {
            // Charger quelques Pokémon populaires
            chargerPokemonsFromApi(1, 10); // Bulbasaur à Caterpie
        }
    }

    @Override
    public List<PokemonDto> getAllPokemons() {
        List<Pokemon> pokemons = pokemonRepository.findAll();
        return pokemonMapper.toDtoList(pokemons);
    }

    @Override
    public Optional<PokemonDto> getPokemonById(Long id) {
        return pokemonRepository.findById(id)
                .map(pokemonMapper::toDto);
    }

    @Override
    public List<PokemonDto> getPokemonsByType(String type) {
        List<Pokemon> pokemons = pokemonRepository.findByTypeIgnoreCase(type);
        return pokemonMapper.toDtoList(pokemons);
    }

    @Override
    public List<PokemonDto> searchPokemonsByName(String nom) {
        List<Pokemon> pokemons = pokemonRepository.findByNomContainingIgnoreCase(nom);
        return pokemonMapper.toDtoList(pokemons);
    }

    @Override
    public List<PokemonDto> getPokemonsAbordables(Integer coutMax) {
        List<Pokemon> pokemons = pokemonRepository.findByPokemonAbordables(coutMax);
        return pokemonMapper.toDtoList(pokemons);
    }

    @Override
    public PokemonDto savePokemon(PokemonDto pokemonDto) {
        Pokemon pokemon = pokemonMapper.toEntity(pokemonDto);
        Pokemon savedPokemon = pokemonRepository.save(pokemon);
        return pokemonMapper.toDto(savedPokemon);
    }

    @Override
    public void chargerPokemonsFromApi(int debut, int fin) {
        List<Pokemon> nouveauxPokemons = new ArrayList<>();
        
        for (int i = debut; i <= fin; i++) {
            try {
                Optional<Pokemon> pokemonOpt = pokemonApiService.getPokemonFromApi(i);
                if (pokemonOpt.isPresent()) {
                    Pokemon pokemon = pokemonOpt.get();
                    
                    // Générer coût et niveau aléatoirement
                    pokemon.setCout(random.nextInt(10) + 1); // Entre 1 et 10
                    pokemon.setNiveau(random.nextInt(50) + 1); // Entre 1 et 50
                    
                    nouveauxPokemons.add(pokemon);
                    
                    if (nouveauxPokemons.size() >= 10) {
                        pokemonRepository.saveAll(nouveauxPokemons);
                        nouveauxPokemons.clear();
                        System.out.println("Batch de 10 Pokémons sauvegardé...");
                    }
                }
                
                // Délai pour éviter de surcharger l'API
                Thread.sleep(100);
                
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du Pokémon " + i + ": " + e.getMessage());
            }
        }
        
        // Sauvegarder le dernier batch
        if (!nouveauxPokemons.isEmpty()) {
            pokemonRepository.saveAll(nouveauxPokemons);
            System.out.println("Dernier batch de " + nouveauxPokemons.size() + " Pokémons sauvegardé.");
        }
        
        System.out.println("Chargement terminé ! Total: " + pokemonRepository.count() + " Pokémons en base.");
    }
}
