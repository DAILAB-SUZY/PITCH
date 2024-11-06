import styled from 'styled-components';
import { colors } from '../../styles/color';
import ColorThief from 'colorthief';
import PlayListCard from '../components/PlayListCard';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import { fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';
import useStore from '../store/store';
import Header from '../components/Header';

const Container = styled.div<{ gradient?: string }>`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh;
  width: 100vw;
  /* background-color: white; */
  color: black;

  background-image: ${({ gradient }: { gradient?: string }) => gradient || 'linear-gradient(to top right, #444444, #bc82c9)'};
`;

const BlankDiv = styled.div`
  width: 100%;
  height: 100px;
  padding: 50px;
`;

const HiddenImage = styled.img`
  display: none;
`;

const Body = styled.div`
  margin-top: 55px;
  padding-top: 20px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-x: hidden;
  height: auto;
`;

const TitleArea = styled.div`
  width: 360px;
  height: 40px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  padding-left: 10px;
  margin: 0px 0px 0px 0px;
`;

const RecTitleArea = styled.div`
  width: 360px;
  height: 40px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  padding-left: 10px;
  margin: 0px 0px 0px 0px;
`;
const Circle = styled.div<{ bgcolor?: string }>`
  width: 35px;
  height: 35px;
  border-radius: 100%;
  overflow: hidden;
  margin-right: 10px;
  background-color: ${props => props.bgcolor};
  object-fit: cover;
`;

const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const PlayListArea = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const RecommendationArea = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 30px;
`;

const Text = styled.div<{
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
  opacity?: number;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  margin: ${props => props.margin};
  color: ${colors.BG_white};
  opacity: ${props => props.opacity};
  /* mix-blend-mode: difference; */
`;

interface track {
  playlistId: number;
  trackId: number;
  title: string;
  artistName: string;
  trackCover: string;
}

interface recommend {
  trackId: number;
  title: string;
  artistName: string;
  albumId: number;
  trackCover: string;
}
interface PlayListData {
  tracks: track[];
  recommends: recommend[];
}

interface playlistInfo {
  id: number;
  username: string;
  profilePicture: string;
  page: number;
}

function PlayListPage() {
  const [playListData, setPlayListData] = useState<PlayListData>();

  const location = useLocation();
  const author: playlistInfo = location.state;
  const { id } = useStore();

  const PlayListURL = `/api/user/${author.id}/playlist`;

  const fetchPlayList = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, PlayListURL, MAX_REISSUE_COUNT).then(data => {
      setPlayListData(data);
    });
  };

  useEffect(() => {
    fetchPlayList(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  }, []);

  const navigate = useNavigate();
  const GoToMusicProfilePage = (userId: number) => {
    navigate('/MusicProfilePage', { state: userId });
  };

  //
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
    <Container gradient={playlistGradient}>
      {playListData && playListData?.tracks.length > 0 ? (
        <HiddenImage ref={albumCoverRef} src={playListData?.tracks[0].trackCover} alt="Album Cover" onLoad={handleImageLoad} crossOrigin="anonymous" />
      ) : null}
      <Header page={3}></Header>
      <Body>
        <TitleArea
          onClick={() => {
            GoToMusicProfilePage(author.id);
          }}
        >
          {playListData ? (
            <Circle>
              <ProfileImageCircle src={author.profilePicture} alt="Profile" />
            </Circle>
          ) : (
            <Circle bgcolor={colors.BG_grey}></Circle>
          )}
          <Text fontFamily="EB" fontSize="25px" margin="0px 10px 0px 0px">
            {author.username}'s PlayList
          </Text>
        </TitleArea>
        <PlayListArea>
          {playListData && <PlayListCard playlist={playListData?.tracks} isEditable={author.id === id ? true : false} playlistInfo={author} isUserPlaylist={true}></PlayListCard>}
        </PlayListArea>
        <RecommendationArea>
          <RecTitleArea>
            <Text fontFamily="SB" fontSize="20px" margin="0px 0px 0px 5px">
              이런 음악은 어떠세요?
            </Text>
            {playListData?.tracks.length !== 0 ? (
              <Text fontFamily="RG" fontSize="15px" margin="10px 0px 0px 5px" opacity={0.7}>
                {author.username}님의 플레이리스트와 비슷한 음악을 추천해드려요.
              </Text>
            ) : (
              <Text fontFamily="RG" fontSize="15px" margin="10px 0px 0px 5px" opacity={0.7}>
                {author.username}님이 좋아할 음악을 추천해드려요.
              </Text>
            )}
          </RecTitleArea>
          <PlayListArea>{playListData && <PlayListCard playlist={playListData?.recommends} isEditable={false} playlistInfo={author} isUserPlaylist={false}></PlayListCard>}</PlayListArea>
        </RecommendationArea>
        <BlankDiv></BlankDiv>
      </Body>
    </Container>
  );
}

export default PlayListPage;
