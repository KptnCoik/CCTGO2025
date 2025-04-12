package com.cct.cctgo.repository;

import com.cct.cctgo.entite.ClassementIndiv;
import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.entite.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassementIndivRepository extends JpaRepository<ClassementIndiv,Integer> {

    @Query("SELECT c from ClassementIndiv c WHERE c.epreuve = :epreuve and c.joueur = :joueur")
    public ClassementIndiv findClassementIndivByEpreuveAndJoueurContains(Epreuve epreuve, Joueur joueur);

    public List<ClassementIndiv> findClassementIndivByEpreuve(Epreuve epreuve);
}
