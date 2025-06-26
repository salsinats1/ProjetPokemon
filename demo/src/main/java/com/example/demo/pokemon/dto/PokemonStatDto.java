package com.example.demo.pokemon.dto;

public class PokemonStatDto {
    private Integer base_stat;
    private StatDto stat;
    
    // Constructeurs
    public PokemonStatDto() {}
    
    // Getters et Setters
    public Integer getBase_stat() { return base_stat; }
    public void setBase_stat(Integer base_stat) { this.base_stat = base_stat; }
    
    public StatDto getStat() { return stat; }
    public void setStat(StatDto stat) { this.stat = stat; }
}
