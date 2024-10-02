import styled from "styled-components";
import { colors } from "../../styles/color";
import logo from "../../img/logo.png";
import Nav from "../components/Nav";
import PlayListCard from "../components/PlayListCard";
import cover1 from "../../img/aespa.webp";
import cover2 from "../../img/newjeans.png";
import cover3 from "../../img/daftpunk.png";
import cover4 from "../../img/weeknd.jpg";
import cover5 from "../../img/oasis.jpeg";
import cover6 from "../../img/aespa2.jpg";

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
  height: 60px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
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
  fontFamily: string;
  fontSize: string;
  margin: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  margin: ${(props) => props.margin};
`;

function PlayListPage() {
  const playlist = [
    {
      playlistId: 0,
      trackId: 0,
      userId: 0,
      title: "Armageddon",
      artistName: "aespa",
      albumCover: cover1,
    },
    {
      playlistId: 1,
      trackId: 1,
      userId: 0,
      title: "ASAP",
      artistName: "NewJeans",
      albumCover: cover2,
    },
    {
      playlistId: 2,
      trackId: 2,
      userId: 0,
      title: "Get Lucky",
      artistName: "DaftPunk",
      albumCover: cover3,
    },
    {
      playlistId: 3,
      trackId: 3,
      userId: 0,
      title: "Blinding Light",
      artistName: "The Weeknd",
      albumCover: cover4,
    },
    {
      playlistId: 4,
      trackId: 4,
      userId: 0,
      title: "Don't look Back in Anger",
      artistName: "Oasis",
      albumCover: cover5,
    },
    {
      playlistId: 5,
      trackId: 5,
      userId: 0,
      title: "Girls",
      artistName: "aespa",
      albumCover: cover6,
    },
    {
      playlistId: 6,
      trackId: 6,
      userId: 0,
      title: "SUPERNOVA",
      artistName: "aespa",
      albumCover: cover1,
    },
  ];
  return (
    <Container>
      <Header>
        <Nav page={2}></Nav>
      </Header>
      <Body>
        <TitleArea>
          <Text fontFamily="Bd" fontSize="20px">
            김준호's Playlist
          </Text>
        </TitleArea>
        <PlayListArea>
          <PlayListCard playlist={playlist}></PlayListCard>
        </PlayListArea>
      </Body>
    </Container>
  );
}

export default PlayListPage;
