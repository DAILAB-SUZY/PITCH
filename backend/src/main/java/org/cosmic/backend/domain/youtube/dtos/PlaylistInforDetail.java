package org.cosmic.backend.domain.youtube.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistInforDetail {
    private String description;
    private String title;
    private String youtubeaccesstoken;
}
