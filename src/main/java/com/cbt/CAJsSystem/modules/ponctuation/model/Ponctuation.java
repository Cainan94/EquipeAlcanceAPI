package com.cbt.CAJsSystem.modules.ponctuation.model;

import com.cbt.CAJsSystem.modules.streamers.model.Streamers;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ponctuation")
public class Ponctuation {

    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID idPublic;
    @Column(nullable = false)
    private boolean visible;
    @Column(nullable = false)
    private boolean deleted;
    @Column(nullable = false)
    private long dateCreate;
    @Column
    private long lastModificationDate;

    @Column()
    private long date;

    @Column()
    private int score;

    @ManyToOne
    @JoinColumn(name = "streamers_id", nullable = false)
    private Streamers streamers;
}
