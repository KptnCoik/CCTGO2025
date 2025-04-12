package com.cct.cctgo.entite;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JoueurDTO {

    int id;

    String nom;

    float point;

    int position;

    String bonus;

}
