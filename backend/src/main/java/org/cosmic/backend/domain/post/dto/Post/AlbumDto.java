package org.cosmic.backend.domain.post.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumDto {
    private Long albumId;
    private String albumName;
    private String artistName;
    //앨범정보가 제공됨
}
