package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.FollowDto;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtherUserFollowDetail {
    UserDetail user;
    private List<FollowDto> followings;
    private List<FollowDto> followers;

    public static OtherUserFollowDetail from(User user) {
        return OtherUserFollowDetail.builder()
                .user(User.toUserDetail(user))
                .followings(user.getFollowings().stream().map(FollowDto::following).toList())
                .followers(user.getFollowers().stream().map(FollowDto::follower).toList())
                .build();
    }
    public static List<OtherUserFollowDetail> from(List<User> users) {
        return users.stream().map(OtherUserFollowDetail::from).toList();
    }
}
