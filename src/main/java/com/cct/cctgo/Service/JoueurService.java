package com.cct.cctgo.Service;

import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.entite.Joueur;
import com.cct.cctgo.entite.Pronostic;
import com.cct.cctgo.repository.EpreuveRepository;
import com.cct.cctgo.repository.JoueurRepository;
import com.cct.cctgo.utilitaire.TypeEpreuve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JoueurService {

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    EpreuveRepository epreuveRepository;

    @Autowired
    ClassementIndivService classementIndivService;

    @Autowired
    ClassementEquipeService classementEquipeService;

    public Joueur saveJoueur(String nom) {
        return joueurRepository.save(new Joueur(nom));
    }

    public void addBonus(String nomJoueur,String nomEpreuve) {
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        joueurRepository.findJoueurByNom(nomJoueur).setBonus(epreuve);
    }

    public void addMalus(String nomJoueur,String nomEpreuve) {
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        joueurRepository.findJoueurByNom(nomJoueur).setMalus(epreuve);
    }

    public List<Joueur> getListeJoueur() { return joueurRepository.findAll();}

    public List<Joueur> getAll() {
        List<Joueur> list = joueurRepository.findAll();
        for(Joueur joueur : list){
            joueur.setPointTotal(getPointTotal(joueur.getNom()));
        }
        Collections.sort(list, new Comparator<Joueur>() {
            public int compare(Joueur p1, Joueur p2) {
                return Float.valueOf(p2.getPointTotal()).compareTo(p1.getPointTotal());
            }
        });
        for(int i=0;i< list.size();i++){
            if(i==0){
                list.get(i).setPosition(1);
            } else if(list.get(i).getPointTotal()==list.get(i-1).getPointTotal()){
                list.get(i).setPosition(list.get(i-1).getPosition());
            } else {
                list.get(i).setPosition(i+1);
            }
        }
        return list;
    }

    public float getPointTotal(String nomJoueur) {
        List<Epreuve> epreuves = epreuveRepository.findAll();
        float pointTotal = 0;
        for(Epreuve epreuve : epreuves){
            if(epreuve.getTypeEpreuve().equals(TypeEpreuve.INDIVIDUELLE)){
                pointTotal += getPointIndiv(nomJoueur,epreuve);
            } else {
                pointTotal += getPointEquipe(nomJoueur,epreuve);
            }
        }
        return pointTotal;
    }

    public float getPointIndiv(String nomJoueur,Epreuve epreuve){
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        return classementIndivService.getPointIndiv(joueur, epreuve);
    }

    public float getPointEquipe(String nomJoueur,Epreuve epreuve){
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        return classementEquipeService.getPointEquipe(joueur,epreuve);
    }

    public float getPointEpreuve(String joueur, String nomEpreuve) {
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        float point =0;
        if(epreuve.getTypeEpreuve()==TypeEpreuve.INDIVIDUELLE){
            point = getPointIndiv(joueur,epreuve);
        } else {
            point = getPointEquipe(joueur, epreuve);
        }
        return point;
    }

    public int getPostionEpreuve(String nomJoueur, String nomEpreuve){
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        if(epreuve.getTypeEpreuve()==TypeEpreuve.INDIVIDUELLE){
            return classementIndivService.getClassementIndiv(joueur,epreuve);
        } else {
            return classementEquipeService.getPosition(joueur,epreuve);
        }
    }

    public void setProno(String joueur, Pronostic prono) {
        Joueur j = joueurRepository.findJoueurByNom(joueur);
        if(j.getPronostics() == null){
            Set<Pronostic> listProno = new HashSet<>();
            listProno.add(prono);
            j.setPronostics(listProno);
        } else {
            j.getPronostics().add(prono);
        }
    }
}
