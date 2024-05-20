import styled from "styled-components";
import { colors } from "../../styles/color";
import logo from "../../img/logo.png";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
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
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: red;
`;

const AlbumPostContainer = styled.div`
  width: 100vw;
  height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: blue;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

function HomePage() {
  return (
    <Container>
      <HeaderContainer>
        <img src={logo} width="80px" height="80px"></img>
        <Title fontSize={"25px"}>이준석님 안녕하세요!</Title>
      </HeaderContainer>
      <LeftAlignContainer>
        <Title fontSize={"25px"}>Friend's Playlist</Title>
      </LeftAlignContainer>
      <PlaylistContainer></PlaylistContainer>
      <LeftAlignContainer>
        <Title fontSize={"25px"}>Album Post +</Title>
      </LeftAlignContainer>
      <AlbumPostContainer></AlbumPostContainer>
    </Container>
  );
}

export default HomePage;