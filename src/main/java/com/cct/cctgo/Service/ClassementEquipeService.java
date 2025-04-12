package com.cct.cctgo.Service;

import com.cct.cctgo.entite.*;
import com.cct.cctgo.repository.ClassementEquipeRepository;
import com.cct.cctgo.repository.EpreuveRepository;
import com.cct.cctgo.repository.EquipeRepository;
import com.cct.cctgo.repository.JoueurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassementEquipeService {
    @Autowired
    ClassementEquipeRepository classementEquipeRepository;
    @Autowired
    EquipeRepository equipeRepository;
    @Autowired
    EpreuveRepository epreuveRepository;
    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    EpreuveService epreuveService;

    @Autowired
    CalculateurPoint calculator;


    public void saveClassement(String nomJoueur, String nomEpreuve,int position) {
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        Equipe equipe = equipeRepository.findEquipeByEpreuveAndJoueursContains(
                epreuve,
                joueurRepository.findJoueurByNom(nomJoueur)
        );
        ClassementEquipe classement = new ClassementEquipe();
        classement.setEquipe(equipe);
        classement.setEpreuve(epreuve);
        classement.setPosition(position);
        classementEquipeRepository.save(classement);
    }

    public float getPointEquipe(Joueur joueur, Epreuve epreuve) {
        Equipe equipe = equipeRepository.findEquipeByEpreuveAndJoueursContains(epreuve,joueur);
        ClassementEquipe classement = classementEquipeRepository.findClassementEquipeByEpreuveAndJoueurContains(epreuve,equipe);
        int position = getPosition(joueur,epreuve);
        float point = 0;
        if(position!=0){
            point = calculator.calculPoint(classement.getPosition(), epreuve);

            return epreuve.getId() == joueur.getBonus().getId() ? point * 2 : point;
        } else {
            return 0;
        }

    }

    public int getPosition(Joueur joueur, Epreuve epreuve){
        Equipe equipe = equipeRepository.findEquipeByEpreuveAndJoueursContains(epreuve,joueur);
        ClassementEquipe classement = classementEquipeRepository.findClassementEquipeByEpreuveAndJoueurContains(epreuve,equipe);
        if(classement!=null){
            return classement.getPosition();
        } else {
            return 0;
        }
    }

    @Transactional
    public ClassementEquipeDTO getListClassementEquipe(String nomEpreuve){
        ClassementEquipeDTO classementReturn = new ClassementEquipeDTO();
        classementReturn.setEpreuve(nomEpreuve);
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        List<ClassementEquipe> listClassement = classementEquipeRepository.findClassementEquipeByEpreuve(epreuve);
        List<EquipeDTO> equipeDTOList = new ArrayList<>();
        List<JoueurDTO> listJoueur = new ArrayList<>();
        for(ClassementEquipe classement : listClassement) {
            ClassementEquipeDTO classementEquipeDTO = new ClassementEquipeDTO();
            EquipeDTO equipeDTO = new EquipeDTO();
            List<JoueurDTO> joueurDTOList = new ArrayList<>();
            for(Joueur joueur : classement.getEquipe().getJoueurs()){
                float point = getPointEquipe(joueur,epreuve);
                JoueurDTO joueurDTO = new JoueurDTO(joueur.getId(),joueur.getNom(),point,classement.getPosition(),joueur.getBonus().getNom());
                equipeDTO.setId(classement.getEquipe().getId());
                equipeDTO.setPosition(classement.getPosition());
                joueurDTOList.add(joueurDTO);

            }
            equipeDTO.setJoueurs(joueurDTOList);
            equipeDTOList.add(equipeDTO);
        }

        Collections.sort(equipeDTOList, new Comparator<EquipeDTO>() {
            public int compare(EquipeDTO e1, EquipeDTO e2) {
                return Integer.valueOf(e1.getPosition()).compareTo(e2.getPosition());
            }
        });
        classementReturn.setEquipe(equipeDTOList);
        switch(epreuve.getTypeEpreuve()){
            case A2 -> {
                if(classementReturn.getEquipe().size()==12){
                    classementReturn.setTermine(true);
                    classementReturn.setMax(12);

                } else {
                    classementReturn.setTermine(false);
                }
            }
            case A3 -> {
                if(classementReturn.getEquipe().size()==8){
                    classementReturn.setTermine(true);
                    classementReturn.setMax(8);

                } else {
                    classementReturn.setTermine(false);
                }
            }
            case A4 -> {
                if(classementReturn.getEquipe().size()==6){
                    classementReturn.setTermine(true);
                    classementReturn.setMax(6);

                } else {
                    classementReturn.setTermine(false);
                }
            }
            case A6 -> {
                if(classementReturn.getEquipe().size()==4){
                    classementReturn.setTermine(true);
                    classementReturn.setMax(4);
                } else {
                    classementReturn.setTermine(false);
                }
            }
            case A8 -> {
                if(classementReturn.getEquipe().size()==3){
                    classementReturn.setTermine(true);
                    classementReturn.setMax(3);
                } else {
                    classementReturn.setTermine(false);
                }
            }
            default -> classementReturn.setTermine(false);
        }

        return classementReturn;
    }

    @Transactional
    public List<ClassementIndivDTO> getAllClassementEquipe(List<Joueur> joueurs){
        List<Epreuve> epreuvesEquipe = epreuveService.getEpreuveEquipe();
        List<ClassementEquipeDTO> list = new ArrayList<>();
        List<ClassementIndivDTO> listReturn = new ArrayList<>();
        for(Epreuve epreuve : epreuvesEquipe){
            ClassementEquipeDTO classementEquipeDTO = getListClassementEquipe(epreuve.getNom());
            list.add(classementEquipeDTO);
        }
        list = list.stream().filter(x-> x.isTermine()).toList();
        for(ClassementEquipeDTO classement : list){
            ClassementIndivDTO classementIndivDTO1 = new ClassementIndivDTO();
            classementIndivDTO1.setEpreuve(classement.getEpreuve());
            classementIndivDTO1.setTermine(classement.isTermine());
            List<JoueurDTO> joueurDTOS = new ArrayList<>();
            for(Joueur joueur : joueurs){
                for(EquipeDTO equipeDTO : classement.getEquipe()){
                    for(JoueurDTO joueurDTO : equipeDTO.getJoueurs()){
                        if(joueurDTO.getNom().equals(joueur.getNom())){
                            joueurDTOS.add(joueurDTO);
                        }
                    }

                }
                classementIndivDTO1.setJoueurs(joueurDTOS);
                classementIndivDTO1.setMax(classement.getMax());
            }
            listReturn.add(classementIndivDTO1);
        }
    return listReturn;
    }
}
