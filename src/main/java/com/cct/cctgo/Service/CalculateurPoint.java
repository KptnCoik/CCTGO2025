package com.cct.cctgo.Service;

import com.cct.cctgo.entite.Epreuve;
import com.cct.cctgo.utilitaire.TypeEpreuve;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CalculateurPoint {

    private List<Integer> pointsIndividuel = Arrays.asList(50,40,37,35,33,31,29,27,25,23,21,19,17,15,13,11,9,7,6,5,4,3,2,0);

    private List<Integer> pointsA2 = Arrays.asList(40,30,27,24,21,18,15,13,10,7,4,0);

    private List<Integer> pointsA3 = Arrays.asList(35,28,23,18,13,8,4,0);

    private List<Integer> pointsA4 = Arrays.asList(30,22,16,10,5,0);

    private List<Integer> pointA5 = Arrays.asList(25,15,7,0);

    private List<Integer> pointsA6 = Arrays.asList(25,15,7,0);

    private List<Integer> pointsA8 = Arrays.asList(20,10,0);

    private List<Integer> pointsA2E = Arrays.asList(15,0);

    private float calculPointIndiv(int position){
        return pointsIndividuel.get(position-1);
    }
    private float calculPointA2(int position){
        return pointsA2.get(position-1);
    }
    private float calculPointA3(int position){
        return pointsA3.get(position-1);
    }
    private float calculPointA4(int position){
        return pointsA4.get(position-1);
    }
    private float calculPointA6(int position){
        return pointsA6.get(position-1);
    }
    private float calculPointA8(int position){
        return pointsA8.get(position-1);
    }
    private float calculPointFoot(int position){
        return pointA5.get(position-1);
    }
    private float calculPointCorde(int position){
        return pointsA2E.get(position-1);
    }

    public float calculPoint(int position, Epreuve epreuve) {
        float point=0;
        switch(epreuve.getTypeEpreuve()) {
            case INDIVIDUELLE -> {
                point = calculPointIndiv(position);
            }
            case A2 -> {
                point = calculPointA2(position);
            }
            case A3 -> {
                point = calculPointA3(position);
            }
            case A4 -> {
                point = calculPointA4(position);
            }
            case A6 -> {
                point = calculPointA6(position);
            }
            case A8 -> {
                point = calculPointA8(position);
            }
            default -> { point = 0;}
        }
        return point;
    }
}
