package org.cosmic.backend.domain.user.dtos;

import org.cosmic.backend.domain.user.domains.Follow;
import org.cosmic.backend.domain.user.domains.User;

public record FollowDto(Long userId, String username, String profilePicture) {

  static public FollowDto from(Follow follow) {
    return new FollowDto(follow.getOther().getUserId(),
        follow.getOther().getUsername(),
        follow.getOther().getProfilePicture());
  }

  static public FollowDto from(User user) {
    return new FollowDto(user.getUserId(),
        user.getUsername(),
        user.getProfilePicture());
  }

  static public FollowDto following(Follow follow) {
    return FollowDto.from(follow.getOther());
  }

  static public FollowDto follower(Follow follow) {
    return FollowDto.from(follow.getUser());
  }
}
