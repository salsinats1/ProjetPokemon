package com.example.demo.pokemon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.pokemon.entity.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    
    List<Pokemon> findByTypeIgnoreCase(String type);
    
    List<Pokemon> findByNomContainingIgnoreCase(String nom);
    
    @Query("SELECT p FROM Pokemon p WHERE p.cout <= :coutMax")
    List<Pokemon> findByPokemonAbordables(@Param("coutMax") Integer coutMax);
    
    List<Pokemon> findByPokemonId(Integer pokemonId);
    
    @Query("SELECT p FROM Pokemon p WHERE p.niveau BETWEEN :niveauMin AND :niveauMax")
    List<Pokemon> findByNiveauRange(@Param("niveauMin") Integer niveauMin, @Param("niveauMax") Integer niveauMax);
}
