import styled from "styled-components";
import BottomNav from "../components/BottomNav";
import AlbumChatBox from "../components/AlbumChatBox";
import { useNavigate } from "react-router-dom";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: white;
  color: black;
`;

const HeaderContainer = styled.div`
  width: 90vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
`;

const HeaderTilte = styled.div`
  width: 70vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
`;

const PostButton = styled.div`
  width: 10vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  flex-direction: row;
  background-color: red;
`;

const ChatHeaderContainer = styled.div`
  width: 90vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
`;

const AlbumContainer = styled.div`
  width: 90vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: row;
`;

const AlbumCover = styled.div`
  width: 21vw;
  height: 10vh;
  border-radius: 10%;
  background-color: black;
  overflow: hidden;
`;

const AlbumDescript = styled.div`
  width: 69vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: column;
`;
const AlbumName = styled.div`
  width: 69vw;
  height: 5vh;
  display: flex;
  align-items: end;
  justify-content: flex-start;
  flex-direction: row;
`;

const ArtistInfo = styled.div`
  width: 69vw;
  height: 5vh;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
`;

const ArtistCircle = styled.div`
  margin-left: 10px;
  width: 40px;
  height: 40px;
  background-color: black;
  border-radius: 100px;
`;

const Circle = styled.div`
  margin-left: 10px;
  width: 20px;
  height: 20px;
  background-color: black;
  border-radius: 100px;
`;

const Line = styled.div`
  width: 90vw;
  border-bottom: 1px;
`;

const ChatContainer = styled.div`
  width: 90vw;
  height: 70vh;
  overflow-y: auto;
`;

const ChatBox = styled.div`
  width: 90vw;
  height: 110px;
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

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;
const Text = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Rg";
`;

function AlbumChatPage() {
  const items = Array.from({ length: 20 }, (_, index) => `Item ${index + 1}`);
  const navigate = useNavigate();

  const GoToPostPage = () => {
    navigate("/AlbumChatPostPage");
  };

  return (
    <Container>
      <HeaderContainer>
        <HeaderTilte>
          <Title fontSize="40px">Album Chat&nbsp;&nbsp;&nbsp;</Title>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="30"
            height="30"
            viewBox="0 0 16 16"
          >
            <path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1H4.414A2 2 0 0 0 3 11.586l-2 2V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12.793a.5.5 0 0 0 .854.353l2.853-2.853A1 1 0 0 1 4.414 12H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z" />
            <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
          </svg>
        </HeaderTilte>
        <PostButton>
          <Text onClick={GoToPostPage} fontSize="50px">
            +
          </Text>
        </PostButton>
      </HeaderContainer>
      <AlbumContainer>
        <AlbumCover></AlbumCover>
        <AlbumDescript>
          <AlbumName>
            <Title fontSize="25px" margin="10px">
              I've IVE
            </Title>
            <Text fontSize="15px" margin="10px">
              댄스/발라드
            </Text>
          </AlbumName>
          <ArtistInfo>
            <ArtistCircle></ArtistCircle>
            <Text fontSize="25px" margin="10px">
              IVE
            </Text>
          </ArtistInfo>
        </AlbumDescript>
      </AlbumContainer>
      <ChatHeaderContainer>
        <Title fontSize="35px">CHAT</Title>
      </ChatHeaderContainer>
      <ChatContainer>
        <ChatBox>
          {items.map((item, index) => (
            <AlbumChatBox></AlbumChatBox>
          ))}
        </ChatBox>
      </ChatContainer>
      <Line></Line>
      <BottomNavContainer>
        <BottomNav></BottomNav>
      </BottomNavContainer>
    </Container>
  );
}

export default AlbumChatPage;
