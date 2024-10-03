package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumScoreDto {
    private int score;

    public static AlbumScoreDto createAlbumScoreDto(
           int score) {
        return  AlbumScoreDto.builder()
                .score(score)
                .build();
    }

}
