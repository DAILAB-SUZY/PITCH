package org.cosmic.backend.domain.user.dtos;

import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.User;

public record FollowDto(Long userId, String username, String profilePicture) {

  static public FollowDto from(Follow follow) {
    return from(follow.getOther());
  }

  static public FollowDto from(User user) {
    return new FollowDto(user.getUserId(),
        user.getUsername(),
        User.getOriginProfilePicture(user.getProfilePicture()));
  }

  static public FollowDto following(Follow follow) {
    return FollowDto.from(follow.getOther());
  }

  static public FollowDto follower(Follow follow) {
    return FollowDto.from(follow.getUser());
  }
}
