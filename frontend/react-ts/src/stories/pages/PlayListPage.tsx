import styled from "styled-components";
import { colors } from "../../styles/color";
import logo from "../../img/logo.png";
import BottomNav from "../components/BottomNav";
import PlaylistCircle from "../components/PlaylistCircle";
import PlayListBox from "../components/PlayListBox";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  height: 90vh;
  width: 100vw;
  background-color: white;
  color: black;
  margin-bottom: 10vh;
`;

const HeaderContainer = styled.div`
  width: 100vw;
  height: 20vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
`;

const RowContainer = styled.div`
  width: 80vw;
  margin-right: 20vw;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex-direction: row;
`;

const RightAlignContainer = styled.div`
  width: 80vw;
  height: 4vh;
  display: flex;
  align-items: center;
  justify-content: end;
  flex-direction: row;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

const Circle = styled.div`
  width: 50px;
  height: 50px;
  border-radius: 100%;
  background-color: black;
`;

const ModBtn = styled.div`
  width: 20vw;
  height: 4vh;
  background-color: lightgrey;
  border-bottom-left-radius: 2vh;
  border-bottom-right-radius: 2vh;
  border-top-right-radius: 2vh;
  border-top-left-radius: 2vh;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex-direction: row;
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

const PlayList = styled.div`
  width: 90vw;
  height: 60vh;
`;

function PlayListPage() {
  return (
    <Container>
      <HeaderContainer>
        <img src={logo} width="80px" height="80px"></img>
        <RowContainer>
          <Circle></Circle>
          <Title fontSize={"25px"}>이준석's Playlist</Title>
        </RowContainer>
        <RightAlignContainer>
          <ModBtn>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              viewBox="0 0 16 16"
            >
              <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
              <path
                fill-rule="evenodd"
                d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
              />
            </svg>
            <Title fontSize={"16px"}>수정</Title>
          </ModBtn>
        </RightAlignContainer>
      </HeaderContainer>
      <PlayList>
        <PlayListBox></PlayListBox>
      </PlayList>
      <BottomNavContainer>
        <BottomNav></BottomNav>
      </BottomNavContainer>
    </Container>
  );
}

export default PlayListPage;
