import styled from 'styled-components';
import { colors } from '../../styles/color';
import ColorThief from 'colorthief';
import { useRef, useState } from 'react';

const PlayListCardContainer = styled.div`
  width: 360px;
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
`;

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  height: auto;
  width: 60px;
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
  margin: 10px;
  overflow: hidden;
`;

const SongTextArea = styled.div`
  height: 80%;
  /* width: 100%; */
  width: 220px;
  display: flex;
  align-items: start;
  justify-content: space-between;
  flex-direction: column;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;
const Btn = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;

  width: 50px;
  height: 35px;
  border-radius: 10px;
  /* padding: 10px; */
  box-sizing: border-box;
  margin: 0px 5px;
  //box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
  opacity: 0.8;
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

interface PlaylistProps {
  playlist: SongData[] | RecommendSongData[];
  isEditable: boolean;
  playlistInfo: playlistInfo;
  setIsSearchModalOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setPlayListData: React.Dispatch<React.SetStateAction<PlayListData | undefined>>;
}

const PlayListEditCard = ({ playlist, isEditable, setIsSearchModalOpen, setPlayListData }: PlaylistProps) => {
  const [playlistGradient, setPlaylistGradient] = useState<string>();
  const albumCoverRef = useRef<HTMLImageElement | null>(null);

  const handleTrackRemove = (trackIdToRemove: number) => {
    setPlayListData(prevData => {
      if (!prevData) {
        // prevData가 undefined일 경우 아무것도 하지 않음
        return prevData;
      }

      // 기존 트랙 배열에서 삭제할 트랙을 필터링
      const updatedTracks = prevData.tracks.filter(track => track.trackId !== trackIdToRemove);

      console.log(updatedTracks);
      return {
        ...prevData,
        tracks: updatedTracks,
        recommends: prevData.recommends || [], // 추천은 그대로 유지
      };
    });
  };

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

  // const navigate = useNavigate();
  // const GoToEditPage = () => {
  //   navigate('/PlayListEditPage', { state: playlistInfo });
  // };

  return (
    <PlayListCardContainer>
      <PlayListInfoArea>
        <Text fontSize={'14px'} fontFamily="RG" margin="0px 100px 0px 0px " opacity="0.8">
          {playlist.length} songs
        </Text>
        {isEditable && (
          <EditBtn
            onClick={() => {
              setIsSearchModalOpen(true);
            }}
          >
            {/* <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill={colors.BG_grey} viewBox="0 0 16 16">
              <path fill-rule="evenodd" d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2" />
            </svg> */}
            <Text fontSize={'14px'} fontFamily="RG" margin="0px 0px 0px 5px " opacity="0.8">
              + 추가
            </Text>
          </EditBtn>
        )}
      </PlayListInfoArea>
      {playlist.map((song, index) => (
        <SongArea key={index}>
          <AlbumCover>
            <img
              ref={index === 0 ? albumCoverRef : null}
              src={song.trackCover}
              width="100%"
              height="100%"
              crossOrigin="anonymous"
              onLoad={handleImageLoad} // 이미지가 로드될 때 색상 추출
              alt={`Album Cover of ${song.title}`}
            ></img>
          </AlbumCover>
          <SongTextArea>
            <Title fontSize={'16px'} fontFamily="EB" margin="0px 0px 5px 0px">
              {song.title}
            </Title>
            <Title fontSize={'14px'} fontFamily="RG">
              {song.artistName}
            </Title>
          </SongTextArea>
          <Btn
            onClick={() => {
              handleTrackRemove(song.trackId);
            }}
          >
            <Text fontFamily="RG" fontSize="14px" margin="0px 0px 0px 4px">
              - 삭제
            </Text>
          </Btn>
        </SongArea>
      ))}
    </PlayListCardContainer>
  );
};

export default PlayListEditCard;
