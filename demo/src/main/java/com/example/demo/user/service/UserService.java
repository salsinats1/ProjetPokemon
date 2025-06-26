package com.example.demo.user.service;

import java.util.Optional;

import com.example.demo.user.dto.UserDto;

public interface UserService {
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    UserDto inscrireUtilisateur(String username, String email, String password);
    
    /**
     * Connexion d'un utilisateur
     */
    Optional<UserDto> connecterUtilisateur(String username, String password);
    
    /**
     * Récupérer un utilisateur par son ID
     */
    Optional<UserDto> getUtilisateurById(Long id);
    
    /**
     * Récupérer un utilisateur par son username
     */
    Optional<UserDto> getUtilisateurByUsername(String username);
    
    /**
     * Mettre à jour l'argent d'un utilisateur
     */
    UserDto mettreAJourArgent(Long userId, Integer nouvelArgent);
    
    /**
     * Débiter l'argent d'un utilisateur
     */
    UserDto debiterArgent(Long userId, Integer montant);
    
    /**
     * Créditer l'argent d'un utilisateur
     */
    UserDto crediterArgent(Long userId, Integer montant);
    
    /**
     * Vérifier si un utilisateur a assez d'argent
     */
    boolean aAssezDArgent(Long userId, Integer montant);
}
