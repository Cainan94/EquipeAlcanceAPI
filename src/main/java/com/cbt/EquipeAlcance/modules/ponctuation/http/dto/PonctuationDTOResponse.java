package com.cbt.EquipeAlcance.modules.ponctuation.http.dto;

import com.cbt.EquipeAlcance.modules.ponctuation.model.Ponctuation;
import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTOResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PonctuationDTOResponse implements Comparable<PonctuationDTOResponse>{
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

    @Override
    public int compareTo(PonctuationDTOResponse o) {
        if(score > o.score){
            return -1;
        }if(score < o.score){
            return 1;
        }else{
            return 0;
        }
    }
}
