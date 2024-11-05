import styled from 'styled-components';
import { colors } from '../../styles/color';

import PlayListEditCard from '../components/PlayListEditCard';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import SearchTrackModal from '../components/SearchTrackModal';
import { fetchPOST, fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';
import Header from '../components/Header';
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh;
  width: 100vw;
  background-color: white;
  color: black;
`;

const Body = styled.div`
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const TitleArea = styled.div`
  width: 100%;
  height: 40px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  padding-left: 10px;
  margin: 5px 0px 10px 0px;
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
  justify-content: space-evenly;
  background-color: ${props => props.bgcolor};
  width: 100px;
  height: 35px;
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
  margin: 0px 20px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

const Text = styled.div<{
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  margin: ${props => props.margin};
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
  return (
    <Container>
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
          <Text fontFamily="EB" fontSize="25px">
            {author.username}'s PlayList
          </Text>
        </TitleArea>
        <PlayListArea>
          {playListData && (
            <PlayListEditCard playlist={playListData.tracks} setPlayListData={setPlayListData} isEditable={true} playlistInfo={author} setIsSearchModalOpen={setIsSearchModalOpen}></PlayListEditCard>
          )}
        </PlayListArea>
        <ButtonContainer>
          <Btn
            bgcolor={colors.Button_green}
            onClick={() => {
              postPlayList(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
            }}
          >
            <Text fontFamily="RG" fontSize="15px" margin="0px 0px 0px 4px">
              저장
            </Text>
          </Btn>
          <Btn
            bgcolor={colors.BG_grey}
            onClick={() => {
              GoToPlayListPage(author);
            }}
          >
            <Text fontFamily="RG" fontSize="15px" margin="0px 0px 0px 4px">
              취소
            </Text>
          </Btn>
        </ButtonContainer>
      </Body>
    </Container>
  );
}

export default PlayListPage;
