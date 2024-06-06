import styled from "styled-components";
import { colors } from "../../styles/color";
import logo from "../../img/logo.png";
import BottomNav from "../components/BottomNav";
import PlaylistCircle from "../components/PlaylistCircle";
import AlbumPost from "../components/AlbumPost";
import { useRef, useState } from "react";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  /* overflow: scroll; */
  height: auto;
  width: 100vw;
  background-color: white;
  color: black;
`;

const HeaderContainer = styled.div`
  width: 100vw;
  height: 400px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
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
  z-index: 2;
  background-color: white;
`;

const BottonNavBlankSpace = styled.div`
  height: 10vh;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

type ChangeBlur = () => void;

const BackgroundBlur = styled.div`
  display: none;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* background: linear-gradient(
    0deg,
    rgba(0, 0, 0, 0.8) 0%,
    rgba(0, 0, 0, 0) 100%
  ); */
  background-color: rgba(0, 0, 0, 0);
  backdrop-filter: blur(0px);
  /* display: none; */
  transition: all 0.5s;
`;
function HomePage() {
  const normalBG = "rgba(0, 0, 0, 0)";
  const blurBG = "rgba(0, 0, 0, 0.2)";
  const BGblurOn = "blur(10px)";
  const BGbluroOff = "blur(0px)";

  const blurRef = useRef<HTMLDivElement>(null);
  const [blur, setBlur] = useState(false);
  const changeBlur = () => {
    setBlur(!blur);
    const blurRefStyle = window.getComputedStyle(
      blurRef.current as HTMLDivElement
    );
    if (blurRefStyle.getPropertyValue("background-color") === normalBG) {
      blurRef.current?.style.setProperty("background-color", blurBG);
    } else if (blurRefStyle.getPropertyValue("background-color") === blurBG) {
      blurRef.current?.style.setProperty("background-color", normalBG);
    }
    if (blurRefStyle.getPropertyValue("backdrop-filter") === BGbluroOff) {
      blurRef.current?.style.setProperty("backdrop-filter", BGblurOn);
    } else if (blurRefStyle.getPropertyValue("backdrop-filter") === BGblurOn) {
      blurRef.current?.style.setProperty("backdrop-filter", BGbluroOff);
    }
    // if (blurRefStyle.getPropertyValue("display") === "none") {
    //   blurRef.current?.style.setProperty("display", "block");
    // } else if (blurRefStyle.getPropertyValue("display") === "block") {
    //   blurRef.current?.style.setProperty("display", "none");
    // }
  };
  const songname1 = "cover1";
  const songname2 = "cover2";
  return (
    <Container>
      <BackgroundBlur ref={blurRef} onClick={changeBlur}></BackgroundBlur>
      <HeaderContainer>
        <img src={logo} width="80px" height="80px"></img>
        <Title fontSize={"25px"}>김준호님 안녕하세요!</Title>
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
        <AlbumPost postClick={changeBlur} song={"cover1"}></AlbumPost>
        <AlbumPost postClick={changeBlur} song={songname2}></AlbumPost>
      </AlbumPostContainer>
      <BottomNavContainer>
        <BottomNav></BottomNav>
      </BottomNavContainer>
      <BottonNavBlankSpace></BottonNavBlankSpace>
    </Container>
  );
}

export default HomePage;
