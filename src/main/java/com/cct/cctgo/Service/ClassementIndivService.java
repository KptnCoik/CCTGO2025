package com.cct.cctgo.Service;

import com.cct.cctgo.entite.*;
import com.cct.cctgo.repository.ClassementIndivRepository;
import com.cct.cctgo.repository.EpreuveRepository;
import com.cct.cctgo.repository.EquipeRepository;
import com.cct.cctgo.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassementIndivService {
    @Autowired
    ClassementIndivRepository classementIndivRepository;
    @Autowired
    EpreuveRepository epreuveRepository;
    @Autowired
    JoueurRepository joueurRepository;
    @Autowired
    EquipeRepository equipeRepository;
    @Autowired
    CalculateurPoint calculator;
    @Autowired
    EpreuveService epreuveService;

    public void saveClassement(String nomJoueur,String nomEpreuve,int position) {
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        ClassementIndiv classement = new ClassementIndiv();
        classement.setJoueur(joueur);
        classement.setEpreuve(epreuve);
        classement.setPosition(position);
        joueur.setPointTotal(joueur.getPointTotal()+calculator.calculPoint(position,epreuve));
        classementIndivRepository.save(classement);
        joueurRepository.save(joueur);
    }

    public int getClassementIndiv(Joueur joueur,Epreuve epreuve){
        ClassementIndiv classement =  classementIndivRepository.findClassementIndivByEpreuveAndJoueurContains(epreuve,joueur);
        if(classement!=null){
            return classement.getPosition();
        } else {
            return 0;
        }
    }

    public float getPointIndiv(Joueur joueur, Epreuve epreuve){
        int position = getClassementIndiv(joueur, epreuve);
        if(position!=0) {
            float point = calculator.calculPoint(position, epreuve);
            /**
            if (joueur.getBonus().getId() == epreuve.getId()) {
                point = point * 2;
            } else if (joueur.getMalus().getId() == epreuve.getId()) {
                point = point / 2;
            }
            **/
             return joueur.getBonus().getId() == epreuve.getId() ? point * 2 : point;
        } else {
            return 0;
        }
    }

    public ClassementIndivDTO getListClassementIndiv(String nomEpreuve){
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        List<ClassementIndiv> listClassement = classementIndivRepository.findClassementIndivByEpreuve(epreuve);
        List<JoueurDTO> listJoueur = new ArrayList<>();
        for(ClassementIndiv classement : listClassement) {
            float point = getPointIndiv(classement.getJoueur(),epreuve);
            /**
             JoueurDTO joueurDTO = new JoueurDTO(classement.getJoueur().getNom(),point,classement.getPosition(),classement.getJoueur().getBonus().getNom(),classement.getJoueur().getMalus().getNom());
             */
            JoueurDTO joueurDTO = new JoueurDTO();
            joueurDTO.setNom(classement.getJoueur().getNom());
            joueurDTO.setPoint(point);
            joueurDTO.setPosition(classement.getPosition());
            joueurDTO.setId(classement.getJoueur().getId());
            joueurDTO.setBonus(classement.getJoueur().getBonus().getNom());
            listJoueur.add(joueurDTO);
        }
        Collections.sort(listJoueur, new Comparator<JoueurDTO>() {
            public int compare(JoueurDTO p1, JoueurDTO p2) {
                return Integer.valueOf(p1.getPosition()).compareTo(p2.getPosition());
            }
        });
        ClassementIndivDTO classementRetour = new ClassementIndivDTO();
        classementRetour.setJoueurs(listJoueur);
        classementRetour.setEpreuve(nomEpreuve);
        if(listJoueur.size()==24){
            classementRetour.setTermine(true);
            classementRetour.setMax(
                    classementRetour.getJoueurs().get(classementRetour.getJoueurs().size()-1).getPosition()
            );
            /**
            List<Joueur> listTotaleJoueur = joueurRepository.findAll();
            for(Joueur j : listTotaleJoueur) {
                j.setPointProno(j.getPointProno()+pronosticService.getPointProno(j.getNom(),nomEpreuve));
            }
             */

        } else {
            classementRetour.setTermine(false);
        }
        return classementRetour;
    }

    public List<ClassementIndivDTO> getAllClassementIndiv(List<Joueur> joueurs){
        List<ClassementIndivDTO> listClassement = new ArrayList<>();
        List<ClassementIndivDTO> listReturn = new ArrayList<>();
        List<Epreuve> listEpreuveIndiv = epreuveService.getIndividuelle();
        for(Epreuve epreuve : listEpreuveIndiv) {
            ClassementIndivDTO classementIndivDTO = getListClassementIndiv(epreuve.getNom());
            listClassement.add(classementIndivDTO);
        }
        listClassement = listClassement.stream().filter(x->x.isTermine()).toList();

            for(ClassementIndivDTO classementIndivDTO : listClassement){
                ClassementIndivDTO classementIndivDTO1 = new ClassementIndivDTO();
                classementIndivDTO1.setEpreuve(classementIndivDTO.getEpreuve());
                classementIndivDTO1.setTermine(classementIndivDTO.isTermine());
                List<JoueurDTO> joueurDTOS = new ArrayList<>();
                for(Joueur joueur : joueurs){
                    for(JoueurDTO joueurDTO : classementIndivDTO.getJoueurs()){
                        if(joueurDTO.getNom().equals(joueur.getNom())){
                            joueurDTOS.add(joueurDTO);
                        }
                    }
                    classementIndivDTO1.setJoueurs(joueurDTOS);
                    classementIndivDTO1.setMax(classementIndivDTO.getMax());
            }
                listReturn.add(classementIndivDTO1);
        }
        return listReturn;
    }
}
