import styled from "styled-components";
import logo from "../../img/logo.png";
import SearchBox from "../components/SearchBox";
import AlbumPostCard from "../components/AlbumPostCard";
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

const Header = styled.div`
  width: 100vw;
  height: 150px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
`;

const RowAlignArea = styled.div`
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

const WriterTextArea = styled.div`
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

const RightAlignArea = styled.div`
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
      <Header>
        <img src={logo} width="80px" height="80px"></img>
        <RowAlignArea>
          <Circle></Circle>
          <Title fontSize={"30px"}>AlbumPost</Title>
          <WriterTextArea>
            <Title>by김준호</Title>
          </WriterTextArea>
        </RowAlignArea>
      </Header>
      <SearchBox></SearchBox>
      {/* <AlbumPostCard></AlbumPostCard>  //TODO: props로 전달할 곡 제목 넣기 */}

      <RightAlignArea>
        <CompleteButton>
          <Title fontSize={"20px"}>완료</Title>
        </CompleteButton>
      </RightAlignArea>
    </Container>
  );
}

export default AlbumChatPostPage;
