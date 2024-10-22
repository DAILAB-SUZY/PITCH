package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@IdClass(FollowPK.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "other_id", nullable = false)
  private User other;
}
