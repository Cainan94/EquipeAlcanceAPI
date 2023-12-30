package com.cbt.CAJsSystem.modules.roles.http.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDTO implements Serializable {
    private String roleName;
}
