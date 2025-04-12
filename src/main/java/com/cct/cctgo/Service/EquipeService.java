package com.cct.cctgo.Service;

import com.cct.cctgo.entite.*;
import com.cct.cctgo.repository.EpreuveRepository;
import com.cct.cctgo.repository.EquipeRepository;
import com.cct.cctgo.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EquipeService {

    @Autowired
    EquipeRepository equipeRepository;

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    EpreuveRepository epreuveRepository;

    public Equipe addEquipe(String nomJoueur, String nomEpreuve) {
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        Equipe equipe = new Equipe();
        List<Joueur> listJoueur = new ArrayList<Joueur>();
        listJoueur.add(joueur);
        equipe.setEpreuve(epreuve);
        equipe.setJoueurs(listJoueur);
        return equipeRepository.save(equipe);
    }

    public void addJoueurEquipe(String nomJoueur, String nomNouveauJoueur, String nomEpreuve) {
        Equipe equipe = equipeRepository.findEquipeByEpreuveAndJoueursContains(epreuveRepository.findEpreuveByNom(nomEpreuve),
                joueurRepository.findJoueurByNom(nomJoueur));
        equipe.getJoueurs().add(joueurRepository.findJoueurByNom(nomNouveauJoueur));


    }

    public void addEquipe(String nomEpreuve, String... equipe){
        addEquipe(equipe[0],nomEpreuve);
        for(int i=1;i< equipe.length;i++){
            addJoueurEquipe(equipe[0],equipe[i],nomEpreuve);
        }
    }

    public List<String> addEquipeFoot(String... equipe){
        addEquipe(equipe[0],"foot");
        for(int i=1;i< equipe.length;i++){
            addJoueurEquipe(equipe[0],equipe[i],"foot");
        }
        return null;
    }

    public void addJoueurPourEquipe() {

    }
    public void addEquipeBinome(String nomEpreuve, String... equipe){
        addEquipe(equipe[0],nomEpreuve);
        addJoueurEquipe(equipe[0],equipe[1],nomEpreuve);
    }

    public void addEquipeDeDix(String nomEpreuve, String... equipe){
        addEquipe(equipe[0],nomEpreuve);
        for(int i=1;i< equipe.length;i++){
            addJoueurEquipe(equipe[0],equipe[i],nomEpreuve);
        }
    }

    public List<EquipeDTO> getEquipes(String nomEpreuve){
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        List<Equipe> equipes = equipeRepository.findEquipeByEpreuve(epreuve);
        for(Equipe e : equipes) {
            List<Joueur> listJoueur = new ArrayList<>();
            List<Joueur> joueurs = equipeRepository.getIdJoueursByEquipe(e.getId());
            e.setJoueurs(joueurs);

        }
        List<EquipeDTO> equipeDTOS = new ArrayList<>();
        for(Equipe equipe : equipes) {
            EquipeDTO equipeDTO = new EquipeDTO();
            List<JoueurDTO> joueurDTOS = new ArrayList<>();
            for(Joueur joueur : equipe.getJoueurs()){
                JoueurDTO joueurDTO = new JoueurDTO(joueur.getId(),joueur.getNom(),0,0,joueur.getBonus().getNom());
                joueurDTOS.add(joueurDTO);
            }
            equipeDTO.setId(equipe.getId());
            equipeDTO.setJoueurs(joueurDTOS);
            equipeDTOS.add(equipeDTO);
        }
        return equipeDTOS;
    }
}
