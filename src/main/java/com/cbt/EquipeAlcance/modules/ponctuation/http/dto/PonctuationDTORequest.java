package com.cbt.EquipeAlcance.modules.ponctuation.http.dto;

import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTORequest;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PonctuationDTORequest {
    private int score;
    private StreamersDTORequest streamersDTORequest;

}
