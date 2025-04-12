package com.cct.cctgo.Service;

import com.cct.cctgo.entite.ClassementIndivDTO;
import com.cct.cctgo.entite.PronoDTO;
import com.cct.cctgo.entite.Pronostic;
import com.cct.cctgo.repository.JoueurRepository;
import com.cct.cctgo.utilitaire.TypeProno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PronosticIndivService {

    @Autowired
    JoueurRepository joueurRepository;

    public Integer calculateurPointProno(ClassementIndivDTO classement, Pronostic prono) {
        Integer point =0;
        int pronoExactHaut = getNbrPronoExact(classement,prono, TypeProno.ClassementHaut);
        int pronoExactBas = getNbrPronoExact(classement,prono,TypeProno.ClassementBas);
        int pronoDesordreHaut = getNbrPronoDesordre(classement,prono,TypeProno.ClassementHaut);
        int pronoDesordreBas= getNbrPronoDesordre(classement,prono,TypeProno.ClassementBas);

        point = calculateurPointBonusProno(pronoExactHaut,pronoDesordreHaut,point);
        point = calculateurPointBonusProno(pronoExactBas,pronoDesordreBas,point);

        return point;
    }

    private int  calculateurPointBonusProno(int pronoExact,int pronoDesordre, int point) {
        if(pronoExact==5) {
            point += 50;
        } else {
            point += calculPointExactEtDesordre(pronoExact,pronoDesordre);
            switch (pronoDesordre+pronoExact) {
                case 5 -> {
                    point +=30;
                }
                case 4 -> {
                    point +=20;
                }
                case 3 -> {
                    point +=10;
                }
                default -> {

                }
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

    private int getNbrPronoExact(ClassementIndivDTO classement, Pronostic prono, TypeProno typeProno) {
        int bonProno = 0;
        int borneInf = typeProno.equals(TypeProno.ClassementHaut) ? 0 : 19;
        int borneSup= typeProno.equals(TypeProno.ClassementHaut) ? 5 : 24 ;
        for(int i=borneInf; i<borneSup; i++) {
            int joueur = classement.getJoueurs().get(i).getId();
            int position = classement.getJoueurs().get(i).getPosition();
            if(prono.getProno().containsKey(position)) {
                int joueurprono = prono.getProno().get(position);
                if(joueur==joueurprono) {
                    bonProno++;
                }
            }
        }
        return bonProno;
    }

    private int getNbrPronoDesordre(ClassementIndivDTO classement, Pronostic prono, TypeProno typeProno) {
        int bonProno = 0;
        Map<Integer, Integer> pronoType = new HashMap<>();
        int borneInf = typeProno.equals(TypeProno.ClassementHaut) ? 0 : 5;
        int borneSup = typeProno.equals(TypeProno.ClassementHaut) ? 6 : 11;
        int count = 1;
        for (Map.Entry<Integer, Integer> entry : prono.getProno().entrySet()) {
            if (count > borneInf && count < borneSup) {
                pronoType.put(entry.getKey(), entry.getValue());
                count++;
            } else {
                count++;
            }
        }
        int borneInfProno = typeProno.equals(TypeProno.ClassementHaut) ? 0 : 19;
        int borneSupProno = typeProno.equals(TypeProno.ClassementHaut) ? 5 : 24;
        for(int i=borneInfProno; i<borneSupProno; i++) {
            int joueur = classement.getJoueurs().get(i).getId();
            int position = classement.getJoueurs().get(i).getPosition();
            if(pronoType.containsValue(joueur)) {
                int joueurprono = prono.getProno().get(position);
                if(joueur!=joueurprono) {
                    bonProno++;
                }
            }
        }
        return bonProno;
    }

    public PronoDTO getPronoIndiv(String joueur, String epreuve) {
        Pronostic pronostic= joueurRepository.findJoueurByNom(joueur).getPronostics().stream().filter(x->x.getEpreuve().getNom().equals(epreuve)).findFirst().orElse(null);
        PronoDTO pronoDTO = new PronoDTO();
        Map<Integer, String> pronoType = new HashMap<>();
        pronoDTO.setEpreuve(pronostic.getEpreuve());
        for (Map.Entry<Integer, Integer> entry : pronostic.getProno().entrySet()) {
            pronoType.put(entry.getKey(),joueurRepository.findJoueurById(entry.getValue()).getNom());
        }
        pronoDTO.setProno(pronoType);
        return pronoDTO;
    }
}
