import styled from "styled-components";
import Nav from "../components/Nav";
import AlbumChatCard from "../components/AlbumChatCard";
import { useNavigate } from "react-router-dom";
import cover1 from "../../img/aespa.webp";
import artistProfile from "../../img/aespaProfile.jpg";
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh;
  width: 100vw;
  background-color: white;
  color: black;
`;

const Header = styled.div`
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Body = styled.div`
  margin-top: 120px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const AlbumTitleArea = styled.div`
  position: relative;
  width: 100%;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  color: white;
  box-sizing: border-box;
`;

const ImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 100%;
  height: 200px;
  /* object-fit: cover; */
  z-index: 1;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-end;
  padding: 0px 0px 20px 20px;
  box-sizing: border-box;
  z-index: 3;
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

const Title = styled.div<{ fontSize: string; margin?: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
`;
const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: ${(props) => props.fontFamily};
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
        <AlbumTitleArea>
          <ImageArea>
            <img
              src={cover1}
              width="100%"
              height="auto"
              object-fit="cover"
              // z-index="1"
            ></img>
          </ImageArea>
          <GradientBG> </GradientBG>
          <TitleTextArea>
            <Text fontFamily="Bd" fontSize="30px">
              SUPERNOVA
            </Text>
            <Text fontFamily="Rg" fontSize="15px">
              AESPA
            </Text>
          </TitleTextArea>
        </AlbumTitleArea>
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
            {/* {items.map((item, index) => (
              <AlbumChatCard></AlbumChatCard>
            ))} */}
          </ChatBox>
        </ChatArea>
        <Line></Line>
      </Body>
    </Container>
  );
}

export default AlbumChatPage;
