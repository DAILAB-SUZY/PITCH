import styled from "styled-components";
import Visxtest from "../components/VisxTest";
import Visxtest2 from "../components/VisxTest2";
import Visxtest3 from "../components/VisxTest3";
import logo from "../../img/logo.png";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  /* overflow: scroll; */
  padding-top: 30px;
  height: 100vh;
  width: 100vw;
  background-color: white;
  color: black;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  margin-right: 15px;
`;

const CharacteristicArea = styled.div`
  display: flex;
  height: 20px;
`;

function MusicProfilePage() {
  return (
    <Container>
      <img src={logo} width="80px" height="80px"></img>
      <Text fontFamily="Mon" fontSize="40px">
        MUSIC PROFILE
      </Text>
      <CharacteristicArea></CharacteristicArea>
      <Visxtest width={350} height={200}></Visxtest>
      <Visxtest2 width={350} height={200}></Visxtest2>
      <Visxtest3 width={350} height={200}></Visxtest3>
    </Container>
  );
}

export default MusicProfilePage;
