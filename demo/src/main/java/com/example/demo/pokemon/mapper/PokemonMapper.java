package com.example.demo.pokemon.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.pokemon.entity.Pokemon;

@Component
public class PokemonMapper {

    public PokemonDto toDto(Pokemon pokemon) {
        if (pokemon == null) {
            return null;
        }
        
        // Si c'est un Pokémon (a pokemonId), utiliser le constructeur Pokémon
        if (pokemon.getPokemonId() != null) {
            return new PokemonDto(
                pokemon.getId(),
                pokemon.getPokemonId(),
                pokemon.getNom(),
                pokemon.getDescription(),
                pokemon.getType(),
                pokemon.getTypeSecondaire(),
                pokemon.getCout(),
                pokemon.getHp(),
                pokemon.getAttaque(),
                pokemon.getDefense(),
                pokemon.getVitesse(),
                pokemon.getNiveau(),
                pokemon.getSpriteFront(),
                pokemon.getSpriteBack(),
                pokemon.getSpriteShiny()
            );
        }
        
        // Sinon, utiliser l'ancien constructeur pour compatibilité
        return new PokemonDto(
            pokemon.getId(),
            pokemon.getNom(),
            pokemon.getDescription(),
            pokemon.getType(),
            pokemon.getCout(),
            pokemon.getAttaque(),
            pokemon.getDefense(),
            pokemon.getImageUrl()
        );
    }

    public Pokemon toEntity(PokemonDto pokemonDto) {
        if (pokemonDto == null) {
            return null;
        }
        
        Pokemon pokemon = new Pokemon(
            pokemonDto.getPokemonId(),
            pokemonDto.getNom(),
            pokemonDto.getDescription(),
            pokemonDto.getType(),
            pokemonDto.getTypeSecondaire(),
            pokemonDto.getCout(),
            pokemonDto.getHp(),
            pokemonDto.getAttaque(),
            pokemonDto.getDefense(),
            pokemonDto.getVitesse(),
            pokemonDto.getNiveau(),
            pokemonDto.getSpriteFront(),
            pokemonDto.getSpriteBack(),
            pokemonDto.getSpriteShiny()
        );
        pokemon.setId(pokemonDto.getId());
        return pokemon;
    }

    public List<PokemonDto> toDtoList(List<Pokemon> pokemons) {
        return pokemons.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
