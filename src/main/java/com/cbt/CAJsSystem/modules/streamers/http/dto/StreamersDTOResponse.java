package com.cbt.CAJsSystem.modules.streamers.http.dto;

import com.cbt.CAJsSystem.modules.streamers.model.Streamers;
import com.cbt.CAJsSystem.utils.DateUtils;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

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
