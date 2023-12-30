package com.cbt.EquipeAlcance.modules.streamers.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "streamers")
public class Streamers {
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

    @Column(unique = true, nullable = false)
    private String twitchName;

    @Column(nullable = false)
    private long birthday;

    @Column(nullable = false)
    private boolean isInLive;

    @Column
    private long lastTimeInLive;

}
