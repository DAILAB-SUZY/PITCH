import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import useStore from "../store/store";

// Props 타입 정의
interface PlaylistProps {
  playlist: [
    {
      playlistId: number;
      trackId: number;
      title: string;
      artistName: string;
      trackCover: string;
    },
  ];
  userDetail: {
    id: number;
    username: string;
    profilePicture: string;
    dnas: [
      {
        dnaKey: number;
        dnaName: string;
      },
    ];
  };
}

// 스타일 정의
const PlaylistCardSmall = styled.div`
  background: linear-gradient(90deg, #3fc8ff, #d1dcff);
  border-radius: 12px;
  padding: 15px;
  width: 330px;
  height: 190px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: white;
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
`;

const UserNameArea = styled.h2`
  width: 100%;
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: white;
`;

const SongList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
`;

const SongItem = styled.div`
  display: flex;
  align-items: center;
`;

const AlbumCover = styled.img`
  width: 50px;
  height: 50px;
  border-radius: 8px;
  margin-right: 12px;
`;

const SongInfo = styled.div`
  display: flex;
  width: auto;
  height: 100%;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-evenly;
`;

const SongTitle = styled.div`
  font-size: 16px;
  font-weight: bold;
`;

const Artist = styled.div`
  font-size: 14px;
  color: white;
`;

const MoreButton = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  text-align: right;
  color: white;
  cursor: pointer;
`;

// Playlist 컴포넌트
const PlaylistCard = ({ playlist, userDetail }: PlaylistProps) => {
  //const { email, setEmail, name, setName, id, setId } = useStore();
  const navigate = useNavigate();
  const GoToPlayListPage = (author: {}) => {
    navigate("/PlayListPage", { state: author });
  };

  const playlistInfo = {
    id: userDetail.id,
    username: userDetail.username,
    profilePicture: userDetail.profilePicture,
    page: 2,
  };
  return (
    <PlaylistCardSmall>
      <UserNameArea>{userDetail.username}'s Playlist</UserNameArea>
      <SongList>
        {playlist.slice(0, 2).map((song) => (
          <SongItem key={song.trackId}>
            <AlbumCover src={song.trackCover} alt="Album Cover" />
            <SongInfo>
              <SongTitle>{song.title}</SongTitle>
              <Artist>{song.artistName}</Artist>
            </SongInfo>
          </SongItem>
        ))}
      </SongList>
      <MoreButton onClick={() => GoToPlayListPage(playlistInfo)}>더보기</MoreButton>
    </PlaylistCardSmall>
  );
};

export default PlaylistCard;
