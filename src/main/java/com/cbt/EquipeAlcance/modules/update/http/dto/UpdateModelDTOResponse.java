package com.cbt.EquipeAlcance.modules.update.http.dto;

import com.cbt.EquipeAlcance.modules.update.model.UpdateModel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateModelDTOResponse {
    private String id;
    private String version;
    private String urlDownload;
    private String noteUpdate;

    public static UpdateModelDTOResponse toDTO(UpdateModel model){
        return UpdateModelDTOResponse.builder()
                .id(model.getIdPublic().toString())
                .version(model.getVersion())
                .urlDownload(model.getUrlDownload())
//                .noteUpdate(model.getNoteUpdate())
                .build();
    }
}
