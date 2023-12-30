package com.cbt.CAJsSystem.modules.ponctuation.http.dto;

import com.cbt.CAJsSystem.modules.streamers.http.dto.StreamersDTORequest;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PonctuationDTORequest {
    private int score;
    private StreamersDTORequest streamersDTORequest;

}
