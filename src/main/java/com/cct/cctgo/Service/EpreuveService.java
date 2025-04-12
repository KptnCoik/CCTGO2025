package com.cct.cctgo.Service;

import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.repository.EpreuveRepository;
import com.cct.cctgo.utilitaire.TypeEpreuve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpreuveService {

    @Autowired
    EpreuveRepository epreuveRepository;

    public Epreuve addEpreuve(String nom, int type, boolean hasProno,boolean hasChefEquipe) {
        Epreuve epreuve = new Epreuve(nom);
        epreuve.setTypeEpreuve(getCategorieEpreuve(type));
        epreuve.setHasProno(hasProno);
        epreuve.setHasChefEquipe(hasChefEquipe);
        return epreuveRepository.save(epreuve);
    }
    private TypeEpreuve getCategorieEpreuve(int categorie) {
        switch(categorie) {
            case 1 :
                return TypeEpreuve.INDIVIDUELLE;
            case 2 :
                return TypeEpreuve.A2;
            case 3 :
                return TypeEpreuve.A3;
            case 4 :
                return TypeEpreuve.A4;
            case 5 :
                return TypeEpreuve.A5;
            case 6 :
                return TypeEpreuve.A6;
            case 8 :
                return TypeEpreuve.A8;
            case 10 :
                return TypeEpreuve.A2E;
            default: return null;
        }
    }

    public List<Epreuve> getAll(){
        List<Epreuve> epreuves = epreuveRepository.findAll();
        for(Epreuve epreuve : epreuves){
            epreuve.setName(epreuve.getNom());
            epreuve.setTooltip(epreuve.getNom());
            epreuve.setIcon("fa-solid fa-house");
        }
        return epreuves;
    }

    public List<Epreuve> getIndividuelle(){
        return  getAll().stream().filter(x->x.getTypeEpreuve().equals(TypeEpreuve.INDIVIDUELLE)).toList();
    }
    public List<Epreuve> getEpreuveEquipe() {
        return  getAll().stream().filter(x->x.getTypeEpreuve()!=(TypeEpreuve.INDIVIDUELLE)).toList();
    }

    public List<Epreuve> getAllProno() {
        return getAll().stream().filter(Epreuve::isHasProno).toList();
    }
}
