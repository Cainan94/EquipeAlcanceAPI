package com.cbt.CAJsSystem.modules.users.http.api;

import com.cbt.CAJsSystem.infra.errors.exceptions.BadRequestException;
import com.cbt.CAJsSystem.modules.users.http.dto.UserDTORequest;
import com.cbt.CAJsSystem.modules.users.http.dto.UserDTOResponse;
import com.cbt.CAJsSystem.modules.users.model.User;
import com.cbt.CAJsSystem.modules.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService service;

    @PostMapping(value = "/login")
    @CrossOrigin("*")
    public ResponseEntity<UserDTOResponse> login(@RequestBody UserDTORequest userDTORequest) {
        return ResponseEntity.ok(service.doLogin(userDTORequest));
    }

    @PostMapping(value = "/register")
    @CrossOrigin("*")
    public ResponseEntity<UserDTOResponse> register(@RequestBody UserDTORequest userDTORequest){
        return ResponseEntity.ok(UserDTOResponse.toDTO(service.doRegister(userDTORequest)));
    }

    @PostMapping(value = "/update")
    @CrossOrigin("*")
    public ResponseEntity<UserDTOResponse> update (@RequestBody UserDTORequest userDTORequest){
        return ResponseEntity.ok(UserDTOResponse.toDTO(service.doUpdate(userDTORequest)));
    }

    @GetMapping(value ="/{username}")
    public ResponseEntity<UserDTOResponse> getByUserName(@PathVariable String username){
        Optional<User> optional = service.getByUserName(username);
        if(optional.isPresent()){
            return ResponseEntity.ok(UserDTOResponse.toDTO(optional.get()));
        }
        throw new BadRequestException("Stremaer não está registrado no sistema","Não foi possivel encontrar streamer");
    }

    @PostMapping(value="/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestBody UserDTORequest userDTORequest){
        return ResponseEntity.ok(service.deleteUser(userDTORequest));
    }

}
