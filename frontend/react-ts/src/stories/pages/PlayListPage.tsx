import styled from 'styled-components';
import { colors } from '../../styles/color';

import PlayListCard from '../components/PlayListCard';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { fetchGET, MAX_REISSUE_COUNT } from '../utils/fetchData';
import useStore from '../store/store';
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

const BlankDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100px;
`;

const Body = styled.div`
  margin-top: 130px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-x: hidden;
`;

const TitleArea = styled.div`
  width: 360px;
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
  margin-top: 20px;
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

  return (
    <Container>
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
          <Text fontFamily="Bd" fontSize="25px" margin="0px 10px 0px 0px">
            {author.username}'s PlayList
          </Text>
        </TitleArea>
        <PlayListArea>{playListData && <PlayListCard playlist={playListData?.tracks} isEditable={author.id === id ? true : false} playlistInfo={author}></PlayListCard>}</PlayListArea>
        <RecommendationArea>
          <TitleArea>
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" className="bi bi-lightbulb" viewBox="0 0 16 16">
              <path d="M2 6a6 6 0 1 1 10.174 4.31c-.203.196-.359.4-.453.619l-.762 1.769A.5.5 0 0 1 10.5 13a.5.5 0 0 1 0 1 .5.5 0 0 1 0 1l-.224.447a1 1 0 0 1-.894.553H6.618a1 1 0 0 1-.894-.553L5.5 15a.5.5 0 0 1 0-1 .5.5 0 0 1 0-1 .5.5 0 0 1-.46-.302l-.761-1.77a2 2 0 0 0-.453-.618A5.98 5.98 0 0 1 2 6m6-5a5 5 0 0 0-3.479 8.592c.263.254.514.564.676.941L5.83 12h4.342l.632-1.467c.162-.377.413-.687.676-.941A5 5 0 0 0 8 1" />
            </svg>
            <Text fontFamily="Bd" fontSize="25px" margin="0px 0px 0px 5px">
              Recommendation
            </Text>
          </TitleArea>
          <PlayListArea>{playListData && <PlayListCard playlist={playListData?.recommends} isEditable={false} playlistInfo={author}></PlayListCard>}</PlayListArea>
        </RecommendationArea>
        <BlankDiv></BlankDiv>
      </Body>
    </Container>
  );
}

export default PlayListPage;
