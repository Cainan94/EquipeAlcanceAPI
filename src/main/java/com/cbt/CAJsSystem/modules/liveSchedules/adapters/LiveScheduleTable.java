package com.cbt.CAJsSystem.modules.liveSchedules.adapters;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveScheduleTable {
    private String dataAgendamento;
    private List<StreamersAgendamentoTable> streamers = new ArrayList<>();
}
