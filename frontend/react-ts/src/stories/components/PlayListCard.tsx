import styled from "styled-components";
import { colors } from "../../styles/color";
import ColorThief from "colorthief";
import { useEffect, useRef, useState } from "react";
interface SongData {
  artistName: string;
  playlistId: number;
  title: string;
  trackCover: string;
  trackId: number;
}

interface PlaylistProps {
  playlist: SongData[];
}

const PlayListCardContainer = styled.div<{ gradient?: string }>`
  width: 360px;
  height: 70vh;
  border-radius: 12px;
  background-image: ${({ gradient }: { gradient?: string }) =>
    gradient || "linear-gradient(to top right, #989898, #f3f3f3)"};
  display: flex;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  overflow-y: auto;
  box-sizing: border-box;
  padding: 10px;
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

const Title = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
  color: white;
  width: 100%;
  height: 100%;
  white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis; // 글자가 넘어가면 말줄임(...) 표시
`;

const PlayListBox = ({ playlist }: PlaylistProps) => {
  const [playlistGradient, setPlaylistGradient] = useState<string>();
  const albumCoverRef = useRef<HTMLImageElement | null>(null);

  // ColorThief로 앨범 커버에서 색상 추출
  const extractColors = () => {
    const colorThief = new ColorThief();
    const img = albumCoverRef.current;

    let gradient = "#ddd"; // 기본 배경색 설정

    if (img) {
      const colors = colorThief.getPalette(img, 2); // 가장 대비되는 두 가지 색상 추출
      const primaryColor = `rgb(${colors[0].join(",")})`;
      const secondaryColor = `rgb(${colors[1].join(",")})`;
      gradient = `linear-gradient(135deg, ${primaryColor}, ${secondaryColor})`;
    }

    setPlaylistGradient(gradient);
  };

  const handleImageLoad = () => {
    extractColors(); // 이미지 로드 후 색상 추출
  };

  return (
    <PlayListCardContainer gradient={playlistGradient}>
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
            <Title fontSize={"20px"}>{song.title}</Title>
            <Title fontSize={"15px"}>{song.artistName}</Title>
          </SongTextArea>
        </SongArea>
      ))}
    </PlayListCardContainer>
  );
};

export default PlayListBox;
