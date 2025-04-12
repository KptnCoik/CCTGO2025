package com.cct.cctgo.Controller;

import com.cct.cctgo.Service.JoueurService;
import com.cct.cctgo.entite.Joueur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Joueur")
@CrossOrigin(origins = "*")
public class JoueurController {

    @Autowired
    JoueurService joueurService;

    @Transactional
    @PostMapping("/addJoueur")
    public Joueur addJoueur(String nom){
        return joueurService.saveJoueur(nom);
    }

    @Transactional
    @PostMapping("/addBonus")
    public void addBonus(String nomJoueur, String nomEpreuve) {
        joueurService.addBonus(nomJoueur,nomEpreuve);
    }

    @Transactional
    @PostMapping("/addMalus")
    public void addMalus(String nomJoueur, String nomEpreuve) {
        joueurService.addMalus(nomJoueur,nomEpreuve);
    }

    @GetMapping("/getAll")
    public List<Joueur> getAll(){
        return joueurService.getAll();
    }

    @GetMapping("/getPointTotal")
    public float getPointTotal(String joueur){
        return joueurService.getPointTotal(joueur);
    }

    @GetMapping("/getPointEpreuve")
    public float getPointEpreuve(String joueur, String epreuve) {return joueurService.getPointEpreuve(joueur, epreuve);}

    @GetMapping("/getPositionEpreuve")
    public int getPoitionEpreuve(String joueur, String epreuve) {return joueurService.getPostionEpreuve(joueur, epreuve);}

    @GetMapping("/getListeJoueur")
    public List<Joueur> getListeJoueur() {return joueurService.getListeJoueur();}
}
