import styled from "styled-components";
import logo from "../../img/logo.png";
import { colors } from "../../styles/color";
import Nav from "../components/Nav";
import profile from "../../img/cat.webp";

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
  margin-top: 110px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  margin-right: 15px;
`;

const Name = styled.div<{
  fontFamily: string;
  fontSize: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  margin-right: 15px;
`;

const ProfileHeaderArea = styled.div`
  width: 100vw;
  height: 150px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding-top: 20px;
`;

const ProfileLeftArea = styled.div`
  width: 35vw;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
`;

const Circle = styled.div`
  width: 100px;
  height: 100px;
  border-radius: 100%;
  background-color: black;
  overflow: hidden;
`;

const FollowBtn = styled.div`
  width: 80px;
  height: 20px;
  font-size: 15px;
  font-family: "Rg";
  color: ${colors.Main_Pink};
  background-color: white;
  border-color: ${colors.Main_Pink};
  border-style: solid;
  border-width: 2px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    background-color: ${colors.Main_Pink};
    color: white;
  }
`;

const ProfileRightArea = styled.div`
  width: 65vw;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
`;

const ProfileNameArea = styled.div`
  width: 100%;
  height: 40px;
`;

const ProfileTagArea = styled.div`
  width: 100%;
  height: 110px;
`;

const Tag = styled.div`
  margin: 5px;
  width: auto;
  height: auto;
  font-size: 15px;
  font-family: "Rg";
  color: black;
  background-color: ${colors.Tag};
  /* border-color: ${colors.Tag};
  border-style: solid;
  border-width: 2px; */
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    background-color: ${colors.Main_Pink};
    color: white;
  }
`;

const CharacteristicArea = styled.div`
  display: flex;
  height: 20px;
`;

function MusicProfilePage() {
  return (
    <Container>
      <Header>
        <Nav page={2}></Nav>
      </Header>
      <Body>
        <ProfileHeaderArea>
          <ProfileLeftArea>
            <Circle>
              <img src={profile} width="100%" height="100%"></img>
            </Circle>
            <FollowBtn>Follow</FollowBtn>
          </ProfileLeftArea>
          <ProfileRightArea>
            <ProfileNameArea>
              <Name fontFamily="Bd" fontSize="30px">
                김준호
              </Name>
            </ProfileNameArea>
            <ProfileTagArea>
              <Tag>#tag</Tag>
              <Tag>#tag</Tag>
              <Tag>#tag</Tag>
              <Tag>#tag</Tag>
            </ProfileTagArea>
          </ProfileRightArea>
        </ProfileHeaderArea>
        <CharacteristicArea></CharacteristicArea>
      </Body>
    </Container>
  );
}

export default MusicProfilePage;
