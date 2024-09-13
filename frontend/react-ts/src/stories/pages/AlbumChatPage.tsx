import styled from "styled-components";
import Nav from "../components/Nav";
import AlbumChatBox from "../components/AlbumChatCard";
import { useNavigate } from "react-router-dom";

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
  margin-top: 100px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const HeaderTilteArea = styled.div`
  width: 70vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
`;

const PostButton = styled.div`
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: row;
  background-color: #ff006a;
  color: white;
  border-radius: 50%;
`;

const ChatHeader = styled.div`
  width: 90vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
`;

const AlbumInfoArea = styled.div`
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

const AlbumDescriptArea = styled.div`
  width: 69vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: column;
`;
const AlbumNameArea = styled.div`
  width: 69vw;
  height: 5vh;
  display: flex;
  align-items: end;
  justify-content: flex-start;
  flex-direction: row;
`;

const ArtistInfoArea = styled.div`
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

const ChatArea = styled.div`
  width: 90vw;
  height: 70vh;
  overflow-y: auto;
`;

const ChatBox = styled.div`
  width: 90vw;
  height: 110px;
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
      <Header>
        <Nav page={3}></Nav>
      </Header>
      <Body>
        <HeaderTilteArea>
          <Title fontSize="40px">Album Chat&nbsp;&nbsp;&nbsp;</Title>
          <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 16 16">
            <path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1H4.414A2 2 0 0 0 3 11.586l-2 2V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12.793a.5.5 0 0 0 .854.353l2.853-2.853A1 1 0 0 1 4.414 12H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z" />
            <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
          </svg>
        </HeaderTilteArea>
        <PostButton>
          <Text onClick={GoToPostPage} fontSize="30px">
            +
          </Text>
        </PostButton>
        <AlbumInfoArea>
          <AlbumCover></AlbumCover>
          <AlbumDescriptArea>
            <AlbumNameArea>
              <Title fontSize="25px" margin="10px">
                I've IVE
              </Title>
              <Text fontSize="15px" margin="10px">
                댄스/발라드
              </Text>
            </AlbumNameArea>
            <ArtistInfoArea>
              <ArtistCircle></ArtistCircle>
              <Text fontSize="25px" margin="10px">
                IVE
              </Text>
            </ArtistInfoArea>
          </AlbumDescriptArea>
        </AlbumInfoArea>
        <ChatHeader>
          <Title fontSize="35px">CHAT</Title>
        </ChatHeader>
        <ChatArea>
          <ChatBox>
            {items.map((item, index) => (
              <AlbumChatBox></AlbumChatBox>
            ))}
          </ChatBox>
        </ChatArea>
        <Line></Line>
      </Body>
    </Container>
  );
}

export default AlbumChatPage;
