package org.cosmic.backend.domain.youtube.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistInforDetail {
    private String description;
    private String title;
    @NotNull(message="youtubeAccess키는 필수 항목입니다.")
    private String youtubeaccesstoken;
}
