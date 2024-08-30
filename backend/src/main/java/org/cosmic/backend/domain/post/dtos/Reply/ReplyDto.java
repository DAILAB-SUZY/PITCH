package org.cosmic.backend.domain.post.dtos.Reply;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDto {
    private Long replyId;
    public static ReplyDto createReplyDto(Long replyId) {
        return  ReplyDto.builder()
                .replyId(replyId)
                .build();
    }
}
