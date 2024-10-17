import styled from "styled-components";
import { colors } from "../../styles/color";
import Nav from "../components/Nav";
import PlayListCard from "../components/PlayListCard";
import { useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import useStore from "../store/store";
import ColorThief from "colorthief";

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
`;
const Circle = styled.div<{ bgcolor?: string }>`
  width: 30px;
  height: 30px;
  border-radius: 100%;
  overflow: hidden;
  margin-right: 10px;
  background-color: ${(props) => props.bgcolor};
  object-fit: cover;
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
          setPlayListData(data);
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

  /////////////////////
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
    <Container>
      {playListData && (
        <img
          src={playListData?.tracks[0].trackCover}
          ref={albumCoverRef}
          style={{ display: "none" }}
          crossOrigin="anonymous" // CORS 문제 방지
          onLoad={handleImageLoad} // 이미지 로드 시 색상 추출
        />
      )}
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
          <Text fontFamily="Bd" fontSize="20px">
            {author.username}'s PlayList
          </Text>
        </TitleArea>
        <PlayListArea>
          {playListData && <PlayListCard playlist={playListData?.tracks}></PlayListCard>}
        </PlayListArea>
      </Body>
    </Container>
  );
}

export default PlayListPage;
