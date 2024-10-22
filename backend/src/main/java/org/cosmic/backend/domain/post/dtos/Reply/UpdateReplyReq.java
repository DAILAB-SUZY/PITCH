package org.cosmic.backend.domain.post.dtos.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.entities.PostComment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateReplyReq {

  private String content;

  public static UpdateReplyReq from(PostComment postComment) {
    return UpdateReplyReq.builder()
        .content(postComment.getContent())
        .build();
  }
}
