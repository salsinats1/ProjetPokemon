package com.example.demo.pokemon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartes")
public class Pokemon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String type;
    
    // Pokémon specific fields
    @Column(name = "pokemon_id")
    private Integer pokemonId;
    
    @Column(name = "type_secondaire")
    private String typeSecondaire;
    
    private Integer cout;
    private Integer attaque;
    private Integer defense;
    private Integer hp;
    private Integer vitesse;
    private Integer niveau;
    
    // Pokémon sprites
    @Column(name = "sprite_front")
    private String spriteFront;
    
    @Column(name = "sprite_back")
    private String spriteBack;
    
    @Column(name = "sprite_shiny")
    private String spriteShiny;
    
    // Legacy field (deprecated but kept for compatibility)
    @Column(name = "image_url")
    private String imageUrl;

    // Constructeurs
    public Pokemon() {}

    public Pokemon(String nom, String description, String type, Integer cout, Integer attaque, Integer defense, String imageUrl) {
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.cout = cout;
        this.attaque = attaque;
        this.defense = defense;
        this.imageUrl = imageUrl;
    }
    
    // Constructor for Pokemon
    public Pokemon(Integer pokemonId, String nom, String description, String type, String typeSecondaire, 
                 Integer hp, Integer attaque, Integer defense, Integer vitesse, 
                 String spriteFront, String spriteBack, String spriteShiny) {
        this.pokemonId = pokemonId;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.typeSecondaire = typeSecondaire;
        this.hp = hp;
        this.attaque = attaque;
        this.defense = defense;
        this.vitesse = vitesse;
        this.spriteFront = spriteFront;
        this.spriteBack = spriteBack;
        this.spriteShiny = spriteShiny;
    }
    
    // Constructor for Pokemon with cost
    public Pokemon(Integer pokemonId, String nom, String description, String type, String typeSecondaire, 
                 Integer cout, Integer hp, Integer attaque, Integer defense, Integer vitesse, Integer niveau,
                 String spriteFront, String spriteBack, String spriteShiny) {
        this.pokemonId = pokemonId;
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.typeSecondaire = typeSecondaire;
        this.cout = cout;
        this.hp = hp;
        this.attaque = attaque;
        this.defense = defense;
        this.vitesse = vitesse;
        this.niveau = niveau;
        this.spriteFront = spriteFront;
        this.spriteBack = spriteBack;
        this.spriteShiny = spriteShiny;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getCout() { return cout; }
    public void setCout(Integer cout) { this.cout = cout; }

    public Integer getAttaque() { return attaque; }
    public void setAttaque(Integer attaque) { this.attaque = attaque; }

    public Integer getDefense() { return defense; }
    public void setDefense(Integer defense) { this.defense = defense; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    // Pokémon specific getters and setters
    public Integer getPokemonId() { return pokemonId; }
    public void setPokemonId(Integer pokemonId) { this.pokemonId = pokemonId; }
    
    public String getTypeSecondaire() { return typeSecondaire; }
    public void setTypeSecondaire(String typeSecondaire) { this.typeSecondaire = typeSecondaire; }
    
    public Integer getHp() { return hp; }
    public void setHp(Integer hp) { this.hp = hp; }
    
    public Integer getVitesse() { return vitesse; }
    public void setVitesse(Integer vitesse) { this.vitesse = vitesse; }
    
    public Integer getNiveau() { return niveau; }
    public void setNiveau(Integer niveau) { this.niveau = niveau; }
    
    public String getSpriteFront() { return spriteFront; }
    public void setSpriteFront(String spriteFront) { this.spriteFront = spriteFront; }
    
    public String getSpriteBack() { return spriteBack; }
    public void setSpriteBack(String spriteBack) { this.spriteBack = spriteBack; }
    
    public String getSpriteShiny() { return spriteShiny; }
    public void setSpriteShiny(String spriteShiny) { this.spriteShiny = spriteShiny; }
}
