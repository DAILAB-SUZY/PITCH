import styled from "styled-components";
import img from "../../img/yanggang.webp";

interface SongData {
  playlistId: number;
  trackId: number;
  title: string;
  artistName: string;
  trackCover: string;
}

interface PlaylistProps {
  playlist: SongData[];
}

const PlayListCardContainer = styled.div`
  width: 360px;
  height: 70vh;
  border-radius: 12px;
  background-color: black;
  background-image: linear-gradient(to top right, #fb96a5, #ffdae0);
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
  display: flex;
  align-items: start;
  justify-content: space-between;
  flex-direction: column;
`;

const Title = styled.div<{ fontSize?: string; margin?: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
  color: white;
`;

const PlayListBox = ({ playlist }: PlaylistProps) => {
  return (
    <PlayListCardContainer>
      {playlist.map((song) => (
        <SongArea>
          <AlbumCover>
            <img src={song.trackCover} width="100%" height="100%"></img>
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
