package org.cosmic.backend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {
    private Long albumId;
    private String albumName;
    private String artistName;
    //앨범정보가 제공됨
}
