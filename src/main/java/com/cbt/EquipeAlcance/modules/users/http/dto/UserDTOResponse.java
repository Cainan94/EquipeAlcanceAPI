package com.cbt.EquipeAlcance.modules.users.http.dto;

import com.cbt.EquipeAlcance.modules.streamers.http.dto.StreamersDTOResponse;
import com.cbt.EquipeAlcance.modules.users.model.User;
import lombok.*;

import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOResponse implements Serializable {
    private String id;
    private boolean visible;
    private String username;
    private StreamersDTOResponse streamersDTOResponse = new StreamersDTOResponse();
    private String token;
    private String role;

    public static UserDTOResponse toDTO(User entity) {
        return UserDTOResponse.builder()
                .id(entity.getIdPublic().toString())
                .visible(entity.isVisible())
                .username(entity.getUsername())
                .streamersDTOResponse(StreamersDTOResponse.toDTO(entity.getStreamers()))
                .role(entity.getRole().getRoleName())
                .build();
    }

    public static UserDTOResponse toDTO(UserAuthenticate authenticate, String token) {
        return UserDTOResponse.builder()
                .id(authenticate.getUserEntity().getIdPublic().toString())
                .visible(authenticate.getUserEntity().isVisible())
                .username(authenticate.getUserEntity().getUsername())
                .streamersDTOResponse(StreamersDTOResponse.toDTO(authenticate.getUserEntity().getStreamers()))
                .role(authenticate.getUserEntity().getRole().getRoleName())
                .token(token)
                .build();
    }

}
