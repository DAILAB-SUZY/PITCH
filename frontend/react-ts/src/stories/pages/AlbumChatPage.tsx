import styled from "styled-components";
import BottomNav from "../components/BottomNav";

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

function AlbumChatPage() {
  return (
    <Container>
      <HeaderContainer></HeaderContainer>
      <BottomNav></BottomNav>
    </Container>
  );
}

export default AlbumChatPage;
