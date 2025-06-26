package com.example.demo.market.entity;

import java.time.LocalDateTime;

import com.example.demo.pokemon.entity.Pokemon;
import com.example.demo.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_cartes")
public class UserPokemon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carte_id", nullable = false)
    private Pokemon pokemon;
    
    @Column(name = "date_achat")
    private LocalDateTime dateAchat;
    
    @Column(name = "prix_achat")
    private Integer prixAchat;
    
    // Constructeurs
    public UserPokemon() {
        this.dateAchat = LocalDateTime.now();
    }
    
    public UserPokemon(User user, Pokemon pokemon, Integer prixAchat) {
        this();
        this.user = user;
        this.pokemon = pokemon;
        this.prixAchat = prixAchat;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Pokemon getPokemon() { return pokemon; }
    public void setPokemon(Pokemon pokemon) { this.pokemon = pokemon; }
    
    public LocalDateTime getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDateTime dateAchat) { this.dateAchat = dateAchat; }
    
    public Integer getPrixAchat() { return prixAchat; }
    public void setPrixAchat(Integer prixAchat) { this.prixAchat = prixAchat; }
}
