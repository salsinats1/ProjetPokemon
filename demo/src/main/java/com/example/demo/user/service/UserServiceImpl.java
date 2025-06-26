package com.example.demo.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    @Override
    public UserDto inscrireUtilisateur(String username, String email, String password) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Cette adresse email est déjà utilisée");
        }
        
        // TODO: Hasher le mot de passe (pour l'instant on le stocke en clair)
        User user = userMapper.createEntityFromDto(username, email, password);
        user.setArgent(1000); // Argent de départ
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    @Override
    public Optional<UserDto> connecterUtilisateur(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // TODO: Vérifier le mot de passe hashé (pour l'instant on compare en clair)
            if (user.getPassword().equals(password) && user.getIsActive()) {
                return Optional.of(userMapper.toDto(user));
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<UserDto> getUtilisateurById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }
    
    @Override
    public Optional<UserDto> getUtilisateurByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }
    
    @Override
    public UserDto mettreAJourArgent(Long userId, Integer nouvelArgent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (nouvelArgent < 0) {
            throw new RuntimeException("L'argent ne peut pas être négatif");
        }
        
        user.setArgent(nouvelArgent);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    @Override
    public UserDto debiterArgent(Long userId, Integer montant) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (montant <= 0) {
            throw new RuntimeException("Le montant à débiter doit être positif");
        }
        
        if (user.getArgent() < montant) {
            throw new RuntimeException("Solde insuffisant");
        }
        
        user.setArgent(user.getArgent() - montant);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    @Override
    public UserDto crediterArgent(Long userId, Integer montant) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (montant <= 0) {
            throw new RuntimeException("Le montant à créditer doit être positif");
        }
        
        user.setArgent(user.getArgent() + montant);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    @Override
    public boolean aAssezDArgent(Long userId, Integer montant) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return user.getArgent() >= montant;
    }
}
