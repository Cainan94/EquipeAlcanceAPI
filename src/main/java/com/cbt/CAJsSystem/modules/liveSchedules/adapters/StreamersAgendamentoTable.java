package com.cbt.CAJsSystem.modules.liveSchedules.adapters;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamersAgendamentoTable {
    private String id;
    private String nome;
    private String horaInicio;
    private String horaFim;
    private String link;
}
