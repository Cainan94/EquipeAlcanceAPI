package com.cbt.CAJsSystem.modules.roles.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Roles {

    @Id
    private UUID id;
    @Column(nullable = false)
    private boolean visible;
    @Column(nullable = false)
    private boolean deleted;
    @Column(nullable = false)
    private long dateCreate;
    @Column(nullable = true)
    private long lastModificationDate;

    @Column(nullable = false, unique = true)
    private String roleName;

    @Column(nullable = false, unique = true)
    private Integer roleCode;
}
