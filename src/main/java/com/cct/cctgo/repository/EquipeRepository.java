package com.cct.cctgo.repository;

import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.entite.Equipe;
import com.cct.cctgo.entite.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe,Integer> {
    public Equipe findEquipeById(int id);

    public Equipe findEquipeByEpreuveAndJoueursContains(Epreuve epreuve, Joueur joueur);

    public List<Equipe> findEquipeByEpreuve(Epreuve epreuve);

    @Query("SELECT eq.joueurs from Equipe eq WHERE eq.id = :id")
    public List<Joueur> getIdJoueursByEquipe(int id);

}
