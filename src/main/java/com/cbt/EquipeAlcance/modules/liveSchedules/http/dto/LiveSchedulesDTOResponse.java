package com.cbt.EquipeAlcance.modules.liveSchedules.http.dto;

import com.cbt.EquipeAlcance.modules.liveSchedules.model.LiveSchedule;
import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTOResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveSchedulesDTOResponse {
    private String id;
    private boolean visible;
    private boolean deleted;
    private long startTime;
    private long endTime;

    private StreamersDTOResponse streamersDTOResponse;

    public static LiveSchedulesDTOResponse toDTO(LiveSchedule entity) {
        return LiveSchedulesDTOResponse.builder()
                .id(entity.getIdPublic().toString())
                .visible(entity.isVisible())
                .deleted(entity.isDeleted())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .streamersDTOResponse(StreamersDTOResponse.toDTO(entity.getStreamer()))
                .build();
    }
}
