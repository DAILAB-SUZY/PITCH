import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import PlayListEditCard from '../components/PlayListEditCard';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import ColorThief from 'colorthief';
import useStore from '../store/store';

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
  const token = localStorage.getItem('login-token');
  const refreshToken = localStorage.getItem('login-refreshToken');
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  const PlayListURL = `${server}/api/user/${author.id}/playlist`;

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
  const GoToEditPage = () => {
    navigate('/PlayListEditPage');
  };
  return (
    <Container>
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
        <PlayListArea>{playListData && <PlayListEditCard playlist={playListData?.tracks} isEditable={true} playlistInfo={author}></PlayListEditCard>}</PlayListArea>
      </Body>
    </Container>
  );
}

export default PlayListPage;
