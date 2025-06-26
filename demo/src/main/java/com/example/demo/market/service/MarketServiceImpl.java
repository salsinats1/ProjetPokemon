package com.example.demo.market.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.market.entity.UserPokemon;
import com.example.demo.market.repository.UserPokemonRepository;
import com.example.demo.pokemon.dto.PokemonDto;
import com.example.demo.pokemon.entity.Pokemon;
import com.example.demo.pokemon.mapper.PokemonMapper;
import com.example.demo.pokemon.repository.PokemonRepository;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;

@Service
public class MarketServiceImpl implements MarketService {
    
    private final UserPokemonRepository userPokemonRepository;
    private final PokemonRepository pokemonRepository;
    private final UserRepository userRepository;
    private final PokemonMapper pokemonMapper;
    private final UserService userService;
    
    public MarketServiceImpl(UserPokemonRepository userPokemonRepository,
                           PokemonRepository pokemonRepository,
                           UserRepository userRepository,
                           PokemonMapper pokemonMapper,
                           UserService userService) {
        this.userPokemonRepository = userPokemonRepository;
        this.pokemonRepository = pokemonRepository;
        this.userRepository = userRepository;
        this.pokemonMapper = pokemonMapper;
        this.userService = userService;
    }
    
    @Override
    public UserDto acheterPokemon(Long userId, Long pokemonId) {
        // Vérifier si l'utilisateur possède déjà ce Pokémon
        if (userPokemonRepository.existsByUserIdAndPokemonId(userId, pokemonId)) {
            throw new RuntimeException("Vous possédez déjà ce Pokémon");
        }
        
        // Récupérer l'utilisateur et le Pokémon
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokémon non trouvé"));
        
        // Calculer le prix
        Integer prix = calculerPrixPokemon(pokemonId);
        
        // Vérifier si l'utilisateur a assez d'argent
        if (!userService.aAssezDArgent(userId, prix)) {
            throw new RuntimeException("Vous n'avez pas assez d'argent");
        }
        
        // Débiter l'argent
        UserDto updatedUser = userService.debiterArgent(userId, prix);
        
        // Ajouter le Pokémon à la collection de l'utilisateur
        UserPokemon userPokemon = new UserPokemon(user, pokemon, prix);
        userPokemonRepository.save(userPokemon);
        
        return updatedUser;
    }
    
    @Override
    public List<PokemonDto> getCollectionUtilisateur(Long userId) {
        List<UserPokemon> userPokemons = userPokemonRepository.findByUserId(userId);
        return userPokemons.stream()
                .map(up -> pokemonMapper.toDto(up.getPokemon()))
                .collect(Collectors.toList());
    }
    
    @Override
    public CollectionStats getStatsCollection(Long userId) {
        long totalPokemons = userPokemonRepository.countByUserId(userId);
        Integer typesUniques = userPokemonRepository.getNombreTypesUniques(userId);
        Integer valeurTotale = userPokemonRepository.getValeurTotaleCollection(userId);
        
        return new CollectionStats(
            totalPokemons, 
            typesUniques != null ? typesUniques : 0, 
            valeurTotale != null ? valeurTotale : 0
        );
    }
    
    @Override
    public List<PokemonDto> getPokemonsMarketPourUtilisateur(Long userId) {
        List<Pokemon> tousPokemons = pokemonRepository.findAll();
        List<UserPokemon> pokemonsUtilisateur = userPokemonRepository.findByUserId(userId);
        
        // IDs des Pokémons que l'utilisateur possède déjà
        List<Long> pokemonsIdsUser = pokemonsUtilisateur.stream()
                .map(up -> up.getPokemon().getId())
                .collect(Collectors.toList());
        
        // Filtrer les Pokémons que l'utilisateur ne possède pas
        List<Pokemon> pokemonsDisponibles = tousPokemons.stream()
                .filter(pokemon -> !pokemonsIdsUser.contains(pokemon.getId()))
                .collect(Collectors.toList());
        
        return pokemonMapper.toDtoList(pokemonsDisponibles);
    }
    
    @Override
    public Integer calculerPrixPokemon(Long pokemonId) {
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokémon non trouvé"));
        
        // Calculer le prix (coût de base * 10)
        return (pokemon.getCout() != null ? pokemon.getCout() : 1) * 10;
    }
    
    @Override
    public boolean utilisateurPossedePokemon(Long userId, Long pokemonId) {
        return userPokemonRepository.existsByUserIdAndPokemonId(userId, pokemonId);
    }
}
