package com.cbt.CAJsSystem.modules.roles.service;

import com.cbt.CAJsSystem.modules.roles.model.Roles;
import com.cbt.CAJsSystem.modules.roles.repopsitory.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RolesRepository repository;

    public Roles selectByName(String name) {
        Optional<Roles> optional = repository.findByRoleName(name);
        if (optional.isEmpty())
            return null;
        return optional.get();
    }

    public Roles selectByCode(Integer code) {
        Optional<Roles> optional = repository.findByRoleCode(code);
        if (optional.isEmpty())
            return null;
        return optional.get();
    }

    public List<Roles> SelectAllRoles() {
        return repository.findAll();
    }
}
