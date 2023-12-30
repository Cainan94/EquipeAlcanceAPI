package com.cbt.CAJsSystem.modules.users.http.dto;

import com.cbt.CAJsSystem.modules.roles.http.dto.RolesDTOAuthenticate;
import com.cbt.CAJsSystem.modules.users.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@Builder

public class UserAuthenticate implements UserDetails {
    private final User userEntity;


    public UserAuthenticate(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return Arrays.asList(new RolesDTOAuthenticate(userEntity.getRole()));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userEntity.isDeleted();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isVisible();
    }
}
