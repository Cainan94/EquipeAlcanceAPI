package com.cbt.CAJsSystem.modules.roles.http.dto;

import com.cbt.CAJsSystem.modules.roles.model.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Builder
public class RolesDTOAuthenticate implements GrantedAuthority {

    private final Roles roles;

    public RolesDTOAuthenticate(Roles role){
        this.roles = role;
    }

    @Override
    public String getAuthority() {
        return roles.getRoleName();
    }
}
