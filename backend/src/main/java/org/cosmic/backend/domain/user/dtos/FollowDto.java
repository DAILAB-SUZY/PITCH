package org.cosmic.backend.domain.user.dtos;

import org.cosmic.backend.domain.user.domains.Follow;

public record FollowDto(Long userId) {

  static public FollowDto from(Follow follow) {
    return new FollowDto(follow.getOther().getUserId());
  }

  static public FollowDto following(Follow follow) {
    return new FollowDto(follow.getOther().getUserId());
  }

  static public FollowDto follower(Follow follow) {
    return new FollowDto(follow.getUser().getUserId());
  }
}
