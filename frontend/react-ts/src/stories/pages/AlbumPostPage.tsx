import styled from "styled-components";
import { colors } from "../../styles/color";
import logo from "../../img/logo.png";
import AlbumPostCard from "../components/AlbumPostCard";
import { useRef, useState } from "react";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh; //auto;
  width: 100vw;
  background-color: white;
  color: black;
`;

// const Header = styled.div`
//   width: 100vw;
//   height: 400px;
//   margin-bottom: 20px;
//   display: flex;
//   align-items: center;
//   justify-content: flex-end;
//   flex-direction: column;
// `;

const Header = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Body = styled.div`
  margin-top: 100px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const PlaylistArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  justify-content: center;
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: white;
`;

const BottomNavArea = styled.div`
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

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

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
function AlbumPostPage() {
  return (
    <Container>
      <AlbumPostArea>
        <AlbumPostCard song={"cover1"}></AlbumPostCard>
        <AlbumPostCard song={"cover1"}></AlbumPostCard>
        <AlbumPostCard song={"cover1"}></AlbumPostCard>
      </AlbumPostArea>
    </Container>
  );
}

export default AlbumPostPage;
