package com.cbt.CAJsSystem.modules.liveSchedules.http.dto;

import com.cbt.CAJsSystem.modules.streamers.http.dto.StreamersDTORequest;
import com.cbt.CAJsSystem.modules.streamers.model.Streamers;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
