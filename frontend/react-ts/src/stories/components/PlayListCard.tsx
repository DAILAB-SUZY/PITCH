import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useNavigate } from 'react-router-dom';

const PlayListCardContainer = styled.div`
  width: 380px;
  height: auto;
  border-radius: 12px;
  display: flex;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  overflow-y: auto;
  box-sizing: border-box;
  padding: 10px;
`;

const PlayListInfoArea = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
  padding: 10px 10px 0px 10px;
  margin-bottom: 10px;
`;

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  height: auto;
  width: auto;
`;

const SongCard = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const SongArea = styled.div`
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: row;
  margin: 10px 0px 10px 0px;
`;

const AlbumCover = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 8px;
  background-color: black;
  margin: 10px 10px 10px 0px;
  overflow: hidden;
`;

const SongTextArea = styled.div`
  height: 80%;
  /* width: 100%; */
  width: 270px;
  display: flex;
  align-items: start;
  justify-content: space-between;
  flex-direction: column;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
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
  color: ${colors.BG_white};
`;

const Title = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  color: white;
  width: 100%;
  height: 100%;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;

const Line = styled.div`
  width: 340px;
  box-sizing: border-box;
  padding: 0px 20px 0px 20px;
  /* width: 320px; */
  height: 1px;
  opacity: 0.5;
  background-color: ${colors.Button_deactive};
`;

interface SongData {
  playlistId: number;
  trackId: number;
  title: string;
  artistName: string;
  trackCover: string;
}

interface RecommendSongData {
  trackId: number;
  title: string;
  artistName: string;
  albumId: number;
  trackCover: string;
}

interface playlistInfo {
  id: number;
  username: string;
  profilePicture: string;
  page: number;
}

interface PlaylistProps {
  playlist: SongData[] | RecommendSongData[];
  isEditable: boolean;
  playlistInfo: playlistInfo;
  isUserPlaylist: boolean;
}

const PlayListBox = ({ playlist, isEditable, playlistInfo, isUserPlaylist }: PlaylistProps) => {
  const navigate = useNavigate();
  const GoToEditPage = () => {
    navigate('/PlayListEditPage', { state: playlistInfo });
  };

  //const youtubeOathUrl = `/oauth2/callback/google`;
  const CreateYoutubePlaylist = async (playlistId: number) => {
    console.log(playlistId);
    // const token = localStorage.getItem('login-token') as string;
    // const refreshToken = localStorage.getItem('login-refreshToken') as string;
    window.location.href = `https://pitches.social/oauth2/authorization/google?state=${playlistInfo.id}`;
  };

  return (
    <PlayListCardContainer>
      <PlayListInfoArea>
        <Text fontSize="14px" fontFamily="RG" margin="0px 120px 0px 0px" opacity="0.8">
          {playlist.length} songs
        </Text>
        {isUserPlaylist && (
          <EditBtn>
            <Text fontSize="13px" fontFamily="RG" margin="0px 5px 0px 0px" opacity="0.8">
              Listen in
            </Text>
            <svg
              onClick={() => CreateYoutubePlaylist(playlistInfo.id)}
              xmlns="http://www.w3.org/2000/svg"
              width="17"
              height="17"
              fill={colors.BG_lightgrey}
              className="bi bi-youtube"
              viewBox="0 0 16 16"
            >
              <path d="M8.051 1.999h.089c.822.003 4.987.033 6.11.335a2.01 2.01 0 0 1 1.415 1.42c.101.38.172.883.22 1.402l.01.104.022.26.008.104c.065.914.073 1.77.074 1.957v.075c-.001.194-.01 1.108-.082 2.06l-.008.105-.009.104c-.05.572-.124 1.14-.235 1.558a2.01 2.01 0 0 1-1.415 1.42c-1.16.312-5.569.334-6.18.335h-.142c-.309 0-1.587-.006-2.927-.052l-.17-.006-.087-.004-.171-.007-.171-.007c-1.11-.049-2.167-.128-2.654-.26a2.01 2.01 0 0 1-1.415-1.419c-.111-.417-.185-.986-.235-1.558L.09 9.82l-.008-.104A31 31 0 0 1 0 7.68v-.123c.002-.215.01-.958.064-1.778l.007-.103.003-.052.008-.104.022-.26.01-.104c.048-.519.119-1.023.22-1.402a2.01 2.01 0 0 1 1.415-1.42c.487-.13 1.544-.21 2.654-.26l.17-.007.172-.006.086-.003.171-.007A100 100 0 0 1 7.858 2zM6.4 5.209v4.818l4.157-2.408z" />
            </svg>
          </EditBtn>
        )}
        {isEditable && (
          <EditBtn onClick={GoToEditPage}>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill={colors.BG_grey} className="bi bi-list-ul" viewBox="0 0 16 16">
              <path
                fillRule="evenodd"
                d="M5 11.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m-3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2m0 4a1 1 0 1 0 0-2 1 1 0 0 0 0 2m0 4a1 1 0 1 0 0-2 1 1 0 0 0 0 2"
              />
            </svg>
            <Text fontSize="14px" fontFamily="RG" margin="0px 0px 0px 5px" opacity="0.8">
              수정
            </Text>
          </EditBtn>
        )}
      </PlayListInfoArea>
      {playlist.map((song, index) => (
        <SongCard>
          <Line />
          <SongArea key={index}>
            <AlbumCover>
              <img src={song.trackCover} width="100%" height="100%" crossOrigin="anonymous" alt={`Album Cover of ${song.title}`}></img>
            </AlbumCover>
            <SongTextArea>
              <Title fontSize={'16px'} fontFamily="EB" margin="0px 0px 5px 0px">
                {song.title}
              </Title>
              <Title fontSize={'14px'} fontFamily="RG">
                {song.artistName}
              </Title>
            </SongTextArea>
          </SongArea>
        </SongCard>
      ))}
      <Line />
    </PlayListCardContainer>
  );
};

export default PlayListBox;
