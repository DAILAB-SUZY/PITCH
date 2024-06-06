import styled from "styled-components";
import img from "../../img/yanggang.webp";

const Container = styled.div`
  width: 90vw;
  height: 60vh;
  border-radius: 3%;
  background-color: black;
  background-image: linear-gradient(to top right, #fb96a5, #ffdae0);
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex-direction: column;
  overflow-y: auto;
`;

const ListBox = styled.div`
  width: 85vw;
  height: 20vh;
  display: flex;
  align-items: center;
  justify-content: start;
  flex-direction: row;
`;

const AlbumCover = styled.div`
  width: 15vw;
  height: 7vh;
  border-radius: 10%;
  background-color: black;
  margin: 10px;
  overflow: hidden;
`;

const TextBox = styled.div`
  height: 80%;
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

const BtnBox = styled.div`
  width: 40vw;
  height: 5vh;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  position: absolute;
  position: fixed;
  bottom: 15vh;
  left: 7vw;
  right: 0;
`;

const CircleBtn = styled.div`
  width: 40px;
  height: 40px;
  background-color: white;
  border-radius: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const PlayListBox = () => {
  const items = Array.from({ length: 10 }, (_, index) => `Item ${index + 1}`);

  return (
    <Container>
      {items.map((item, index) => (
        <ListBox>
          <AlbumCover>
            <img src={img} width="100%" height="100%"></img>
          </AlbumCover>
          <TextBox>
            <Title fontSize={"20px"}>밤양갱</Title>
            <Title fontSize={"15px"}>비비</Title>
          </TextBox>
        </ListBox>
      ))}
      <BtnBox>
        <CircleBtn>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="25"
            fill="red"
            viewBox="0 0 16 16"
          >
            <path
              fill-rule="evenodd"
              d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
            />
          </svg>
        </CircleBtn>
        <CircleBtn>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="25"
            fill="lightblue"
            viewBox="0 0 16 16"
          >
            <path d="M2 2v13.5a.5.5 0 0 0 .74.439L8 13.069l5.26 2.87A.5.5 0 0 0 14 15.5V2a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2" />
          </svg>
        </CircleBtn>
        <CircleBtn>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="25"
            height="25"
            fill="currentColor"
            viewBox="0 0 16 16"
          >
            <path d="M11 2.5a2.5 2.5 0 1 1 .603 1.628l-6.718 3.12a2.5 2.5 0 0 1 0 1.504l6.718 3.12a2.5 2.5 0 1 1-.488.876l-6.718-3.12a2.5 2.5 0 1 1 0-3.256l6.718-3.12A2.5 2.5 0 0 1 11 2.5" />
          </svg>
        </CircleBtn>
      </BtnBox>
    </Container>
  );
};

export default PlayListBox;
