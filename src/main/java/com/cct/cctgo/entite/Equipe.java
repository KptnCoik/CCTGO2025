package com.cct.cctgo.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Equipe", schema = "public")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    List<Joueur> joueurs;

    @ManyToOne
    @JsonIgnore
    Epreuve epreuve;

}
