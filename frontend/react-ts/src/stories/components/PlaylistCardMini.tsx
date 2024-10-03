import styled from "styled-components";
import { useNavigate } from "react-router-dom";

// PlaylistData 타입 정의
interface SongData {
  id: number;
  songName: string;
  artist: string;
  albumCover: string;
}

// Props 타입 정의
interface PlaylistProps {
  id: number;
  userName: string;
  profileImage: string;
  songs: SongData[];
}

interface PlaylistCardProps {
  playlist: PlaylistProps;
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
const PlaylistCard = ({ playlist }: PlaylistCardProps) => {
  const navigate = useNavigate();
  const GoToPlayListPage = () => {
    navigate("/PlayListPage");
  };
  const { songs, userName } = playlist;
  return (
    <PlaylistCardSmall>
      <UserNameArea>{userName}'s Playlist</UserNameArea>
      <SongList>
        {songs.map((song) => (
          <SongItem key={song.id}>
            <AlbumCover src={song.albumCover} alt="Album Cover" />
            <SongInfo>
              <SongTitle>{song.songName}</SongTitle>
              <Artist>{song.artist}</Artist>
            </SongInfo>
          </SongItem>
        ))}
      </SongList>
      <MoreButton onClick={() => GoToPlayListPage()}>더보기</MoreButton>
    </PlaylistCardSmall>
  );
};

export default PlaylistCard;
