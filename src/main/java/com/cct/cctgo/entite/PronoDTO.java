package com.cct.cctgo.entite;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.Map;

@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PronoDTO {

    Epreuve epreuve;

    Map<Integer,String> prono = new LinkedHashMap<>();
}
