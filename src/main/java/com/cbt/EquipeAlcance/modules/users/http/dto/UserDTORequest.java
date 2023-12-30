package com.cbt.EquipeAlcance.modules.users.http.dto;

import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTORequest;
import com.cbt.EquipeAlcance.modules.users.model.User;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTORequest implements Serializable {
    private String id;
    private String username;
    private String password;
    private StreamersDTORequest streamersRequestDTO = new StreamersDTORequest();
    private String role;
    private String currentToken;

    public static UserDTORequest toDTO(User user) {
        return UserDTORequest.builder()
                .id(user.getIdPublic().toString())
                .username(user.getUsername())
                .password(user.getPassword())
                .streamersRequestDTO(StreamersDTORequest.toDTO(user.getStreamers()))
                .role(user.getRole().getRoleName())
                .currentToken(user.getCurrentToken())
                .build();
    }

    public static UserDTORequest setCurrentToken(User user, String token){
        var request = toDTO(user);
        request.setCurrentToken(token);
        return request;
    }
}
