package com.cbt.EquipeAlcance.modules.news.http.adapters;

import com.cbt.EquipeAlcance.modules.news.model.NewsModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsDTOResponse {
    private String id;
    private long date;
    private String title;
    private String desc;
    private String link;
    private String image;

    public static NewsDTOResponse toDto(NewsModel model) {
        return new NewsDTOResponse().builder()
                .id(model.getIdPublic().toString())
                .date(model.getDateCreate())
                .title(model.getTitle())
                .desc(model.getDescription())
                .link(model.getLink())
                .image(model.getImage())
                .build();
    }
}
