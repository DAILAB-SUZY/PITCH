package org.cosmic.backend.domain.albumChat.dtos.comment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonDeserialize(builder = AlbumChatCommentDto.AlbumChatCommentDtoBuilder.class)
public class AlbumChatCommentDto {
    private Long albumChatCommentId;
    @JsonPOJOBuilder(withPrefix = "")
    public static class AlbumChatCommentDtoBuilder {
    }
}
