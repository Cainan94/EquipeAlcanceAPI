package com.cbt.CAJsSystem.modules.ponctuation.http.dto;

import com.cbt.CAJsSystem.modules.ponctuation.model.Ponctuation;
import com.cbt.CAJsSystem.modules.streamers.http.dto.StreamersDTOResponse;
import com.cbt.CAJsSystem.modules.streamers.model.Streamers;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PonctuationDTOResponse {
    private String id;
    private float score;
    private boolean isregistered;
    private long date;
    private StreamersDTOResponse streamer;

    public static PonctuationDTOResponse toDTO(Ponctuation entity){
        return PonctuationDTOResponse.builder()
                .id(entity.getId().toString())
                .score(entity.getScore())
                .isregistered(entity.isVisible())
                .date(entity.getDate())
                .streamer(StreamersDTOResponse.toDTO(entity.getStreamers()))
                .build();
    }
}
