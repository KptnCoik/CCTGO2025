package com.cct.cctgo.entite;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassementIndivDTO {

    String epreuve;

    boolean termine;
    List<JoueurDTO> joueurs;

    int max;
}
