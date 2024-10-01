import styled from "styled-components";
import logo from "../../img/logo.png";
import SearchBox from "../components/SearchBox";
import AlbumPostBox from "../components/AlbumPostBox";
import { colors } from "../../styles/color";

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
  height: 150px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
`;

const HeaderElement = styled.div`
  width: 90vw;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
`;
const Circle = styled.div`
  margin-left: 10px;
  width: 50px;
  height: 50px;
  background-color: black;
  border-radius: 100px;
`;

const ByTextBox = styled.div`
  width: 20vw;
  height: 50px;
  display: flex;
  align-items: end;
  justify-content: center;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;

const RightAlignBox = styled.div`
  width: 90vw;
  display: flex;
  align-items: center;
  justify-content: end;
`;

const CompleteButton = styled.div`
  width: 60px;
  height: 30px;
  border-radius: 15px;
  background-color: ${colors.BG_grey};
  display: flex;
  align-items: center;
  justify-content: center;
`;

function AlbumChatPostPage() {
  return (
    <Container>
      <HeaderContainer>
        <img src={logo} width="80px" height="80px"></img>
        <HeaderElement>
          <Circle></Circle>
          <Title fontSize={"30px"}>AlbumPost</Title>
          <ByTextBox>
            <Title>by김준호</Title>
          </ByTextBox>
        </HeaderElement>
      </HeaderContainer>
      <SearchBox></SearchBox>
      <AlbumPostBox></AlbumPostBox>
      <RightAlignBox>
        <CompleteButton>
          <Title fontSize={"20px"}>완료</Title>
        </CompleteButton>
      </RightAlignBox>
    </Container>
  );
}

export default AlbumChatPostPage;
