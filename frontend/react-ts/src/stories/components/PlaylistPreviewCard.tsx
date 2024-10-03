import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import { colors } from "../../styles/color";

// PlaylistData 타입 정의
interface FriendsPlayList {
  playlistId: number;
  albumCover: string[];
  author: {
    id: number;
    username: string;
    profilePicture: string;
  };
}

// Props 타입 정의
interface PlaylistProps {
  playlists: FriendsPlayList[];
}

// 전체 컨테이너 (가로 스크롤 가능)
const PlaylistContainer = styled.div`
  width: 100vw;
  display: flex;
  overflow-x: scroll;
  padding: 20px;
`;

// 각 플레이리스트 박스
const PlaylistBox = styled.div<{ bgColor: string }>`
  background-color: ${({ bgColor }) => bgColor || "#ddd"};
  border-radius: 12px;
  width: 170px;
  height: 220px;
  margin-right: 16px;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
`;

// 앨범 커버 이미지 묶음
const AlbumCoverStack = styled.div`
  display: flex;
  position: relative;
  width: 90px;
  height: 90px;
  margin-top: 30px;
  box-sizing: border-box;
`;

// 앨범 커버 이미지 스타일
const AlbumCover = styled.img`
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: cover;
  right: -10px;
  top: -10px;
  &:nth-child(2) {
    right: 0px;
    top: 0px;
    z-index: 1;
  }
  &:nth-child(3) {
    right: 10px;
    top: 10px;
    z-index: 2;
  }
`;

const ProfileArea = styled.div`
  width: 150px;
  height: 70px;
  padding: 15px 10px 15px 10px;
  border-radius: 0px 0px 12px 12px;
  background: linear-gradient(
    0deg,
    rgba(0, 0, 0, 0.6) 0%,
    rgba(0, 0, 0, 0) 100%
  );
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-start;
`;
// 프로필 이미지 스타일
const ProfileImage = styled.img`
  width: 35px;
  height: 35px;
  border-radius: 50%;
  margin-right: 10px;
`;

const UserNameArea = styled.div`
  width: 100%;
  height: 35px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-evenly;
`;
// 사용자 이름 스타일
const UserName = styled.div<{ fontFamily: string }>`
  font-size: 13px;
  font-family: ${(props) => props.fontFamily};
  color: white;
`;

const PlaylistPreviewCard = ({ playlists }: PlaylistProps) => {
  const navigate = useNavigate();
  const GoToPlayListPage = (userId: number) => {
    navigate("/PlayListPage", { state: userId });
  };
  return (
    <PlaylistContainer>
      {playlists.map((playlist) => (
        <PlaylistBox
          key={playlist.playlistId}
          bgColor={colors.BG_grey}
          onClick={() => GoToPlayListPage(playlist.author.id)}
        >
          <AlbumCoverStack>
            {playlist.albumCover.reverse().map((cover: any, index: any) => (
              <AlbumCover
                key={index}
                src={cover}
                alt={`Album Cover ${index + 1}`}
              />
            ))}
          </AlbumCoverStack>
          <ProfileArea>
            <ProfileImage src={playlist.author.profilePicture} alt="Profile" />
            <UserNameArea>
              <UserName fontFamily="Bd">{playlist.author.username}'s</UserName>
              <UserName fontFamily="Rg">Playlist</UserName>
            </UserNameArea>
          </ProfileArea>
        </PlaylistBox>
      ))}
    </PlaylistContainer>
  );
};

export default PlaylistPreviewCard;
