package com.example.demo.pokemon.dto;

public class PokemonTypeDto {
    private Integer slot;
    private TypeDto type;
    
    // Constructeurs
    public PokemonTypeDto() {}
    
    // Getters et Setters
    public Integer getSlot() { return slot; }
    public void setSlot(Integer slot) { this.slot = slot; }
    
    public TypeDto getType() { return type; }
    public void setType(TypeDto type) { this.type = type; }
}
