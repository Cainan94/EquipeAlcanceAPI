package com.cbt.EquipeAlcance.modules.users.model;


import com.cbt.EquipeAlcance.modules.roles.model.Roles;
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
@Table(name = "users")
public class User {

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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "streamer_id")
    private Streamers streamers;

    @JoinColumn(name="role_id")
    @ManyToOne(optional = false)
    private Roles role;

}
