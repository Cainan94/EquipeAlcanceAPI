package com.cbt.EquipeAlcance.infra.security;

import com.cbt.EquipeAlcance.modules.users.http.dto.UserAuthenticate;
import com.cbt.EquipeAlcance.modules.users.model.User;
import com.cbt.EquipeAlcance.modules.users.repopsitory.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsersRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token!=null){
            var login = tokenService.validateToken(token);
            Optional<User> optional = repository.findByUsernameIgnoreCase(login);
            if(optional.isPresent()){
                UserAuthenticate user = new UserAuthenticate(optional.get());
                var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader==null)
            return null;
        return authHeader.replace("Bearer ","");
    }
}
