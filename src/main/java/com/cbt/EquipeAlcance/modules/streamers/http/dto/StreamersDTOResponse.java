package com.cbt.EquipeAlcance.modules.streamers.http.dto;

import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamersDTOResponse implements Serializable {
    private String id;
    private boolean visible;
    private String twitchName;
    private long birthday;
    private boolean isInLive;
    private Long lastTimeInLive;

    public static StreamersDTOResponse toDTO(Streamers entity) {
        return StreamersDTOResponse.builder()
                .id(entity.getIdPublic().toString())
                .visible(entity.isVisible())
                .twitchName(entity.getTwitchName())
                .birthday(entity.getBirthday())
                .isInLive(entity.isInLive())
                .lastTimeInLive(entity.getLastTimeInLive())
                .build();
    }
}
