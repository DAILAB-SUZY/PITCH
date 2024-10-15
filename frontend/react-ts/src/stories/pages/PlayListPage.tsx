import styled from "styled-components";
import { colors } from "../../styles/color";
import Nav from "../components/Nav";
import PlayListCard from "../components/PlayListCard";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import useStore from "../store/store";

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
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
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
    trackCover: string;
  }

  const [playList, setPlayList] = useState<PlayList[]>([]);

  const location = useLocation();
  const author = location.state;

  const token = localStorage.getItem("login-token");
  const refreshToken = localStorage.getItem("login-refreshToken");
  const server = "http://203.255.81.70:8030";
  const reissueTokenUrl = `${server}/api/auth/reissued`;

  const PlayListURL = `${server}/api/user/${author.id}/playlist`;

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

  return (
    <Container>
      <Header>
        <Nav page={author.page}></Nav>
      </Header>
      <Body>
        <TitleArea>
          <Text fontFamily="Bd" fontSize="20px">
            {author.username}'s PlayList
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
