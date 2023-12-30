package com.cbt.EquipeAlcance.infra.security;

import com.cbt.EquipeAlcance.infra.errors.exceptions.BadRequestException;
import com.cbt.EquipeAlcance.modules.users.http.dto.UserAuthenticate;
import com.cbt.EquipeAlcance.modules.users.model.User;
import com.cbt.EquipeAlcance.modules.users.repopsitory.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    final UsersRepository repository;

    public UserDetailServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsernameIgnoreCase(username);
        if(user.isPresent())
            return new UserAuthenticate(user.get());
        throw new BadRequestException("Usuário ou senha incorreto","Falha de login");
    }
}
