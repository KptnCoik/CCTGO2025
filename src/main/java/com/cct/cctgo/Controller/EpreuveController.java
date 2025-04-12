package com.cct.cctgo.Controller;

import com.cct.cctgo.Service.EpreuveService;
import com.cct.cctgo.entite.Epreuve;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Epreuve")
@CrossOrigin(origins = "*")
public class EpreuveController {

    @Autowired
    EpreuveService epreuveService;

    @Transactional
    @PostMapping("/addEpreuve")
    public Epreuve addEpreuve(String nom, int type, boolean hasProno, boolean hasChefEquipe){
        return epreuveService.addEpreuve(nom,type,hasProno,hasChefEquipe);
    }

    @GetMapping("/getAll")
    public List<Epreuve> getAll() {
        return epreuveService.getAll();
    }

    @GetMapping("/getIndiv")
    public List<Epreuve> getIndiv() {
        return epreuveService.getIndividuelle();
    }

    @GetMapping("/getEquipe")
    public List<Epreuve> getEquipe() {
        return epreuveService.getEpreuveEquipe();
    }

    @GetMapping("/getAllProno")
    public List<Epreuve> getAllProno() {return epreuveService.getAllProno();}
}
