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
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";

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
  interface PlayList {
    playlistId: number;
    trackId: number;
    title: string;
    artistName: string;
  }

  const [playList, setPlayList] = useState<PlayList[]>([]);

  const location = useLocation();
  const userId = location.state;

  const token = localStorage.getItem("login-token");
  const refreshToken = localStorage.getItem("login-refreshToken");
  const server = "http://203.255.81.70:8030";
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  const PlayListURL = `${server}/api/user/${userId}/playlist`;

  const fetchPlayList = async () => {
    if (token) {
      try {
        console.log(`fetching Playlist...`);
        const response = await fetch(PlayListURL, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setPlayList((prevList) => [...prevList, ...data]);
          console.log("fetched PlayList:");
          console.log(data);
        } else if (response.status === 401) {
          console.log("reissuing Token");
          const reissueToken = await fetch(reissueTokenUrl, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Refresh-Token": `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem("login-token", data.token);
          localStorage.setItem("login-refreshToken", data.refreshToken);
          fetchPlayList();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        console.log("finished");
      }
    }
  };

  useEffect(() => {
    fetchPlayList();
  }, []);

  // const playlist = [
  //   {
  //     playlistId: 0,
  //     trackId: 0,
  //     userId: 0,
  //     title: "Armageddon",
  //     artistName: "aespa",
  //     albumCover: cover1,
  //   },
  //   {
  //     playlistId: 1,
  //     trackId: 1,
  //     userId: 0,
  //     title: "ASAP",
  //     artistName: "NewJeans",
  //     albumCover: cover2,
  //   },
  //   {
  //     playlistId: 2,
  //     trackId: 2,
  //     userId: 0,
  //     title: "Get Lucky",
  //     artistName: "DaftPunk",
  //     albumCover: cover3,
  //   },
  //   {
  //     playlistId: 3,
  //     trackId: 3,
  //     userId: 0,
  //     title: "Blinding Light",
  //     artistName: "The Weeknd",
  //     albumCover: cover4,
  //   },
  //   {
  //     playlistId: 4,
  //     trackId: 4,
  //     userId: 0,
  //     title: "Don't look Back in Anger",
  //     artistName: "Oasis",
  //     albumCover: cover5,
  //   },
  //   {
  //     playlistId: 5,
  //     trackId: 5,
  //     userId: 0,
  //     title: "Girls",
  //     artistName: "aespa",
  //     albumCover: cover6,
  //   },
  //   {
  //     playlistId: 6,
  //     trackId: 6,
  //     userId: 0,
  //     title: "SUPERNOVA",
  //     artistName: "aespa",
  //     albumCover: cover1,
  //   },
  // ];
  return (
    <Container>
      <Header>
        <Nav page={2}></Nav>
      </Header>
      <Body>
        <TitleArea>
          <Text fontFamily="Bd" fontSize="20px">
            {userId}
          </Text>
        </TitleArea>
        <PlayListArea>
          <PlayListCard playlist={playList}></PlayListCard>
        </PlayListArea>
      </Body>
    </Container>
  );
}

export default PlayListPage;
