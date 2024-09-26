package org.cosmic.backend.domain.user.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
public class AuthorDetail {
    private Long id;
    private String username;
    private String profilePicture;

    public AuthorDetail(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture();
    }
}
