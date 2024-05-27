import styled from "styled-components";

const Container = styled.div`
  width: 100%;
  height: 100%;
  border-radius: 5%;
  background-color: black;
  background-image: linear-gradient(to top right, #fb96a5, #ffdae0);
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex-direction: column;
`;

const ListBox = styled.div`
  width: 90%;
  height: 15%;
  padding-left: 10px;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: row;
`;

const AlbumCover = styled.div`
  width: 20%;
  height: 80%;
  border-radius: 10%;
  background-color: black;
`;

const TextBox = styled.div`
  height: 80%;
  margin: 10px;
  display: flex;
  align-items: start;
  justify-content: space-evenly;
  flex-direction: column;
`;

const Title = styled.div<{ fontSize: string; margin: string }>`
  font-size: ${(props) => props.fontSize};
  margin: ${(props) => props.margin};
  font-family: "Bd";
  color: white;
`;

const PlayListBox = () => {
  const items = Array.from({ length: 7 }, (_, index) => `Item ${index + 1}`);

  return (
    <Container>
      {items.map((item, index) => (
        <ListBox>
          <AlbumCover></AlbumCover>
          <TextBox>
            <Title fontSize={"20px"}>밤양갱</Title>
            <Title fontSize={"10px"}>밤양갱</Title>
          </TextBox>
        </ListBox>
      ))}
    </Container>
  );
};

export default PlayListBox;
