package com.example.demo.pokemon.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.pokemon.dto.PokemonApiDto;
import com.example.demo.pokemon.dto.PokemonStatDto;
import com.example.demo.pokemon.entity.Pokemon;

@Service
public class PokemonApiService {
    
    private final RestTemplate restTemplate;
    private final String POKEMON_API_BASE_URL = "https://pokeapi.co/api/v2";
    
    public PokemonApiService() {
        this.restTemplate = new RestTemplate();
    }
    
    public PokemonApiDto getPokemonById(Integer id) {
        String url = POKEMON_API_BASE_URL + "/pokemon/" + id;
        try {
            System.out.println("Fetching Pokemon from: " + url);
            PokemonApiDto result = restTemplate.getForObject(url, PokemonApiDto.class);
            System.out.println("Successfully fetched Pokemon: " + (result != null ? result.getName() : "null"));
            return result;
        } catch (Exception e) {
            System.err.println("Error fetching Pokemon " + id + ": " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération du Pokémon : " + id, e);
        }
    }
    
    public PokemonApiDto getPokemonByName(String name) {
        String url = POKEMON_API_BASE_URL + "/pokemon/" + name.toLowerCase();
        try {
            System.out.println("Fetching Pokemon from: " + url);
            PokemonApiDto result = restTemplate.getForObject(url, PokemonApiDto.class);
            System.out.println("Successfully fetched Pokemon: " + (result != null ? result.getName() : "null"));
            return result;
        } catch (Exception e) {
            System.err.println("Error fetching Pokemon " + name + ": " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération du Pokémon : " + name, e);
        }
    }
    
    public Optional<Pokemon> getPokemonFromApi(Integer id) {
        try {
            PokemonApiDto pokemonApi = getPokemonById(id);
            if (pokemonApi == null) {
                return Optional.empty();
            }
            
            // Mapper les données de l'API vers notre entité Pokemon
            Pokemon pokemon = new Pokemon();
            pokemon.setPokemonId(pokemonApi.getId());
            pokemon.setNom(capitalizeFirstLetter(pokemonApi.getName()));
            pokemon.setDescription("Pokémon de type " + getMainType(pokemonApi));
            
            // Types
            pokemon.setType(getMainType(pokemonApi));
            if (pokemonApi.getTypes().size() > 1) {
                pokemon.setTypeSecondaire(pokemonApi.getTypes().get(1).getType().getName());
            }
            
            // Stats
            for (PokemonStatDto stat : pokemonApi.getStats()) {
                String statName = stat.getStat().getName();
                switch (statName) {
                    case "hp":
                        pokemon.setHp(stat.getBase_stat());
                        break;
                    case "attack":
                        pokemon.setAttaque(stat.getBase_stat());
                        break;
                    case "defense":
                        pokemon.setDefense(stat.getBase_stat());
                        break;
                    case "speed":
                        pokemon.setVitesse(stat.getBase_stat());
                        break;
                }
            }
            
            // Sprites
            if (pokemonApi.getSprites() != null) {
                pokemon.setSpriteFront(pokemonApi.getSprites().getFront_default());
                pokemon.setSpriteBack(pokemonApi.getSprites().getBack_default());
                pokemon.setSpriteShiny(pokemonApi.getSprites().getFront_shiny());
            }
            
            return Optional.of(pokemon);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du Pokémon " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }
    
    private String getMainType(PokemonApiDto pokemonApi) {
        if (pokemonApi.getTypes() != null && !pokemonApi.getTypes().isEmpty()) {
            return pokemonApi.getTypes().get(0).getType().getName();
        }
        return "unknown";
    }
    
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
