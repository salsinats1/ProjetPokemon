package com.example.demo.pokemon.dto;

public class PokemonAbilityDto {
    private AbilityDto ability;
    private boolean is_hidden;
    private Integer slot;
    
    // Constructeurs
    public PokemonAbilityDto() {}
    
    // Getters et Setters
    public AbilityDto getAbility() { return ability; }
    public void setAbility(AbilityDto ability) { this.ability = ability; }
    
    public boolean isIs_hidden() { return is_hidden; }
    public void setIs_hidden(boolean is_hidden) { this.is_hidden = is_hidden; }
    
    public Integer getSlot() { return slot; }
    public void setSlot(Integer slot) { this.slot = slot; }
}
