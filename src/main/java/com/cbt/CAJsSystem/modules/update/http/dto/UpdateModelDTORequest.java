package com.cbt.CAJsSystem.modules.update.http.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateModelDTORequest {
    private String version;
    private String urlDownload;
    private String noteUpdate;
}
