package com.cct.cctgo.repository;

import com.cct.cctgo.entite.ClassementEquipe;

import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.entite.Equipe;
import com.cct.cctgo.entite.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassementEquipeRepository extends JpaRepository<ClassementEquipe,Integer> {

    @Query("SELECT c from ClassementEquipe c WHERE c.epreuve = :epreuve and c.equipe = :equipe")
    public ClassementEquipe findClassementEquipeByEpreuveAndJoueurContains(Epreuve epreuve, Equipe equipe);

    public List<ClassementEquipe> findClassementEquipeByEpreuve(Epreuve epreuve);
}
