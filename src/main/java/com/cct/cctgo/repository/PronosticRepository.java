package com.cct.cctgo.repository;

import com.cct.cctgo.entite.Pronostic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PronosticRepository extends JpaRepository<Pronostic,Integer> {
    void deleteById(Integer id);
}
