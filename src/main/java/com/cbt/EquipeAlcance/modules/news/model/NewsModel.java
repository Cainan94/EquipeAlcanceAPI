package com.cbt.EquipeAlcance.modules.news.model;

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
@Table(name = "news")
public class NewsModel {
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column
    private String link;

    @Column
    private String image;



}
