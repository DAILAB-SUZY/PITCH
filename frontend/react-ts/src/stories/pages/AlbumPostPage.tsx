import styled from "styled-components";
import { colors } from "../../styles/color";
import { useRef, useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AlbumChatCard from "../components/AlbumChatCard";
import useStore from "../store/store";
import useAlbumPostStore from "../store/albumPostStore";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  height: 100vh; //auto;
  width: 100vw;
  background-color: white;
  color: ${colors.Font_black};
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  background-color: white;
`;

const AlbumTitleArea = styled.div`
  position: relative;
  width: 100vw;
  height: 80vw;
  /* padding: 0px 0px 20px 10px; */
  /* z-index: 3; */
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
  width: 100vw;
  height: 80vw;
  /* object-fit: cover; */
  z-index: 1;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100vw;
  height: 80vw;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px 10px 0px 0px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
  /* position: absolute;
  bottom: 10px;
  left: 10px; */
  position: sticky;
  top: 10px;
  width: 100%;
  height: auto;
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
  padding: 0px 0px 20px 20px;
  box-sizing: border-box;
  z-index: 3;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color: string;
  margin: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  color: ${(props) => props.color};
  margin: ${(props) => props.margin};
  max-width: 280px;

  // 두 줄 이상일 때 '...' 처리
  display: -webkit-box;
  -webkit-line-clamp: 2; // 2줄까지만 표시
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const PostArea = styled.div`
  display: flex;
  width: 100vw;
  height: auto;
  /* overflow: hidden; */
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  margin: 0px;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
`;
const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: "Rg";
  margin: 0px 0px 2px 10px;
  color: ${colors.Font_grey};
`;
const ProfileTextArea = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
`;
const ProfileName = styled.div`
  display: flex;
  font-size: 20px;
  font-family: "Rg";
  margin-left: 10px;
  color: ${colors.Font_black};
`;
const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 30px;
  height: 30px;
  border-radius: 50%;
`;

const ProfileImgTextArea = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
`;

const EditBtn = styled.div`
  display: flex;
  position: relative;
`;

const DropdownMenu = styled.div`
  position: absolute;
  top: 25px;
  right: 0;
  width: 70px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background-color: white;
  border-radius: 7px;
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.3);
  z-index: 10;
  overflow: hidden;
`;

const DropdownItem = styled.div`
  padding: 10px;
  width: 70px;
  display: flex;
  justify-content: center;
  cursor: pointer;
  &:hover {
    background-color: ${colors.BG_grey};
  }
`;
const RowAlignArea = styled.div`
  display: flex;
  width: 90vw;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0px;
  margin: 10px;
`;
const PostContentArea = styled.div`
  display: flex;
  width: 100%;
  height: auto;
  box-sizing: border-box;

  /* white-space: nowrap; */
  /* text-overflow: ellipsis; */
  white-space: pre-wrap;
  line-height: 150%;

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: "Rg";
  padding: 0px 10px;
  margin: 10px 0px 20px 0px;

  transition: height ease 0.7s;
`;

const ButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

const ChatArea = styled.div`
  width: 100vw;
  height: 70vh;
  overflow-y: scroll;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
`;

interface albumPost {
  postDetail: {
    postId: number;
    content: string;
    createAt: number;
    updateAt: number;
    author: {
      id: number;
      username: string;
      profilePicture: string;
      dnas: [
        {
          dnaKey: number;
          dnaName: string;
        }[],
      ];
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
    }[];
    childComments: {
      id: number;
      content: string;
      author: {
        id: number;
        username: string;
        profilePicture: string;
      };
    }[];
    author: {
      id: number;
      username: string;
      profilePicture: string;
    };
  }[];
  likes: {
    id: number;
    username: string;
    profilePicture: string;
  }[];
}

function AlbumPostPage() {
  const location = useLocation();
  const [albumPost, setAlbumPost] = useState<albumPost>();
  const [albumPostId, setAlbumPostId] = useState(location.state);
  const server = "http://203.255.81.70:8030";
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem("login-token"));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem("login-refreshToken"));
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement | null>(null);
  const navigate = useNavigate();
  const GoToHomePage = () => {
    navigate("/Home");
  };
  const GoToAlbumPostEditPage = () => {
    let album;
    if (albumPost) {
      album = {
        albumArtist: {
          artistId: null,
          imageUrl: null,
          name: albumPost.postDetail.album.artistName,
        },
        albumId: albumPost.postDetail.album.id,
        imageUrl: albumPost.postDetail.album.albumCover,
        name: albumPost.postDetail.album.title,
        total_tracks: null,
        release_date: null,
        postContent: albumPost.postDetail.content,
        postId: albumPost.postDetail.postId,
      };
    }
    navigate("/AlbumPostEditPage", { state: album });
  };

  const fetchAlbumPost = async () => {
    console.log("start fetching albumPost...");
    let albumPostUrl = `${server}/api/album/post/${albumPostId}`;
    console.log(albumPostUrl);
    if (token) {
      try {
        console.log("fetching...");
        const response = await fetch(albumPostUrl, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        console.log("fetching...complete");

        if (response.ok) {
          console.log("set Post");
          const data = await response.json();
          console.log(data);
          setAlbumPost(data); // 기존 데이터에 새로운 데이터를 추가
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
          fetchAlbumPost();
        } else {
          console.error("Failed to fetch AlbumPost data:", response.status);
        }
      } catch (error) {
        console.error("Error fetching the JSON file:", error);
      } finally {
        console.log("fetching Complete: ", albumPost);
      }
    }
  };

  useEffect(() => {
    fetchAlbumPost();
  }, []);

  // Post 시간 계산에 필요한 상태 관리
  const [timeAgo, setTimeAgo] = useState<string>("");
  useEffect(() => {
    if (albumPost) {
      console.log("getting time from post");
      const CreateTime = albumPost.postDetail.createAt;
      const UpdatedTime = albumPost.postDetail.updateAt;

      const updateTimeAgo = () => {
        if (!UpdatedTime) {
          setTimeAgo(formatTimeAgo(CreateTime));
        } else {
          setTimeAgo(formatTimeAgo(UpdatedTime));
        }
      };
      updateTimeAgo();
    }
  }, [albumPost]);

  const formatTimeAgo = (unixTimestamp: number): string => {
    console.log("time calculating");
    const currentTime = Math.floor(Date.now() / 1000); // 현재 시간 (초)
    const timeDifference = currentTime - Math.floor(unixTimestamp); // 경과 시간 (초)

    const minutesAgo = Math.floor(timeDifference / 60); // 경과 시간 (분)
    const hoursAgo = Math.floor(timeDifference / 3600); // 경과 시간 (시간)
    const daysAgo = Math.floor(timeDifference / 86400); // 경과 시간 (일)

    if (minutesAgo < 60) {
      return `${minutesAgo}분 전`;
    } else if (hoursAgo < 24) {
      return `${hoursAgo}시간 전`;
    } else {
      return `${daysAgo}일 전`;
    }
  };
  /////////////

  const { email, setEmail, name, setName, id, setId } = useStore();

  // Post 좋아요 상태 확인
  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);

  useEffect(() => {
    if (albumPost) {
      setLikesCount(albumPost.likes.length);
      if (albumPost.likes.some((like: any) => like.id === id)) {
        setIsPostLiked(true);
      }
      console.log(`좋아요 상태 : ${isPostLiked} / 좋아요 개수 : ${likesCount}`);
    }
  }, [albumPost]);

  useEffect(() => {
    if (albumPost) {
      console.log(`--좋아요 상태 : ${isPostLiked} / 좋아요 개수 : ${likesCount}`);
    }
  }, [isPostLiked, likesCount]);

  // 좋아요 상태 변경 함수
  const changePostLike = async () => {
    console.log("changing Like");
    if (albumPost) {
      if (isPostLiked) {
        // 이미 좋아요를 눌렀다면 좋아요 취소
        setIsPostLiked(false);
        setLikesCount(likesCount - 1);
        albumPost.likes = albumPost.likes.filter((like: any) => like.id !== id);
      } else {
        // 좋아요 누르기
        setIsPostLiked(true);
        setLikesCount(likesCount + 1);
        albumPost.likes.push({
          id: id,
          username: name,
          // TODO: profile 이미지 링크 추가
          profilePicture: "string",
        });
      }
      fetchLike();
      // like 데이터 POST 요청
    }
  };

  const fetchLike = async () => {
    const PostLikeUrl =
      server + "/api/album/post/" + (albumPost ? albumPost.postDetail.postId : "") + "/like";
    if (token) {
      console.log("fetching Like Data");
      try {
        const response = await fetch(PostLikeUrl, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const data = await response.json();
          setAlbumPost(data);
          // replaceAlbumPostById(albumPostId, data);
          if (data.likes.some((like: any) => like.id === id)) {
            console.log("like 추가");
          } else {
            console.log("like 삭제");
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
          setToken(data.token);
          localStorage.setItem("login-refreshToken", data.refreshToken);
          setRefreshToken(data.refreshToken);
          fetchLike();
        } else {
          console.error("Failed to fetch data:", response.status);
        }
      } catch (error) {
        console.error("like 실패:", error);
      }
    }
  };

  // 수정/삭제 버튼
  const editMenu = () => {
    console.log("edit");
    setIsDropdownOpen(!isDropdownOpen);
  };

  // 삭제요청
  const deletePost = async () => {
    const PostDeleteUrl = server + "/api/album/post/" + (albumPost ? albumPost.postDetail.postId : "");
    if (token) {
      if (albumPost) {
        console.log(`delete id: ${albumPost.postDetail.postId}Post...`);
      }
      try {
        const response = await fetch(PostDeleteUrl, {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const data = await response.json();
          setAlbumPost(data);
          console.log("deleted");
          GoToHomePage();
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
          setToken(data.token);
          localStorage.setItem("login-refreshToken", data.refreshToken);
          setRefreshToken(data.refreshToken);
          deletePost();
        } else {
          console.error("Failed to delete data:", response.status);
        }
      } catch (error) {
        console.error("delete 실패:", error);
      }
    }
  };

  if (!albumPost) {
    return <div>Loading...</div>;
  }

  return (
    <Container>
      {albumPost && (
        <>
          <AlbumPostArea>
            {/*  TODO: 스크롤 되다가 일부 남기고 멈추기 */}
            <AlbumTitleArea>
              <ImageArea>
                <img
                  src={albumPost.postDetail.album.albumCover}
                  width="100%"
                  object-fit="cover"
                  // z-index="1"
                ></img>
              </ImageArea>
              <GradientBG> </GradientBG>
              <TitleTextArea>
                <Text fontFamily="Bd" fontSize="30px" margin="0px" color={colors.BG_white}>
                  {albumPost.postDetail.album.title}
                </Text>
                <Text fontFamily="Rg" fontSize="20px" margin="0px 0px 2px 10px" color={colors.BG_white}>
                  {albumPost.postDetail.album.artistName}
                </Text>
              </TitleTextArea>
            </AlbumTitleArea>
            <PostArea>
              <ProfileArea>
                <ProfileImgTextArea>
                  <ProfileImage>
                    <img src={albumPost.postDetail.author.profilePicture}></img>
                  </ProfileImage>
                  <ProfileTextArea>
                    <ProfileName>{albumPost.postDetail.author.username}</ProfileName>
                    <PostUploadTime> {timeAgo} </PostUploadTime>
                  </ProfileTextArea>
                </ProfileImgTextArea>
                {albumPost.postDetail.author.id === id && (
                  <EditBtn ref={dropdownRef}>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="20"
                      height="20"
                      fill={colors.Font_grey}
                      className="bi bi-three-dots"
                      viewBox="0 0 16 16"
                      onClick={() => editMenu()} // 클릭 시 토글
                    >
                      <path d="M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3m5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3" />
                    </svg>
                    {isDropdownOpen && (
                      <DropdownMenu>
                        <DropdownItem onClick={() => GoToAlbumPostEditPage()}>수정</DropdownItem>
                        <DropdownItem onClick={() => deletePost()}>삭제</DropdownItem>
                      </DropdownMenu>
                    )}
                  </EditBtn>
                )}
              </ProfileArea>
              <PostContentArea>{albumPost.postDetail.content}</PostContentArea>
              <ButtonArea>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="14"
                  height="14"
                  fill={isPostLiked ? colors.Button_active : colors.Button_deactive}
                  className="bi bi-heart-fill"
                  viewBox="0 0 16 16"
                  onClick={() => changePostLike()}
                >
                  <path
                    fillRule="evenodd"
                    d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
                  />
                </svg>
                <Text fontFamily="Rg" fontSize="14px" color="grey" margin="0px 20px 0px 3px">
                  좋아요 {albumPost.likes.length}개
                </Text>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="14"
                  height="14"
                  fill="grey"
                  className="bi bi-chat-right-text-fill"
                  viewBox="0 0 16 16"
                  style={{ strokeWidth: 6 }}
                >
                  <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
                  <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
                </svg>
                <Text fontFamily="Rg" fontSize="14px" color="grey" margin="0px 0px 0px 3px">
                  답글 {albumPost.comments.length}개
                </Text>
              </ButtonArea>
            </PostArea>
          </AlbumPostArea>
          <ChatArea>
            <RowAlignArea>
              <Text fontFamily="Bd" fontSize="30px" margin="0px" color="black">
                Comment
              </Text>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="22"
                height="22"
                fill="currentColor"
                className="bi bi-pencil-square"
                viewBox="0 0 16 16"
                //  TODO: 댓글 작성 기능 구현
                // onClick={ 댓글 작성 페이지로 이동 }
              >
                <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                <path
                  fillRule="evenodd"
                  d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                />
              </svg>
            </RowAlignArea>
            {albumPost.comments.map((comment: any, index: number) => (
              <AlbumChatCard key={index} comment={comment}></AlbumChatCard>
            ))}
          </ChatArea>
        </>
      )}
    </Container>
  );
}

export default AlbumPostPage;
