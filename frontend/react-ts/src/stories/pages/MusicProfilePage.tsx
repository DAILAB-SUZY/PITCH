import styled from "styled-components";
import { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { colors } from "../../styles/color";
import Nav from "../components/Nav";
import PlaylistCardMini from "../components/PlaylistCardMini";
import AlbumGrid from "../components/AlbumGrid";
import FavoriteArtistCard from "../components/FavoriteArtistCard";
import AlbumPostCard from "../components/AlbumPostCard";
import useStore from "../store/store";

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
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
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

interface MusicProfileData {
  userDetail: {
    id: number;
    username: string;
    profilePicture: string;
    dnas: [
      {
        dnaKey: number;
        dnaName: string;
      },
    ];
  };
  favoriteArtist: {
    artistName: string;
    albumName: string;
    trackName: string;
    artistCover: string;
    albumCover: string;
    trackCover: string;
  };
  bestAlbum: [
    {
      albumId: number;
      albumName: string;
      albumCover: string;
      score: number;
    },
  ];
  userDna: [
    {
      dnaName: string;
    },
  ];
  playlist: [
    {
      playlistId: number;
      trackId: number;
      title: string;
      artistName: string;
      trackCover: string;
    },
  ];
  followings: [
    {
      userId: number;
    },
  ];
  followers: [
    {
      userId: number;
    },
  ];
}

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-evenly;
  background-color: ${colors.BG_grey};
  width: 70px;
  height: 30px;
  border-radius: 15px;
  padding: 10px;
  box-sizing: border-box;
  margin-bottom: 50px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

function MusicProfilePage() {
  const [tabBtn, setTabBtn] = useState(1);
  const [musicProfileData, setMusicProfileData] = useState<MusicProfileData>();
  const [activityData, setActivityData] = useState();
  const navigate = useNavigate();
  const location = useLocation();
  const profileId = location.state;
  const GoToEditPage = (musicProfileData: MusicProfileData) => {
    navigate("/MusicProfileEditPage", { state: musicProfileData });
  };

  const { email, setEmail, name, setName, id, setId } = useStore();
  console.log(`id: ${id} / name: ${name}`);
  const server = "http://203.255.81.70:8030";
  let musiProfileUrl = `${server}/api/user/${profileId}/musicProfile`;
  let activityUrl = `${server}/api/user/${profileId}/musicProfile/activity`;
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const token = localStorage.getItem("login-token");
  const refreshToken = localStorage.getItem("login-refreshToken");

  const fetchData = async () => {
    if (token) {
      try {
        console.log("fetching...");
        const response = await fetch(musiProfileUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log("set PostList");
          const data = await response.json();
          console.log(data);
          setMusicProfileData(data);
        } else if (response.status === 401) {
          console.log("reissuing Token");
          const reissueToken = await fetch(reissueTokenUrl, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Refresh-Token": `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem("login-token", data.token);
          localStorage.setItem("login-refreshToken", data.refreshToken);
          fetchData();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        console.log("finished");
      }
    }
  };
  useEffect(() => {
    fetchData();
  }, []);

  const fetchActivityData = async () => {
    if (token) {
      try {
        console.log("fetching Activity...");
        const response = await fetch(activityUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log("set PostList");
          const data = await response.json();
          console.log(data);
          setActivityData(data);
        } else if (response.status === 401) {
          console.log("reissuing Token");
          const reissueToken = await fetch(reissueTokenUrl, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Refresh-Token": `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem("login-token", data.token);
          localStorage.setItem("login-refreshToken", data.refreshToken);
          fetchActivityData();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        console.log("finished");
      }
    }
  };

  return (
    <Container>
      <Header>
        <Nav page={2}></Nav>
      </Header>
      <Body>
        <ProfileHeaderArea>
          <ProfileLeftArea>
            <Circle>
              <img src={musicProfileData?.userDetail.profilePicture} width="100%" height="100%"></img>
            </Circle>
            {id !== musicProfileData?.userDetail.id && <FollowBtn>Follow</FollowBtn>}
          </ProfileLeftArea>
          <ProfileRightArea>
            <ProfileNameArea>
              <Text fontFamily="Bd" fontSize="30px" margin="0px 15px 0px 0px ">
                {musicProfileData?.userDetail.username}
              </Text>
            </ProfileNameArea>
            <ProfileTagArea>
              {/* <Badge>더뮤직슬레이어</Badge> */}
              {musicProfileData?.userDetail.dnas.map((dna) => <Tag key={dna.dnaKey}>#{dna.dnaName}</Tag>)}
            </ProfileTagArea>
          </ProfileRightArea>
        </ProfileHeaderArea>
        {musicProfileData?.playlist && (
          <PlaylistCardArea>
            <PlaylistCardMini playlist={musicProfileData.playlist} userDetail={musicProfileData.userDetail} />
          </PlaylistCardArea>
        )}
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
              onClick={() => {
                setTabBtn(2);
                fetchActivityData();
              }}
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
                  {musicProfileData?.followers !== null ? musicProfileData?.followers.length : 0}
                </Text>
              </FollowBox>
              <FollowBox>
                <Text fontFamily="Bd" fontSize="15px" margin="5px">
                  Following
                </Text>
                <Text fontFamily="Rg" fontSize="15px" margin="5px">
                  {musicProfileData?.followings !== null ? musicProfileData?.followings.length : 0}
                </Text>
              </FollowBox>
            </FollowArea>
            <BestAlbumArea>
              <Title>BestAlbum</Title>
              {musicProfileData?.bestAlbum && <AlbumGrid AlbumData={musicProfileData?.bestAlbum}></AlbumGrid>}
            </BestAlbumArea>
            <FavoriteArtistArea>
              <Title>Favorite Artist</Title>
              {musicProfileData?.favoriteArtist && (
                <FavoriteArtistCard FavoriteArtistData={musicProfileData.favoriteArtist}></FavoriteArtistCard>
              )}
            </FavoriteArtistArea>
            {musicProfileData && (
              <EditBtn onClick={() => GoToEditPage(musicProfileData)}>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="17"
                  height="17"
                  fill="currentColor"
                  className="bi bi-pencil-square"
                  viewBox="0 0 16 16"
                >
                  <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                  <path
                    fill-rule="evenodd"
                    d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                  />
                </svg>
                <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 4px">
                  수정
                </Text>
              </EditBtn>
            )}
          </>
        ) : (
          <>
            {" "}
            {activityData.length > 0 ? (
              activityData.map((albumPost) => (
                <AlbumPostCard
                  key={albumPost.postDetail.postId}
                  albumPost={albumPost}
                  // setAlbumPostList={setAlbumPostList}
                ></AlbumPostCard>
              ))
            ) : (
              <Text fontSize="15px" margin="150px 0px 0px 0px"></Text>
            )}
          </>
        )}
      </Body>
    </Container>
  );
}

export default MusicProfilePage;
