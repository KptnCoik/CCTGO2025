package com.cct.cctgo.entite;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Pronostic", schema = "public")
public class Pronostic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    Epreuve epreuve;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "prono_mapping", joinColumns = @JoinColumn(name = "pronostics_id"))
    @MapKeyColumn(name = "cle")
    @Column(name = "valeur")
    Map<Integer, Integer> prono = new LinkedHashMap<>();
}
