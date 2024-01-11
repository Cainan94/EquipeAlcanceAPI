package com.cbt.EquipeAlcance.modules.update.model;

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
@Table(name = "updater-control")
public class UpdateModel {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String urlDownload;

    @Column(columnDefinition = "TEXT")
    private String noteUpdate;

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

}
