package com.cbt.EquipeAlcance.modules.ponctuation.http.adapters;

import com.cbt.EquipeAlcance.modules.ponctuation.http.dto.PonctuationDTOResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PonctuationTable {
    private String date;
    private List<PonctuationDTOResponse> pontuacaoes = new ArrayList<>();
}
