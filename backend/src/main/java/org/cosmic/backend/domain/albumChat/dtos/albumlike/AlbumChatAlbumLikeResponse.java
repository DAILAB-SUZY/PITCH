package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumChatAlbumLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;

    public AlbumChatAlbumLikeResponse(Long userId, String userName,String profilePicture){
        AlbumChatAlbumLikeResponse likeresponse = new AlbumChatAlbumLikeResponse();
        likeresponse.setUserId(userId);
        likeresponse.setUserName(userName);
        likeresponse.setProfilePicture(profilePicture);
    }
}

