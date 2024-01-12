package com.cbt.EquipeAlcance.modules.liveSchedules.model;

import com.cbt.EquipeAlcance.modules.streamers.model.Streamers;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "live_schedule")
public class LiveSchedule {

    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID idPublic;
    @Column(nullable = false)
    private boolean visible;
    @Column(nullable = false)
    private boolean deleted;
    @Column(nullable = false)
    private Long dateCreate;
    @Column
    private Long lastModificationDate;

    @Column
    private Long startTime;

    @Column
    private Long endTime;

    @ManyToOne()
    @JoinColumn(name = "streamer_id")
    private Streamers streamer;
}
