package com.cct.cctgo.repository;

import com.cct.cctgo.entite.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoueurRepository extends JpaRepository<Joueur, Integer> {

    public Joueur findJoueurById(int id);

    public Joueur findJoueurByNom(String nom);
}
