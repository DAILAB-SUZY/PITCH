import styled from 'styled-components';
import { colors } from '../../styles/color';

import PlayListEditCard from '../components/PlayListEditCard';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import SearchTrackModal from '../components/SearchTrackModal';
import { fetchPOST, fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';
import Header from '../components/Header';
import ColorThief from 'colorthief';
const Container = styled.div<{ gradient?: string }>`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh;
  width: 100vw;

  color: black;

  background-image: ${({ gradient }: { gradient?: string }) => gradient || 'linear-gradient(to top right, #989898, #f3f3f3)'};
`;

const HiddenImage = styled.img`
  display: none;
`;

const Body = styled.div`
  margin-top: 70px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const TitleArea = styled.div`
  width: 360px;
  height: 40px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  padding-left: 10px;
  margin: 0px 0px 10px 0px;
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
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const Blur = styled.div`
  display: flex;
  position: absolute;
  width: 100vw;
  height: 100vh;
  z-index: 90;
  background-color: rgba(30, 30, 30, 0.5);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
`;

const ModalArea = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 100vw;
  height: 100vh;
`;

const ButtonContainer = styled.div`
  width: 100%;
  height: 5vh;
  margin: 10px 0px 10px 0px;
  display: flex;
  justify-content: space-between;
`;

const Btn = styled.div<{ bgcolor: string }>`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid ${colors.BG_lightgrey};
  width: 100px;
  height: 35px;
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
  margin: 0px 20px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
  color: ${colors.Font_black};
`;

const Text = styled.div<{
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
  color?: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  margin: ${props => props.margin};
  color: ${props => props.color};
`;

interface track {
  albumId: number;
  artistName: string;
  spotifyId: string;
  title: string;
  trackCover: string;
  trackId: number;
  trackOrder: number;
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
  const [isSearchModalOpen, setIsSearchModalOpen] = useState(false);

  const location = useLocation();
  const author: playlistInfo = location.state;

  const PlayListURL = `/api/user/${author.id}/playlist`;
  const PostPlayListURL = `/api/playlist`;

  const fetchPlayList = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, PlayListURL, MAX_REISSUE_COUNT).then(data => {
      setPlayListData(data);
    });
  };

  const postPlayList = async (token: string, refreshToken: string) => {
    const data = {
      spotifyTrackIds: playListData?.tracks.map(track => track.spotifyId),
    };
    fetchPOST(token, refreshToken, PostPlayListURL, data, MAX_REISSUE_COUNT).then(data => {
      setPlayListData(data);
      GoToPlayListPage(author);
    });
  };

  useEffect(() => {
    fetchPlayList(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  }, []);

  const navigate = useNavigate();
  const GoToPlayListPage = (author: playlistInfo) => {
    navigate('/PlayListPage', { state: author });
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
      <HiddenImage ref={albumCoverRef} src={playListData?.tracks[0].trackCover} alt="Album Cover" onLoad={handleImageLoad} crossOrigin="anonymous" />

      {isSearchModalOpen && (
        <Blur>
          <ModalArea>
            <SearchTrackModal searchingTopic={'track'} tracks={playListData?.tracks || []} setPlayListData={setPlayListData} setIsSearchModalOpen={setIsSearchModalOpen}></SearchTrackModal>
          </ModalArea>
        </Blur>
      )}
      <Header page={3}></Header>
      <Body>
        <TitleArea>
          {playListData ? (
            <Circle>
              <ProfileImageCircle src={author.profilePicture} alt="Profile" />
            </Circle>
          ) : (
            <Circle bgcolor={colors.BG_grey}></Circle>
          )}
          <Text fontFamily="EB" fontSize="25px" color={colors.Font_white}>
            내 PlayList 수정
          </Text>
        </TitleArea>
        <PlayListArea>
          {playListData && (
            <PlayListEditCard playlist={playListData.tracks} setPlayListData={setPlayListData} isEditable={true} playlistInfo={author} setIsSearchModalOpen={setIsSearchModalOpen}></PlayListEditCard>
          )}
        </PlayListArea>
        <ButtonContainer>
          <Btn
            bgcolor={colors.BG_lightpink}
            onClick={() => {
              GoToPlayListPage(author);
            }}
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill={colors.Font_white} className="bi bi-x" viewBox="0 0 16 16">
              <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708" />
            </svg>
            <Text color={colors.Font_white} fontFamily="SB" fontSize="15px" margin="0px 0px 0px 0px">
              취소
            </Text>
          </Btn>
          <Btn
            bgcolor={colors.BG_lightgrey}
            onClick={() => {
              postPlayList(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
            }}
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill={colors.Font_white} className="bi bi-check" viewBox="0 0 16 16">
              <path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z" />
            </svg>
            <Text color={colors.Font_white} fontFamily="SB" fontSize="15px" margin="0px 0px 0px 0px">
              저장
            </Text>
          </Btn>
        </ButtonContainer>
      </Body>
    </Container>
  );
}

export default PlayListPage;
