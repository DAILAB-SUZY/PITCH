package org.cosmic.backend.domain.user.apis;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.user.applications.FollowService;
import org.cosmic.backend.domain.user.dtos.FollowDto;
import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "팔로우/팔로잉 기능")
@AllArgsConstructor
@Log4j2
public class FollowApi {

  private FollowService followService;

  @PostMapping("/follow/{otherUserId}")
  public ResponseEntity<List<FollowDto>> followOrUnfollow(@PathVariable Long otherUserId,
      @AuthenticationPrincipal Long userId) {
    try {
      followService.unfollow(userId, otherUserId);
    } catch (NotFoundException e) {
      followService.follow(userId, otherUserId);
    }
    return ResponseEntity.ok(followService.followings(userId));
  }

  @GetMapping("/{userId}/following")
  public ResponseEntity<List<FollowDto>> getFollowing(@PathVariable Long userId) {
    return ResponseEntity.ok(followService.followings(userId));
  }

  @GetMapping("/{userId}/follower")
  public ResponseEntity<List<FollowDto>> getFollwer(@PathVariable Long userId) {
    return ResponseEntity.ok(followService.followers(userId));
  }
}
