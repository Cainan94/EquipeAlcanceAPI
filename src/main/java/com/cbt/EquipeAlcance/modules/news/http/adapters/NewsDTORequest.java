package com.cbt.EquipeAlcance.modules.news.http.adapters;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsDTORequest {
    private String id;
    private long date;
    private String title;
    private String desc;
    private String link;
    private String image;
}
