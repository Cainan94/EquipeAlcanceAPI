package com.cbt.CAJsSystem.modules.users.service;

import com.cbt.CAJsSystem.infra.errors.exceptions.BadRequestException;
import com.cbt.CAJsSystem.infra.security.TokenService;
import com.cbt.CAJsSystem.modules.roles.http.dto.RoleRequestDTO;
import com.cbt.CAJsSystem.modules.roles.model.Roles;
import com.cbt.CAJsSystem.modules.roles.service.RoleService;
import com.cbt.CAJsSystem.modules.streamers.http.dto.StreamersDTORequest;
import com.cbt.CAJsSystem.modules.streamers.service.StreamersServices;
import com.cbt.CAJsSystem.modules.users.http.dto.UserAuthenticate;
import com.cbt.CAJsSystem.modules.users.http.dto.UserDTORequest;
import com.cbt.CAJsSystem.modules.users.http.dto.UserDTOResponse;
import com.cbt.CAJsSystem.modules.users.model.User;
import com.cbt.CAJsSystem.modules.users.repopsitory.UsersRepository;
import com.cbt.CAJsSystem.utils.DateUtils;
import com.cbt.CAJsSystem.utils.ID;
import com.cbt.CAJsSystem.utils.Security;
import com.cbt.CAJsSystem.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepository repository;

    @Autowired
    private StreamersServices streamersServices;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTOResponse doLogin(UserDTORequest dtoRequest) {

        var user = repository.findByUsernameIgnoreCase(dtoRequest.getUsername());
        if (user.isPresent() && !passwordEncoder.matches(dtoRequest.getPassword(), user.get().getPassword())) {
            throw new BadRequestException("Usuário ou senha incorreto", "Falha de login");
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(dtoRequest.getUsername(), dtoRequest.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserAuthenticate) auth.getPrincipal());
        if(user.isPresent()){
            if(user.get().getCurrentToken() != null && !StringUtils.isNullOrEmpty(user.get().getCurrentToken())){
                var updateUser = UserDTORequest.setCurrentToken(user.get(),token);
                this.doUpdate(updateUser);
            }
        }
        return UserDTOResponse.toDTO((UserAuthenticate) auth.getPrincipal(), token);
    }

    public Optional<User> getByUserName(String username) {
        return repository.findByUsernameIgnoreCase(username);
    }

    @Transactional
    public User doUpdate(UserDTORequest dtoRequest) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            Optional<User> userOptional = repository.findByIdPublic(UUID.fromString(dtoRequest.getId()));
            if (userOptional.isEmpty()) {
                throw new BadRequestException("Usuário não registrado na base de dados.", "Falha ao atualizar usuário");
            }
            if (!canUpdate(dtoRequest, userOptional.get())) {
                throw new BadRequestException("Não foi possivel Atualizar usuário.", "Falha ao atualizar usuário");
            }
            return repository.save(User.builder()
                    .id(userOptional.get().getId())
                    .idPublic(userOptional.get().getIdPublic())
                    .dateCreate(userOptional.get().getDateCreate())
                    .deleted(userOptional.get().isDeleted())
                    .visible(userOptional.get().isVisible())
                    .dateCreate(userOptional.get().getDateCreate())
                    .username(dtoRequest.getUsername())
                    .password(UpdatePassword(dtoRequest, userOptional.get()))
                    .lastModificationDate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                    .role(setRole(dtoRequest.getRole()))
                    .streamers(streamersServices.doUpdate(StreamersDTORequest.builder()
                            .birthday(dtoRequest.getStreamersRequestDTO().getBirthday())
                            .twitchName(dtoRequest.getUsername())
                            .id(dtoRequest.getStreamersRequestDTO().getId())
                            .build()))

                    .build());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao atualizar usuário");
        }
    }

    private String UpdatePassword(UserDTORequest dtoRequest, User user) {
        if (StringUtils.isNullOrEmpty(dtoRequest.getPassword())) {
            return user.getPassword();
        }
        return passwordEncoder.encode(dtoRequest.getPassword());
    }

    private boolean canUpdate(UserDTORequest dtoRequest, User user) {
        if (!user.getIdPublic().equals(ID.toUUID(dtoRequest.getId()))) {
            throw new BadRequestException("Usuário recebido invalido", "Falha ao atualizar usuário");
        }
        if (StringUtils.isNullOrEmpty(dtoRequest.getUsername())) {
            throw new BadRequestException("Username deve ser informado.", "Falha ao registrar usuário");
        }
        if (repository.findByUsernameIgnoreCase(dtoRequest.getUsername()).isPresent() && !dtoRequest.getUsername().equals(user.getUsername())) {
            throw new BadRequestException("Username já está sendo utilizado", "Falha ao registrar usuário");
        }
        return true;
    }

    @Transactional
    public User doRegister(UserDTORequest dtoRequest) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            if (canRegister(dtoRequest)) {
                return repository.save(User.builder()
                        .id(ID.generate())
                        .idPublic(ID.generate())
                        .visible(true)
                        .deleted(false)
                        .dateCreate(DateUtils.localDateTimeToEpoch(LocalDateTime.now()))
                        .lastModificationDate(0l)
                        .username(dtoRequest.getUsername())
                        .password(new BCryptPasswordEncoder().encode(dtoRequest.getPassword()))
                        .streamers(streamersServices.doRegister(StreamersDTORequest.builder()
                                .birthday(dtoRequest.getStreamersRequestDTO().getBirthday())
                                .twitchName(dtoRequest.getUsername())
                                .build()))
                        .role(setRole(dtoRequest.getRole()))
                        .build());
            } else
                throw new BadRequestException("Não foi possivel registrar usuário.", "Falha ao registrar usuário");
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao registrar usuário");
        }
    }

    @Transactional
    private Roles setRole(String roleStr) {
        Roles role = null;
        if (!StringUtils.isNullOrEmpty(roleStr)) {
            role = roleService.selectByName(roleStr);
        }
        if (role == null)
            return roleService.selectByName("streamer");
        return role;
    }

    private boolean canRegister(UserDTORequest dtoRequest) {
        if (!StringUtils.isNullOrEmpty(dtoRequest.getUsername()) && !StringUtils.isNullOrEmpty(dtoRequest.getPassword())) {
            if (repository.findByUsernameIgnoreCase(dtoRequest.getUsername()).isEmpty()) {
                return true;
            } else {
                throw new BadRequestException("Username já está sendo utilizado", "Falha ao registrar usuário");
            }
        } else {
            throw new BadRequestException("Username e senha deve ser informado.", "Falha ao registrar usuário");
        }
    }

    @Transactional
    public boolean deleteUser(UserDTORequest userDTORequest) {
        try {
            if(!Security.isADM()){
                throw new BadRequestException("Seu usuário não tem privilégios para esta operação", "Falha de segurança");
            }
            Optional<User> userOptional = repository.findByIdPublic(UUID.fromString(userDTORequest.getId()));
            if (userOptional.isEmpty()) {
                throw new BadRequestException("Usuário não registrado na base de dados.", "Falha ao deletar usuário");
            }

            StreamersDTORequest streamersDTORequest = StreamersDTORequest.builder().build();

            repository.save(User.builder()
                    .id(userOptional.get().getId())
                    .idPublic(userOptional.get().getIdPublic())
                    .visible(false)
                    .deleted(true)
                    .dateCreate(userOptional.get().getDateCreate())
                    .lastModificationDate(userOptional.get().getLastModificationDate())
                    .username(userOptional.get().getUsername())
                    .password(userOptional.get().getPassword())
                    .streamers(streamersServices.delete(userOptional.get().getStreamers().getIdPublic()))
                    .role(userOptional.get().getRole())
                    .build());

            return true;
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage(), "Falha critica ao deletar usuário");
        }
    }
}
