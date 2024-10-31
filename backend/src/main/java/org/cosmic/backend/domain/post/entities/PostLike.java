package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`post_like`")
@Builder
@IdClass(PostLikePK.class)
public class PostLike {

  @Id
  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
