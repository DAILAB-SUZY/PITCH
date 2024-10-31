package org.cosmic.backend.domain.user.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.user.domains.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {

  private Long id;
  private String username;
  private String profilePicture;
  private List<DnaDetail> dnas;

  public static UserDetail from(User user) {
    return User.toUserDetail(user);
  }

  public static List<UserDetail> from(List<User> userLikes) {
    return userLikes.stream().map(UserDetail::from).toList();
  }

}
