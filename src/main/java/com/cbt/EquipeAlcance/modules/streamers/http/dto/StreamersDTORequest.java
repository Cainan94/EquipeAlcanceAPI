package com.cbt.EquipeAlcance.modules.streamers.http.dto;

import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamersDTORequest implements Serializable {
    private String id;
    private boolean visible;
    private String twitchName;
    private long birthday;

    public static StreamersDTORequest toDTO(Streamers streamers){
        return StreamersDTORequest.builder()
                .id(streamers.getIdPublic().toString())
                .visible(streamers.isVisible())
                .twitchName(streamers.getTwitchName())
                .birthday(streamers.getBirthday())
                .build();
    }
}
