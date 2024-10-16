import styled from "styled-components";
import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { colors } from "../../styles/color";
import Nav from "../components/Nav";
import profile from "../../img/cat.webp";
import PlaylistCard from "../components/PlaylistCardMini";
import AlbumGrid from "../components/AlbumGrid";
import FavoriteArtistCard from "../components/FavoriteArtistCard";

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
  margin: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  margin: ${(props) => props.margin};
`;

const ProfileHeaderArea = styled.div`
  width: 100vw;
  height: 150px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding-top: 20px;
  margin-bottom: 20px;
`;

const ProfileLeftArea = styled.div`
  width: 35vw;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const Circle = styled.div`
  width: 100px;
  height: 100px;
  border-radius: 100%;
  background-color: black;
  overflow: hidden;
  margin-bottom: 10px;
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
  padding-top: 10px;
`;

const ProfileNameArea = styled.div`
  width: 100%;
  height: 40px;
`;

const ProfileTagArea = styled.div`
  width: 100%;
  height: 110px;
`;

const Badge = styled.div`
  background: linear-gradient(90deg, #a3d8f7, #72df9c);
  padding: 5px 12px;
  border-radius: 15px;
  color: white;
  font-size: 14px;
  margin: 0px 10px 12px 0px;
  display: inline-block;
`;

const Tag = styled.div`
  margin: 0px 10px 12px 0px;
  padding: 5px 12px;
  width: auto;
  height: auto;
  font-size: 15px;
  font-family: "Rg";
  color: black;
  background-color: ${colors.BG_grey};
  /* border-color: ${colors.Tag};
  border-style: solid;
  border-width: 2px; */
  border-radius: 20px;
  display: inline-flex;
  align-items: center;
  justify-content: center;

  &:hover {
    background-color: ${colors.Main_Pink};
    color: white;
  }
`;

const MenuArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 50px;
`;

const TabArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 360px;
  margin-bottom: 20px;
`;
const TabBtn = styled.div<{ bgColor: string; color: string }>`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-family: "Bd";
  width: 175px;
  height: 40px;
  border-radius: 5px;
  background-color: ${(props) => props.bgColor};
  color: ${(props) => props.color};
`;

const FollowArea = styled.div`
  width: 320px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;
  margin-bottom: 20px;
  background-color: ${colors.BG_grey};
  color: black;
  padding: 10px 20px;
`;

const FollowBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: auto;
  height: 100%;
`;

const BestAlbumArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
`;

const Title = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
  width: 100vw;
  padding-left: 20px;
  font-family: "Bd";
  font-size: 22px;
`;

const FavoriteArtistArea = styled.div`
  display: flex;
  width: 100vw;
  flex-direction: column;
`;

const PlaylistCardArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100vw;
  height: auto;
  margin-bottom: 35px;
`;

const BtnArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 50px;
  margin-bottom: 100px;
`;

const Btn = styled.div<{ bgColor: string }>`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;
  background-color: ${(props) => props.bgColor};
  width: 70px;
  height: 30px;
  border-radius: 15px;
  padding: 10px;
  box-sizing: border-box;
  margin: 0px 20px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

function MusicProfileEditPage() {
  const [tabBtn, setTabBtn] = useState(1);
  const navigate = useNavigate();
  const GoToMusicProfilePage = () => {
    navigate("/MusicProfilePage");
  };

  const location = useLocation();
  const musicProfileData = location.state;

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
              <Text fontFamily="Bd" fontSize="30px" margin="0px 15px 0px 0px ">
                김준호
              </Text>
            </ProfileNameArea>
            <ProfileTagArea>
              <Tag>#여유로운</Tag>
              <Tag>#Rock</Tag>
              <Tag>#RnB</Tag>
            </ProfileTagArea>
          </ProfileRightArea>
        </ProfileHeaderArea>
        <PlaylistCardArea>
          <PlaylistCard
            playlist={{
              playlist: { ...musicProfileData.playlist },
              userDetail: { ...musicProfileData.userDetail },
            }}
          ></PlaylistCard>
        </PlaylistCardArea>
        <MenuArea>
          <TabArea>
            <TabBtn
              bgColor={tabBtn === 1 ? colors.Main_Pink : colors.BG_grey}
              color={tabBtn === 1 ? colors.BG_white : colors.Font_black}
              onClick={() => setTabBtn(1)}
            >
              Profile
            </TabBtn>
            <TabBtn
              bgColor={tabBtn === 2 ? colors.Main_Pink : colors.BG_grey}
              color={tabBtn === 2 ? colors.BG_white : colors.Font_black}
              onClick={() => setTabBtn(2)}
            >
              Activity
            </TabBtn>
          </TabArea>
        </MenuArea>
        {tabBtn === 1 ? (
          <>
            <FollowArea>
              <FollowBox>
                <Text fontFamily="Bd" fontSize="15px" margin="5px">
                  Follower
                </Text>
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  123
                </Text>
              </FollowBox>
              <FollowBox>
                <Text fontFamily="Bd" fontSize="15px" margin="5px">
                  Follower
                </Text>
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  456
                </Text>
              </FollowBox>
            </FollowArea>
            <BestAlbumArea>
              <Title>BestAlbum</Title>
              <AlbumGrid AlbumData={musicProfileData}></AlbumGrid>
            </BestAlbumArea>
            <FavoriteArtistArea>
              <Title>Favorite Artist</Title>
              <FavoriteArtistCard FavoriteArtistData={musicProfileData.favoriteArtist}></FavoriteArtistCard>
            </FavoriteArtistArea>
            <BtnArea>
              <Btn bgColor={colors.BG_lightpink} onClick={() => GoToMusicProfilePage()}>
                <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
                  저장
                </Text>
              </Btn>
              <Btn bgColor={colors.BG_grey} onClick={() => GoToMusicProfilePage()}>
                <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
                  취소
                </Text>
              </Btn>
            </BtnArea>
          </>
        ) : (
          <></>
        )}
      </Body>
    </Container>
  );
}

export default MusicProfileEditPage;
