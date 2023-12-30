package com.cbt.EquipeAlcance.modules.liveSchedules.http.dto;

import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTORequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveSchedulesDTORequest {
    private String id;
    private boolean visible;
    private boolean deleted;
    private long startTime;
    private long endTime;

    private StreamersDTORequest streamersDTORequest;
}
