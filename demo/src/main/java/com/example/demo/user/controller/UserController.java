package com.example.demo.user.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.dto.UserDto;
import com.example.demo.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            
            if (username == null || email == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Tous les champs sont obligatoires"));
            }
            
            UserDto user = userService.inscrireUtilisateur(username, email, password);
            return ResponseEntity.ok(Map.of(
                "message", "Inscription réussie !",
                "user", user
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Connexion d'un utilisateur
     */
    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Nom d'utilisateur et mot de passe obligatoires"));
        }
        
        Optional<UserDto> userOpt = userService.connecterUtilisateur(username, password);
        
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(Map.of(
                "message", "Connexion réussie !",
                "user", userOpt.get()
            ));
        } else {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Nom d'utilisateur ou mot de passe incorrect"));
        }
    }
    
    /**
     * Récupérer les informations d'un utilisateur
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUtilisateur(@PathVariable Long id) {
        Optional<UserDto> userOpt = userService.getUtilisateurById(id);
        
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Mettre à jour l'argent d'un utilisateur
     */
    @PutMapping("/{id}/argent")
    public ResponseEntity<?> mettreAJourArgent(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        try {
            Integer nouvelArgent = request.get("argent");
            if (nouvelArgent == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Le montant d'argent est obligatoire"));
            }
            
            UserDto user = userService.mettreAJourArgent(id, nouvelArgent);
            return ResponseEntity.ok(Map.of(
                "message", "Argent mis à jour avec succès",
                "user", user
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
