package com.cct.cctgo.entite;

import com.cct.cctgo.utilitaire.TypeEpreuve;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Epreuve", schema = "public")
public class Epreuve {

    public Epreuve(String nom) {
        this.nom = nom;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column
    String nom;

    @Column
    TypeEpreuve typeEpreuve;

    @Transient
    String name;

    String icon;

    String tooltip;

    boolean hasProno;

    boolean hasChefEquipe;
}
