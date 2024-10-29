import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import PlayListEditCard from '../components/PlayListEditCard';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import ColorThief from 'colorthief';
import useStore from '../store/store';
import SearchTrackModal from '../components/SearchTrackModal';
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

const Header = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
const Body = styled.div`
  margin-top: 130px;
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

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;
  background-color: ${colors.BG_grey};
  width: auto;
  height: auto;
  border-radius: 50%;
  padding: 10px;
  box-sizing: border-box;

  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
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
  const { id } = useStore();
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  const PlayListURL = `${server}/api/user/${author.id}/playlist`;
  const PostPlayListURL = `${server}/api/playlist`;

  const fetchPlayList = async () => {
    if (token) {
      try {
        console.log(`fetching Playlist...`);
        const response = await fetch(PlayListURL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setPlayListData(data);
          console.log('fetched PlayList:');
          console.log(data);
        } else if (response.status === 401) {
          ReissueToken();
          fetchPlayList();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
      }
    }
  };
  const ReissueToken = async () => {
    console.log('reissuing Token');
    try {
      const response = await fetch(reissueTokenUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Refresh-Token': `${refreshToken}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('login-token', data.token);
        localStorage.setItem('login-refreshToken', data.refreshToken);
        setToken(data.token);
        setRefreshToken(data.refreshToken);
      } else {
        console.error('failed to reissue token', response.status);
      }
    } catch (error) {
      console.error('Refresh Token 재발급 실패', error);
    }
  };

  const postPlayList = async () => {
    if (token) {
      try {
        console.log(`post Playlist...`);
        const response = await fetch(PostPlayListURL, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            spotifyTrackIds: playListData?.tracks.map(track => track.spotifyId),
          }),
        });

        if (response.ok) {
          const data = await response.json();
          setPlayListData(data);
          console.log('posted PlayList:');
          console.log(data);
          GoToPlayListPage(author);
        } else if (response.status === 401) {
          console.log('reissuing Token');
          const reissueToken = await fetch(reissueTokenUrl, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Refresh-Token': `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem('login-token', data.token);
          localStorage.setItem('login-refreshToken', data.refreshToken);
          fetchPlayList();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
      }
    }
  };

  useEffect(() => {
    fetchPlayList();
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
      <Header>
        <Nav page={author.page}></Nav>
      </Header>
      <Body>
        <TitleArea>
          {playListData ? (
            <Circle>
              <img src={author.profilePicture} width="100%" height="100%" object-fit="cover"></img>
            </Circle>
          ) : (
            <Circle bgcolor={colors.BG_grey}></Circle>
          )}
          <Text fontFamily="Bd" fontSize="25px">
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
              postPlayList();
            }}
          >
            <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
              저장
            </Text>
          </Btn>
          <Btn
            bgcolor={colors.BG_grey}
            onClick={() => {
              GoToPlayListPage(author);
            }}
          >
            <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
              취소
            </Text>
          </Btn>
        </ButtonContainer>
      </Body>
    </Container>
  );
}

export default PlayListPage;
