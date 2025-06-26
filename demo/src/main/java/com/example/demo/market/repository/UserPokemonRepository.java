package com.example.demo.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.market.entity.UserPokemon;

@Repository
public interface UserPokemonRepository extends JpaRepository<UserPokemon, Long> {
    
    /**
     * Récupérer tous les Pokémons d'un utilisateur
     */
    List<UserPokemon> findByUserId(Long userId);
    
    /**
     * Vérifier si un utilisateur possède déjà un Pokémon spécifique
     */
    boolean existsByUserIdAndPokemonId(Long userId, Long pokemonId);
    
    /**
     * Compter le nombre de Pokémons d'un utilisateur
     */
    long countByUserId(Long userId);
    
    /**
     * Récupérer la valeur totale de la collection d'un utilisateur
     */
    @Query("SELECT COALESCE(SUM(up.prixAchat), 0) FROM UserPokemon up WHERE up.user.id = :userId")
    Integer getValeurTotaleCollection(@Param("userId") Long userId);
    
    /**
     * Récupérer le nombre de types uniques dans la collection d'un utilisateur
     */
    @Query("SELECT COUNT(DISTINCT p.type) FROM UserPokemon up JOIN up.pokemon p WHERE up.user.id = :userId")
    Integer getNombreTypesUniques(@Param("userId") Long userId);
    
    /**
     * Récupérer les Pokémons les plus chers de la collection d'un utilisateur
     */
    @Query("SELECT up FROM UserPokemon up WHERE up.user.id = :userId ORDER BY up.prixAchat DESC")
    List<UserPokemon> findTopExpensivePokemonsByUser(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);
    
    /**
     * Récupérer la collection par type de Pokémon
     */
    @Query("SELECT up FROM UserPokemon up JOIN up.pokemon p WHERE up.user.id = :userId AND p.type = :type")
    List<UserPokemon> findByUserIdAndPokemonType(@Param("userId") Long userId, @Param("type") String type);
}
