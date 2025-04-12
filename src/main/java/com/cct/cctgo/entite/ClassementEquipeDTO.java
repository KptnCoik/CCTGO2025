package com.cct.cctgo.entite;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassementEquipeDTO {

    String epreuve;

    boolean termine;

    List<EquipeDTO> equipe;

    int max;


}
