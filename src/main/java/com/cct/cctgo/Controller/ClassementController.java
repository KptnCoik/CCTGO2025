package com.cct.cctgo.Controller;

import com.cct.cctgo.Service.ClassementEquipeService;
import com.cct.cctgo.Service.ClassementIndivService;
import com.cct.cctgo.Service.EpreuveService;
import com.cct.cctgo.entite.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("Classement")
@CrossOrigin(origins = "*")
public class ClassementController {
    @Autowired
    ClassementIndivService classementIndivService;

    @Autowired
    ClassementEquipeService classementEquipeService;

    @Autowired
    EpreuveService epreuveService;

    @Autowired
    JoueurController joueurController;

    @Transactional
    @PostMapping("/individuel")
    public void addClassementIndiv(String joueur,String epreuve,int position){
        classementIndivService.saveClassement(joueur, epreuve, position);
    }

    @Transactional
    @PostMapping("/equipe")
    public void addClassementEquipe(String joueur,String epreuve,int position){
        classementEquipeService.saveClassement(joueur, epreuve, position);
    }

    @GetMapping("/getClassementIndiv")
    public ClassementIndivDTO getClassementIndiv(String epreuve){
        return classementIndivService.getListClassementIndiv(epreuve);
    }

    @GetMapping("/getClassementEquipe")
    public ClassementEquipeDTO getClassementEquipe(String epreuve){
        return classementEquipeService.getListClassementEquipe(epreuve);
    }

    @GetMapping("/getAllClassementIndiv")
    public List<ClassementIndivDTO> getListClassementIndiv(){
        List<Joueur> joueurs = joueurController.getAll();
        List<ClassementIndivDTO> listIndiv = classementIndivService.getAllClassementIndiv(joueurs);
        List<ClassementIndivDTO> listEquipe = classementEquipeService.getAllClassementEquipe(joueurs);
        List<ClassementIndivDTO> listReturn = new ArrayList<>();
        for(ClassementIndivDTO classementIndivDTO : listIndiv){
            listReturn.add(classementIndivDTO);
        }
        for(ClassementIndivDTO classementIndivDTO : listEquipe){
            listReturn.add(classementIndivDTO);
        }
        return listReturn;
    }

    @GetMapping("/getAllClassementEquipe")
    public List<ClassementIndivDTO> getListClassementEquipe(){
        List<Joueur> joueurs = joueurController.getAll();
        return classementEquipeService.getAllClassementEquipe(joueurs);
    }
}
