package com.cct.cctgo.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Joueur", schema = "public")
public class Joueur implements Serializable {

    private static final long serialVersionUID = 1L;

    public Joueur(String nom) {
        this.nom = nom;
        this.pointTotal=0;
        this.pointEpreuve=0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column
    String nom;

    @OneToOne
    Epreuve bonus;

    @OneToOne
    Epreuve malus;

    @Transient
    float pointEpreuve;


    float pointTotal=0;

    @Transient
    int position;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Pronostic> pronostics;


    int pointProno;

}
