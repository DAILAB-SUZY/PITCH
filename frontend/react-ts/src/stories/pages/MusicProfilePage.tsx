import styled from "styled-components";
import { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { colors } from "../../styles/color";
import Nav from "../components/Nav";
import PlaylistCardMini from "../components/PlaylistCardMini";
import AlbumGrid from "../components/AlbumGrid";
import FavoriteArtistCard from "../components/FavoriteArtistCard";
import AlbumPostCard from "../components/AlbumPostCard";
import AlbumChatCardWithAlbum from "../components/AlbumChatCardWithAlbum";
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

const Circle = styled.div<{ bgcolor?: string }>`
  width: 100px;
  height: 100px;
  border-radius: 100%;
  overflow: hidden;
  margin-bottom: 10px;
  background-color: ${(props) => props.bgcolor};
`;

const FollowBtn = styled.div<{ bgcolor: string; color: string }>`
  width: 90px;
  height: 20px;
  font-size: 13px;
  font-family: "Rg";
  color: ${(props) => props.color};
  background-color: ${(props) => props.bgcolor};
  border-color: ${colors.Main_Pink};
  border-style: solid;
  border-width: 2px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;

  /* &:hover {
    background-color: ${colors.Main_Pink};
    color: white;
  } */
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

const MusicDnaArea = styled.div`
  width: 100%;
  height: 110px;
`;

const Tag = styled.div<{ color: string }>`
  margin: 0px 10px 12px 0px;
  padding: 5px 12px;
  width: auto;
  height: auto;
  font-size: 15px;
  font-family: "Rg";
  color: ${(props) => props.color};
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
  width: 350px;
  margin-bottom: 20px;
`;
const TabBtn = styled.div<{ bgcolor: string; color: string }>`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-family: "Bd";
  width: 170px;
  height: 40px;
  border-radius: 5px;
  background-color: ${(props) => props.bgcolor};
  color: ${(props) => props.color};
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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
  padding: 10px 15px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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
  overflow: hidden;
`;

const Title = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
  width: 100vw;
  box-sizing: border-box;
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

const PlaylistCardSmall = styled.div`
  background: linear-gradient(to top right, #989898, #f3f3f3);
  border-radius: 12px;
  padding: 15px;
  /* margin-top: 20px; */
  width: 320px;
  height: 190px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: white;
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
`;

interface MusicProfileData {
  userDetail: {
    id: number;
    username: string;
    profilePicture: string;
    dnas: {
      dnaKey: number;
      dnaName: string;
    }[];
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
      spotifyId: string;
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

interface PostData {
  type: string;
  postDetail: {
    postId: number;
    content: string;
    createAt: number;
    updateAt: number;
    author: {
      id: number;
      username: string;
      profilePicture: string;
      dnas: {
        dnaKey: number;
        dnaName: string;
      }[];
    };
    album: {
      id: number;
      title: string;
      albumCover: string;
      artistName: string;
      genre: string;
    };
  };
  comments: {
    id: number;
    content: string;
    createdAt: number;
    updatedAt: number;
    likes: {
      id: number;
      username: string;
      profilePicture: string;
      dnas: {
        dnaKey: number;
        dnaName: string;
      }[];
    }[];
    childComments: {
      id: number;
      content: string;
      author: {
        id: number;
        username: string;
        profilePicture: string;
        dnas: {
          dnaKey: number;
          dnaName: string;
        }[];
      };
    }[];
    author: {
      id: number;
      username: string;
      profilePicture: string;
      dnas: {
        dnaKey: number;
        dnaName: string;
      }[];
    };
  }[];
  likes: {
    id: number;
    username: string;
    profilePicture: string;
    dnas: {
      dnaKey: number;
      dnaName: string;
    }[];
  }[];
}
[];

interface ChatData {
  type: string;
  albumDetail: {
    albumId: number;
    title: string;
    albumCover: string;
    artistName: string;
    genre: string;
  };
  albumChatCommentDetail: {
    albumChatCommentId: number;
    content: string;
    createAt: number;
    updateAt: number;
    likes: {
      author: {
        id: number;
        username: string;
        profilePicture: string;
        dnas: {
          dnaKey: number;
          dnaName: string;
        }[];
      };
      updateAt: string;
    }[];
    comments: {
      albumChatCommentId: number;
      content: string;
      createAt: string;
      updateAt: string;
      author: {
        id: number;
        username: string;
        profilePicture: string;
        dnas: {
          dnaKey: number;
          dnaName: string;
        }[];
      };
    }[];
    author: {
      id: number;
      username: string;
      profilePicture: string;
      dnas: {
        dnaKey: number;
        dnaName: string;
      }[];
    };
  };
}

interface ActivityDataList {
  postDetail: PostData[];
  albumChatDetail: ChatData[];
}

type mergeActivityData = PostData | ChatData;

type ActivityData = mergeActivityData[];

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
  margin-bottom: 80px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.1);
`;

function MusicProfilePage() {
  const [tabBtn, setTabBtn] = useState(1);
  const [musicProfileData, setMusicProfileData] = useState<MusicProfileData>();
  const [activityData, setActivityData] = useState<ActivityData>([]);
  const navigate = useNavigate();
  const location = useLocation();
  const profileId = location.state;
  const GoToEditPage = (musicProfileData: MusicProfileData) => {
    navigate("/MusicProfileEditPage", { state: musicProfileData.userDetail.id });
  };

  const [isFollowed, setIsFollowed] = useState<boolean>();

  const { email, setEmail, name, setName, id, setId } = useStore();
  console.log(`id: ${id} / name: ${name}`);
  const server = "http://203.255.81.70:8030";
  let musiProfileUrl = `${server}/api/user/${profileId}/musicProfile`;
  let activityUrl = `${server}/api/user/${profileId}/musicProfile/activity`;
  let followUrl = `${server}/api/user/follow/${profileId}`;
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

          // 내가 현재 musicprofile 사용자 follow 되어 있는지 판단
          if (data.followers.some((user: any) => user.userId === id)) {
            setIsFollowed(true);
            console.log("following this user");
          } else {
            console.log("NOT following this user");
            console.log(id);
            console.log(musicProfileData?.followers);
          }
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
          console.log("set Activity List");
          const data = await response.json();
          console.log(data);
          // setActivityData(data);
          // postDetail과 albumChatDetail 배열을 합치기
          //const mergedData = [...data.postDetail, ...data.albumChatDetail];
          const mergedData = [
            ...data.albumPostList.map((post: any) => ({ ...post, type: "post" as const })),
            ...data.albumCommentList.map((chat: any) => ({ ...chat, type: "chat" as const })),
          ];
          console.log("--before Sort--");
          console.log(mergedData);

          // createAt 기준으로 내림차순 정렬
          mergedData.sort((a, b) => {
            let stA;
            let stB;
            if (a.type === "post") {
              stA = a.postDetail.updateAt === null ? a.postDetail.createAt : a.postDetail.updateAt;
            } else if (a.type === "chat") {
              stA =
                a.albumChatCommentDetail.updateAt === null
                  ? a.albumChatCommentDetail.createAt
                  : a.albumChatCommentDetail.updateAt;
            }
            if (b.type === "post") {
              stB = b.postDetail.updateAt === null ? b.postDetail.createAt : b.postDetail.updateAt;
            } else if (b.type === "chat") {
              stB =
                b.albumChatCommentDetail.updateAt === null
                  ? b.albumChatCommentDetail.createAt
                  : b.albumChatCommentDetail.updateAt;
            }
            return stB - stA;
          });
          console.log("--after Sort--");
          console.log(mergedData); // 정렬된 데이터 확인
          setActivityData(mergedData);
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

  const fetchFollow = async () => {
    if (token) {
      try {
        console.log("fetching...");
        const response = await fetch(followUrl, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log("following complete");
          setIsFollowed(!isFollowed);
          const data = await response.json();
          console.log(data);
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
          fetchFollow();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        console.log("finished");
        fetchData();
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
            {musicProfileData ? (
              <Circle>
                <img src={musicProfileData?.userDetail.profilePicture} width="100%" height="100%"></img>
              </Circle>
            ) : (
              <Circle bgcolor={colors.BG_grey}></Circle>
            )}
            {id !== musicProfileData?.userDetail.id &&
              (isFollowed ? (
                <FollowBtn color={colors.BG_white} bgcolor={colors.Main_Pink} onClick={() => fetchFollow()}>
                  Following
                </FollowBtn>
              ) : (
                <FollowBtn color={colors.Main_Pink} bgcolor={colors.BG_white} onClick={() => fetchFollow()}>
                  Follow
                </FollowBtn>
              ))}
          </ProfileLeftArea>
          <ProfileRightArea>
            <ProfileNameArea>
              <Text fontFamily="Bd" fontSize="30px" margin="0px 15px 0px 0px ">
                {musicProfileData?.userDetail.username}
              </Text>
            </ProfileNameArea>
            <MusicDnaArea>
              {musicProfileData ? (
                musicProfileData?.userDetail.dnas.map((dna) => (
                  <Tag key={dna.dnaKey} color={colors.Font_black}>
                    #{dna.dnaName}
                  </Tag>
                ))
              ) : (
                <>
                  <Tag color={colors.BG_grey}>dnadna</Tag>
                  <Tag color={colors.BG_grey}>dnadnadna</Tag>
                  <Tag color={colors.BG_grey}>dnadnadna</Tag>
                  <Tag color={colors.BG_grey}>dnadna</Tag>
                </>
              )}
            </MusicDnaArea>
          </ProfileRightArea>
        </ProfileHeaderArea>
        {musicProfileData?.playlist ? (
          <PlaylistCardArea>
            <PlaylistCardMini playlist={musicProfileData.playlist} userDetail={musicProfileData.userDetail} />
          </PlaylistCardArea>
        ) : (
          <PlaylistCardArea>
            <PlaylistCardSmall></PlaylistCardSmall>
          </PlaylistCardArea>
        )}
        <MenuArea>
          <TabArea>
            <TabBtn
              bgcolor={tabBtn === 1 ? colors.Main_Pink : colors.BG_grey}
              color={tabBtn === 1 ? colors.BG_white : colors.Font_black}
              onClick={() => setTabBtn(1)}
            >
              Profile
            </TabBtn>
            <TabBtn
              bgcolor={tabBtn === 2 ? colors.Main_Pink : colors.BG_grey}
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
              {musicProfileData?.bestAlbum ? (
                <AlbumGrid AlbumData={musicProfileData?.bestAlbum}></AlbumGrid>
              ) : (
                <Text>아직 설정하지 않았습니다.</Text>
              )}
            </BestAlbumArea>
            <FavoriteArtistArea>
              <Title>Favorite Artist</Title>
              {musicProfileData?.favoriteArtist && (
                <FavoriteArtistCard FavoriteArtistData={musicProfileData.favoriteArtist}></FavoriteArtistCard>
              )}
            </FavoriteArtistArea>
            {musicProfileData && id === musicProfileData?.userDetail.id && (
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
                    fillRule="evenodd"
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
            {activityData?.length > 0 ? (
              activityData?.map((item, index) => {
                const { type, ...props } = item;
                if (type === "post") {
                  const albumPostProps = props as PostData; // 타입 캐스팅
                  console.log(albumPostProps);
                  return <AlbumPostCard key={index} albumPost={albumPostProps} />;
                } else if (type === "chat") {
                  const albumChatProps = props as ChatData; // 타입 캐스팅
                  return <AlbumChatCardWithAlbum key={index} comment={albumChatProps} />;
                }
              })
            ) : (
              <Text fontSize="15px" margin="150px 0px 0px 0px">
                {" "}
                작성한 글이 없습니다.
              </Text>
            )}
          </>
        )}
      </Body>
    </Container>
  );
}

export default MusicProfilePage;
