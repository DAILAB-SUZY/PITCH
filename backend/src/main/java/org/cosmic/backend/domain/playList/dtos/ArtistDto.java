package org.cosmic.backend.domain.playList.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistDto {
    //artistId도 필요해보임
    private String artistName;
    public static ArtistDto createArtistDto(String artistName) {
        return  ArtistDto.builder()
                .artistName(artistName)
                .build();
    }
}
