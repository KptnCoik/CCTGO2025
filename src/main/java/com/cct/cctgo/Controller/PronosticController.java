package com.cct.cctgo.Controller;

import com.cct.cctgo.Service.PronosticService;
import com.cct.cctgo.entite.PronoDTO;
import com.cct.cctgo.entite.PronoEquipeDTO;
import com.cct.cctgo.entite.Pronostic;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Prono")
@CrossOrigin(origins = "*")
public class PronosticController {

    @Autowired
    PronosticService service;

    @Transactional
    @PostMapping("/addProno")
    public Pronostic addProno(String joueur, String epreuve, Integer... prono) {
        return service.addProno(joueur,epreuve,prono);
    }

    @GetMapping("/getPronoIndiv")
    public PronoDTO getPronoIndiv(String joueur, String epreuve) {
        return service.getProno(joueur,epreuve);
    }

    @GetMapping("/getPronoEquipe")
    public PronoEquipeDTO getPronoEquipe(String joueur, String epreuve) {
        return service.getPronoEquipe(joueur,epreuve);
    }

    @GetMapping("/getPointProno")
    public Integer getPointProno(String joueur, String epreuve) {
        return service.getPointProno(joueur,epreuve);
    }

    @GetMapping("/setAllProno")
    public void setAllProno() {
        service.setAllProno();
    }

    @DeleteMapping("/deleteProno")
    public void deleteProno(String joueur, String epreuve) {
         service.deleteProno(joueur,epreuve);
    }

    @GetMapping("/setPointPronoEpreuve")
    public void setPointPronoEpreuve(String joueur, String epreuve) {
        service.setPointProno(joueur,epreuve);
    }

    @GetMapping("/setAllPointPronoEpreuve")
    public void setAllPointPronoEpreuve(String epreuve){
    service.setAllPointPronoEpreuve(epreuve);
    }

    @GetMapping("/setClassementProno")
    public void setClassementProno(){
        service.setClassementProno();
    }

}
