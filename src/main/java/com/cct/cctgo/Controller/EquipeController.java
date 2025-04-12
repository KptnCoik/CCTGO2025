package com.cct.cctgo.Controller;

import com.cct.cctgo.Service.EquipeService;
import com.cct.cctgo.entite.Equipe;
import com.cct.cctgo.entite.EquipeDTO;
import com.cct.cctgo.entite.Joueur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("equipe")
@CrossOrigin(origins = "*")
public class EquipeController {

    @Autowired
    EquipeService equipeService;

    @Transactional
    @PostMapping("/addEquipe")
    public Equipe addEquipe(String nomJoueur, String nomEpreuve) {
        return equipeService.addEquipe(nomJoueur,nomEpreuve);
    }

    @Transactional
    @PostMapping("/addJoueurEquipe")
    public void addJoueurEquipe(String nomJoueur, String nouveauJoueur, String nomEpreuve){
         equipeService.addJoueurEquipe(nomJoueur,nouveauJoueur,nomEpreuve);
    }

    @Transactional
    @PostMapping("/addJoueurPourEquipe")
    public void addJoueurPourEquipe(String nomJoueur, String nouveauJoueur, String nomEpreuve){
        equipeService.addJoueurEquipe(nomJoueur,nouveauJoueur,nomEpreuve);
    }

    @Transactional
    @PostMapping("/addEquipeFoot")
    public List<String> addEquipeFoot(String... equipe){
        return equipeService.addEquipeFoot(equipe);
    }

    @Transactional
    @PostMapping("/addEquipeBinome")
    public void addEquipeBinome(String nomEpreuve,String... equipe){
        equipeService.addEquipeBinome(nomEpreuve,equipe);
    }

    @Transactional
    @PostMapping("/addEquipeComplete")
    public void addEquipe(String nomEpreuve,String... equipe){
        equipeService.addEquipe(nomEpreuve,equipe);
    }

    @GetMapping("/getEquipes")
    public List<EquipeDTO> getEquipe(String epreuve){
        return equipeService.getEquipes(epreuve);
    }

}
