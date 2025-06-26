package com.example.demo.pokemon.dto;

import java.util.List;

public class PokemonApiDto {
    private Integer id;
    private String name;
    private Integer height;
    private Integer weight;
    private Integer base_experience;
    private List<PokemonTypeDto> types;
    private List<PokemonStatDto> stats;
    private PokemonSpritesDto sprites;
    private List<PokemonAbilityDto> abilities;
    
    // Constructeurs
    public PokemonApiDto() {}
    
    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
    
    public Integer getBase_experience() { return base_experience; }
    public void setBase_experience(Integer base_experience) { this.base_experience = base_experience; }
    
    public List<PokemonTypeDto> getTypes() { return types; }
    public void setTypes(List<PokemonTypeDto> types) { this.types = types; }
    
    public List<PokemonStatDto> getStats() { return stats; }
    public void setStats(List<PokemonStatDto> stats) { this.stats = stats; }
    
    public PokemonSpritesDto getSprites() { return sprites; }
    public void setSprites(PokemonSpritesDto sprites) { this.sprites = sprites; }
    
    public List<PokemonAbilityDto> getAbilities() { return abilities; }
    public void setAbilities(List<PokemonAbilityDto> abilities) { this.abilities = abilities; }
}
