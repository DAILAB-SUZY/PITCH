import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import ColorThief from 'colorthief';
import { useRef, useState } from 'react';

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
    dnas: {
      dnaKey: number;
      dnaName: string;
    }[];
  };
}

// 스타일 정의
const Container = styled.div`
  display: flex;
  justify-content: center;
  width: 100vw;
  /* padding: 20px; */
  box-sizing: border-box;
`;
const PlaylistCardSmall = styled.div<{ gradient?: string }>`
  background: ${({ gradient }: { gradient?: string }) => gradient || 'linear-gradient(to top right, #989898, #f3f3f3)'};
  border-radius: 12px;
  padding: 15px;
  width: 320px;
  height: 190px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: white;
  font-family: 'EB';
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
`;

const UserNameArea = styled.h2`
  width: 100%;
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-family: 'EB';
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
  font-family: 'SB';
`;

const Artist = styled.div`
  font-size: 14px;
  color: white;
  font-family: 'RG';
  opacity: 0.7;
`;

const Text = styled.div<{
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
  opacity?: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  margin: ${props => props.margin};
  opacity: ${props => props.opacity};

  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
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
  font-family: 'SB';
`;

// Playlist 컴포넌트
const PlaylistCard = ({ playlist, userDetail }: PlaylistProps) => {
  //const { email, setEmail, name, setName, id, setId } = useStore();
  const navigate = useNavigate();
  const GoToPlayListPage = (author: {}) => {
    navigate('/PlayListPage', { state: author });
  };

  const playlistInfo = {
    id: userDetail.id,
    username: userDetail.username,
    profilePicture: userDetail.profilePicture,
    page: 2,
  };

  const [playlistGradient, setPlaylistGradient] = useState<string>();
  const albumCoverRef = useRef<HTMLImageElement | null>(null);
  // ColorThief로 앨범 커버에서 색상 추출
  const extractColors = () => {
    const colorThief = new ColorThief();
    const img = albumCoverRef.current;

    let gradient = '#ddd'; // 기본 배경색 설정

    if (img) {
      const colors = colorThief.getPalette(img, 2); // 가장 대비되는 두 가지 색상 추출
      const primaryColor = `rgb(${colors[0].join(',')})`;
      const secondaryColor = `rgb(${colors[1].join(',')})`;
      gradient = `linear-gradient(135deg, ${primaryColor}, ${secondaryColor})`;
    }

    setPlaylistGradient(gradient);
  };

  const handleImageLoad = () => {
    extractColors(); // 이미지 로드 후 색상 추출
  };

  return (
    <Container>
      <PlaylistCardSmall gradient={playlistGradient} onClick={() => GoToPlayListPage(playlistInfo)}>
        <UserNameArea>{userDetail.username}'s Playlist</UserNameArea>
        <SongList>
          {playlist.slice(0, 2).map((song, index) => (
            <SongItem key={song.trackId}>
              <AlbumCover src={song.trackCover} alt="Album Cover" crossOrigin="anonymous" onLoad={handleImageLoad} ref={index === 0 ? albumCoverRef : null} />
              <SongInfo>
                <SongTitle>{song.title}</SongTitle>
                <Artist>{song.artistName}</Artist>
              </SongInfo>
            </SongItem>
          ))}
        </SongList>
        {playlist.length > 2 ? (
          <MoreButton>더보기</MoreButton>
        ) : playlist.length === 1 ? (
          <Text fontFamily="SB" fontSize="14px" margin="20px 0px 0px 0px" opacity="0.5">
            곡을 더 추가해보세요!
          </Text>
        ) : (
          <Text fontFamily="SB" fontSize="14px" margin="30px 0px 0px 0px" opacity="0.7">
            곡을 추가해보세요!
          </Text>
        )}
      </PlaylistCardSmall>
    </Container>
  );
};

export default PlaylistCard;
