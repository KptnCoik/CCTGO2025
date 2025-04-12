package com.cct.cctgo.entite;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PronoEquipeDTO {

    Epreuve epreuve;

    Map<Integer, Set<String>> pronoEquipe = new LinkedHashMap<>();
}
