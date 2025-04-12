package com.cct.cctgo.Service;

import com.cct.cctgo.entite.*;
import com.cct.cctgo.repository.EquipeRepository;
import com.cct.cctgo.repository.JoueurRepository;
import com.cct.cctgo.utilitaire.TypeEpreuve;
import com.cct.cctgo.utilitaire.TypeProno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PronosticEquipeService {

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    EquipeRepository equipeRepository;

    private final static int[] pointsA4 = {0,3,6,10,15,30,30};
    private final static int[] pointsA6 = {0,3,8,20,20};
    private final static int[] pointsA8 = {0,3,10,10};

    public Integer calculateurPointProno(ClassementEquipeDTO classement, Pronostic pronostic, Epreuve epreuve) {
        Integer point = 0;
        if(epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) || epreuve.getTypeEpreuve().equals(TypeEpreuve.A3)) {
            point = calculateurPointPronoA2A3(classement, pronostic, epreuve);
        } else {
            point = calculateurPointPronoA4A6A8(classement, pronostic, epreuve);
        }

        return point;
    }

    private Integer calculateurPointPronoA4A6A8(ClassementEquipeDTO classement, Pronostic pronostic, Epreuve epreuve) {
        Integer point = 0;
        int pronoExactHaut = getNbrPronoExactA4A6A8(classement,pronostic, epreuve);

        switch (epreuve.getTypeEpreuve()) {
            case A4 -> point = pointsA4[pronoExactHaut];
            case A6 -> point = pointsA6[pronoExactHaut];
            case A8 -> point = pointsA8[pronoExactHaut];
        }
        return point;
    }

    private Integer calculateurPointPronoA2A3(ClassementEquipeDTO classement, Pronostic pronostic, Epreuve epreuve) {
        Integer point = 0;
        int pronoExactHaut = getNbrPronoExactA2A3(classement,pronostic,TypeProno.ClassementHaut, epreuve);
        int pronoExactBas = getNbrPronoExactA2A3(classement,pronostic,TypeProno.ClassementBas, epreuve);
        int  pronoDesordreHaut = getNbrPronoDesordreA2A3(classement,pronostic,TypeProno.ClassementHaut, epreuve);
        int  pronoDesordreBas = getNbrPronoDesordreA2A3(classement,pronostic,TypeProno.ClassementBas, epreuve);

        point = calculateurPointBonusPronoA2A3(pronoExactHaut,pronoDesordreHaut,point,epreuve);
        point = calculateurPointBonusPronoA2A3(pronoExactBas,pronoDesordreBas,point,epreuve);

        return point;
    }

    private int getNbrPronoExactA2A3(ClassementEquipeDTO classement, Pronostic prono, TypeProno typeProno, Epreuve epreuve) {
        int bonProno = 0;
        int borneInf = typeProno.equals(TypeProno.ClassementHaut) ? 0 : epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 9 : 5;
        int borneSup= typeProno.equals(TypeProno.ClassementHaut) ? 3 : epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 12 : 8 ;
        for(int i=borneInf; i<borneSup; i++) {
            int equipe = classement.getEquipe().get(i).getId();
            int position = classement.getEquipe().get(i).getPosition();
            if(prono.getProno().containsKey(position)) {
                int equipePronop = prono.getProno().get(position);
                if(equipe==equipePronop) {
                    bonProno++;
                }
            }
        }
        return bonProno;
    }

    private int getNbrPronoExactA4A6A8(ClassementEquipeDTO classement, Pronostic prono, Epreuve epreuve) {
        int bonProno = 0;
        int borneSup= epreuve.getTypeEpreuve().equals(TypeEpreuve.A4) ? 6 : epreuve.getTypeEpreuve().equals(TypeEpreuve.A6) ? 4 : 3;
        for(int i=0; i<borneSup; i++) {
            int equipe = classement.getEquipe().get(i).getId();
            int position = classement.getEquipe().get(i).getPosition();
            if(prono.getProno().containsKey(position)) {
                int equipePronop = prono.getProno().get(position);
                if(equipe==equipePronop) {
                    bonProno++;
                }
            }
        }
        return bonProno;
    }

    private int getNbrPronoDesordreA2A3(ClassementEquipeDTO classement, Pronostic prono, TypeProno typeProno, Epreuve epreuve) {
        int bonProno = 0;
        Map<Integer, Integer> pronoType = new HashMap<>();
        int borneInf = typeProno.equals(TypeProno.ClassementHaut) ? 0 : 3;
        int borneSup = typeProno.equals(TypeProno.ClassementHaut) ? 4 : 8;
        int count = 1;
        for (Map.Entry<Integer, Integer> entry : prono.getProno().entrySet()) {
            if (count > borneInf && count < borneSup) {
                pronoType.put(entry.getKey(), entry.getValue());
                count++;
            } else {
                count++;
            }
        }
        int borneInfProno = typeProno.equals(TypeProno.ClassementHaut) ? 0 : epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 9 : 5 ;
        int borneSupProno = typeProno.equals(TypeProno.ClassementHaut) ? 3 : epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 12 : 8 ;
        for(int i=borneInfProno; i<borneSupProno; i++) {
            int equipe = classement.getEquipe().get(i).getId();
            int position = classement.getEquipe().get(i).getPosition();
            if(pronoType.containsValue(equipe)) {
                int equipeProno = prono.getProno().get(position);
                if(equipe!=equipeProno) {
                    bonProno++;
                }
            }
        }
        return bonProno;
    }

    private int  calculateurPointBonusPronoA2A3(int pronoExact, int pronoDesordre,int point, Epreuve epreuve) {
        if(pronoExact==3) {
            point += epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 30 : 20;
        } else {
            point += calculPointExactEtDesordre(pronoExact,pronoDesordre);
            switch (pronoExact + pronoDesordre) {
                case 2 ->  {point +=5;}
                case  3 -> {point += epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 15 : 10;}
            }
        }
        return point;
    }


    private Integer calculPointExactEtDesordre(int pronoExact,int pronoDesordre) {
        Integer point=0;
        point += pronoExact > 0 ? pronoExact * 2 : 0;
        point += pronoDesordre > 0 ? pronoDesordre : 0;
        return point;
    }

    public PronoEquipeDTO getPronoEquipe(String joueur, String epreuve) {
        Pronostic pronostic= joueurRepository.findJoueurByNom(joueur).getPronostics().stream().filter(x->x.getEpreuve().getNom().equals(epreuve)).findFirst().orElse(null);
        PronoEquipeDTO pronoEquipeDTO = new PronoEquipeDTO();
        Map<Integer, Set<String>> pronoType = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : pronostic.getProno().entrySet()) {
            Set<String> equipeList = new HashSet<>();
            Equipe equipe = equipeRepository.findEquipeById(entry.getValue());
            for(Joueur j : equipe.getJoueurs()) {
                equipeList.add(j.getNom());
            }
            pronoType.put(entry.getKey(),equipeList);
        }
        pronoEquipeDTO.setEpreuve(pronostic.getEpreuve());
        pronoEquipeDTO.setPronoEquipe(pronoType);
        return pronoEquipeDTO;
    }
}
