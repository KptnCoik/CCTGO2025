package com.cct.cctgo.utilitaire;

import com.cct.cctgo.entite.ClassementIndivDTO;
import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.entite.Pronostic;
import org.springframework.stereotype.Service;

@Service
public class PronosticUtil {

    public int getNbrPronoExact(ClassementIndivDTO classement, Pronostic prono, TypeProno typeProno, Epreuve epreuve) {
        int bonProno = 0;
        int borneInf = typeProno.equals(TypeProno.ClassementHaut) ? 0 :
                epreuve.getTypeEpreuve().equals(TypeEpreuve.INDIVIDUELLE) ? 19 :
                        epreuve.getTypeEpreuve().equals(TypeEpreuve.A2) ? 9 :
                                epreuve.getTypeEpreuve().equals(TypeEpreuve.A3) ? 5 : 0;
        int borneSup= typeProno.equals(TypeProno.ClassementHaut) ? 5 : 24 ;
        for(int i=borneInf; i<borneSup; i++) {
            int pronoClassement = classement.getJoueurs().get(i).getId();
            int position = classement.getJoueurs().get(i).getPosition();
            if(prono.getProno().containsKey(position)) {
                int pronoJoueur = prono.getProno().get(position);
                if(pronoClassement==pronoJoueur) {
                    bonProno++;
                }
            }
        }
        return bonProno;
    }
}
