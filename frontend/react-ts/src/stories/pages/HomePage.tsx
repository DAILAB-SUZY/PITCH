import styled from "styled-components";
import { colors } from "../../styles/color";
import logo from "../../img/logo.png";
import BottomNav from "../components/BottomNav";
import PlaylistCircle from "../components/PlaylistCircle";
import AlbumPost from "../components/AlbumPost";
import { useState } from "react";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: scroll;
  height: 100%;
  width: 100vw;
  background-color: white;
  color: black;
`;

const HeaderContainer = styled.div`
  width: 100vw;
  height: 20vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
`;

const LeftAlignContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: 85vw;
  margin: 10px;
  padding: 0;
`;

const PlaylistContainer = styled.div`
  width: 100vw;
  height: 10vh;
  display: flex;
  justify-content: center;
`;

const AlbumPostContainer = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: white;
`;

const BottomNavContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: white;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

type ChangeBlur = () => void;

const BackgroundBlur = styled.div``;
function HomePage() {
  const [blur, setBlur] = useState(false);
  const changeBlur: ChangeBlur = () => {
    setBlur(!blur);
  };
  return (
    <Container>
      <BackgroundBlur></BackgroundBlur>
      <HeaderContainer>
        <img src={logo} width="80px" height="80px"></img>
        <Title fontSize={"25px"}>이준석님 안녕하세요!</Title>
      </HeaderContainer>
      <LeftAlignContainer>
        <Title fontSize={"25px"}>Friend's Playlist</Title>
      </LeftAlignContainer>
      <PlaylistContainer>
        <PlaylistCircle></PlaylistCircle>
      </PlaylistContainer>
      <LeftAlignContainer>
        <Title fontSize={"25px"}>Album Post +</Title>
      </LeftAlignContainer>
      <AlbumPostContainer>
        <AlbumPost postClick={changeBlur}></AlbumPost>
        <AlbumPost postClick={() => changeBlur}></AlbumPost>
      </AlbumPostContainer>
      <BottomNavContainer>
        <BottomNav></BottomNav>
      </BottomNavContainer>
    </Container>
  );
}

export default HomePage;
