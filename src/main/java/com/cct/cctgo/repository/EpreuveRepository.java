package com.cct.cctgo.repository;

import com.cct.cctgo.entite.Epreuve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpreuveRepository extends JpaRepository<Epreuve,Integer> {

    public Epreuve findEpreuveById(int id);

    public Epreuve findEpreuveByNom(String nom);

}
