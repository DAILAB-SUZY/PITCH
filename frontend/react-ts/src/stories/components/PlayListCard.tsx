import styled from 'styled-components';
import { colors } from '../../styles/color';
import ColorThief from 'colorthief';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const PlayListCardContainer = styled.div<{ gradient?: string }>`
  width: 360px;
  height: auto;
  border-radius: 12px;
  background-image: ${({ gradient }: { gradient?: string }) => gradient || 'linear-gradient(to top right, #989898, #f3f3f3)'};
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
}

const PlayListBox = ({ playlist, isEditable, playlistInfo }: PlaylistProps) => {
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

  const navigate = useNavigate();
  const GoToEditPage = () => {
    navigate('/PlayListEditPage', { state: playlistInfo });
  };

  return (
    <PlayListCardContainer gradient={playlistGradient}>
      <PlayListInfoArea>
        <Text fontSize={'14px'} fontFamily="Rg" margin="0px 100px 0px 0px " opacity="0.8">
          {playlist.length} songs
        </Text>
        {isEditable && (
          <EditBtn
            onClick={() => {
              GoToEditPage();
            }}
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill={colors.BG_grey} className="bi bi-list-ul" viewBox="0 0 16 16">
              <path
                fill-rule="evenodd"
                d="M5 11.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m-3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2m0 4a1 1 0 1 0 0-2 1 1 0 0 0 0 2m0 4a1 1 0 1 0 0-2 1 1 0 0 0 0 2"
              />
            </svg>
            <Text fontSize={'14px'} fontFamily="Rg" margin="0px 0px 0px 5px " opacity="0.8">
              수정
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
            <Title fontSize={'16px'} fontFamily="Bd" margin="0px 0px 5px 0px">
              {song.title}
            </Title>
            <Title fontSize={'14px'} fontFamily="Rg">
              {song.artistName}
            </Title>
          </SongTextArea>
        </SongArea>
      ))}
    </PlayListCardContainer>
  );
};

export default PlayListBox;
