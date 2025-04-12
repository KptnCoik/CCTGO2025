package com.cct.cctgo.Service;

import com.cct.cctgo.entite.*;
import com.cct.cctgo.utilitaire.TypeEpreuve;
import com.cct.cctgo.utilitaire.TypeProno;
import com.cct.cctgo.repository.ClassementIndivRepository;
import com.cct.cctgo.repository.EpreuveRepository;
import com.cct.cctgo.repository.JoueurRepository;
import com.cct.cctgo.repository.PronosticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PronosticService {

    @Autowired
    PronosticRepository repository;

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    EpreuveRepository epreuveRepository;

    @Autowired
    JoueurService joueurService;

    @Autowired
    ClassementIndivService classementIndivService;

    @Autowired
    ClassementIndivRepository classementIndivRepository;

    @Autowired
    PronosticIndivService pronosticIndivService;

    @Autowired
    private ClassementEquipeService classementEquipeService;

    @Autowired
    private PronosticEquipeService pronosticEquipeService;

    public Pronostic addProno(String joueur, String epreuve, Integer... prono) {
        var epreuveEntity = epreuveRepository.findEpreuveByNom(epreuve);
        if (epreuveEntity == null) {
            throw new IllegalArgumentException("L'épreuve spécifiée n'existe pas : " + epreuve);
        }
        Map<Integer, Integer> pronoEpreuve = new HashMap<>();
        int borneInf = switch(epreuveEntity.getTypeEpreuve()) {
            case INDIVIDUELLE -> 4;
            case A2, A3 -> 2;
            default -> 0;
        };
        int borneSup = switch(epreuveEntity.getTypeEpreuve()) {
            case INDIVIDUELLE -> 15;
            case A2 -> 7;
            case A3 -> 3;
            default -> 0;
        };

        if(epreuveEntity.getTypeEpreuve().equals(TypeEpreuve.INDIVIDUELLE) ||
                epreuveEntity.getTypeEpreuve().equals(TypeEpreuve.A2) ||
                epreuveEntity.getTypeEpreuve().equals(TypeEpreuve.A3)) {
            pronoEpreuve = IntStream.range(0, prono.length)
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> (i > borneInf) ? (i + borneSup) : (i + 1), // Calcul de la position
                            i -> prono[i]
                    ));
        } else {
            pronoEpreuve = IntStream.range(0, prono.length)
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> (i + 1), // Calcul de la position
                            i -> prono[i]
                    ));
        }

        Pronostic pronostic = new Pronostic();
        pronostic.setEpreuve(epreuveEntity);
        pronostic.setProno(pronoEpreuve);

        repository.save(pronostic);
        joueurService.setProno(joueur, pronostic);

        return pronostic;
    }

    public PronoDTO getProno(String joueur, String epreuve) {
        return joueurRepository.findJoueurByNom(joueur).getPronostics().stream().
                filter(x->x.getEpreuve().getNom().equals(epreuve)).findFirst().orElse(null) == null ? null
                : pronosticIndivService.getPronoIndiv(joueur,epreuve);
    }

    public PronoEquipeDTO getPronoEquipe(String joueur, String epreuve) {
        return joueurRepository.findJoueurByNom(joueur).getPronostics().stream().
                filter(x->x.getEpreuve().getNom().equals(epreuve)).findFirst().orElse(null) == null ? null
                : pronosticEquipeService.getPronoEquipe(joueur,epreuve);
    }


    public void setAllProno() {
        List<Epreuve> listEpreuve = epreuveRepository.findAll();
        listEpreuve.removeIf(x->!x.isHasProno());
        List<Joueur> listJoueur = joueurRepository.findAll();
        for(Epreuve epreuve : listEpreuve) {
            for(Joueur joueur : listJoueur) {
                joueur.setPointProno(joueur.getPointProno()+getPointProno(joueur.getNom(),epreuve.getNom()));
            }
        }
    }

    public boolean deleteProno(String nomJoueur, String epreuve) {
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        Pronostic pronostic = joueur.getPronostics().stream().filter(x->x.getEpreuve().getNom().equals(epreuve)).findFirst().orElse(null);
        Pronostic pronoRepo = repository.findById(pronostic.getId()).get();
        joueur.getPronostics().removeIf(x->x.getEpreuve().getNom().equals(epreuve));
        joueurRepository.save(joueur);
        repository.deleteById(pronoRepo.getId());

        return true;
    }


    public Integer getPointProno(String nomJoueur, String nomEpreuve) {
        Integer point=0;
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        Epreuve epreuve = epreuveRepository.findEpreuveByNom(nomEpreuve);
        Pronostic prono = joueur.getPronostics().stream().filter(x->x.getEpreuve().getNom().equals(nomEpreuve)).findFirst().orElse(null);
        if (epreuve.getTypeEpreuve().equals(TypeEpreuve.INDIVIDUELLE)) {
            ClassementIndivDTO classement = classementIndivService.getListClassementIndiv(nomEpreuve);
            point =  pronosticIndivService.calculateurPointProno(classement,prono);
        } else {
            ClassementEquipeDTO classementEquipeDTO = classementEquipeService.getListClassementEquipe(nomEpreuve);
            point = pronosticEquipeService.calculateurPointProno(classementEquipeDTO,prono,epreuve);
        }


        return point;
    }

    public void setPointProno(String nomJoueur, String epreuve) {
        Joueur joueur = joueurRepository.findJoueurByNom(nomJoueur);
        joueur.setPointProno(joueur.getPointProno()+getPointProno(nomJoueur,epreuve));
        joueurRepository.save(joueur);
    }

    public void setAllPointPronoEpreuve(String epreuve) {
        List<Joueur> listJoueur = joueurRepository.findAll();
        for(Joueur joueur : listJoueur){
            setPointProno(joueur.getNom(),epreuve);
        }
    }

    public void setClassementProno(){
        List<Joueur> listJoueur = joueurRepository.findAll();
        Collections.sort(listJoueur, new Comparator<Joueur>() {
            public int compare(Joueur p1, Joueur p2) {
                return Integer.valueOf(p2.getPointProno()).compareTo(p1.getPointProno());
            }
        });
        for(int i =1;i<=listJoueur.size();i++){
            if(i>1 && listJoueur.get(i-2).getPointProno()==listJoueur.get(i-1).getPointProno()){
                classementIndivService.saveClassement(listJoueur.get(i-1).getNom(),"Pronostic",
                        classementIndivService.getClassementIndiv(listJoueur.get(i-2),epreuveRepository.findEpreuveByNom("Pronostic")));
            } else {
                classementIndivService.saveClassement(listJoueur.get(i-1).getNom(),"Pronostic",i);
            }

        }
    }
}
