import styled from "styled-components";
import { colors } from "../../styles/color";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useStore from "../store/store";

const AlbumPostContainer = styled.div`
  width: 350px;
  margin: 20px 0px;
  height: auto;
  background-color: ${colors.BG_grey};
  border-radius: 10px;
  position: relative;
  z-index: 1;
  line-height: 18px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  /* transition: all ease-out 3s; */
`;

const AlbumTitleArea = styled.div`
  position: relative;
  width: 100%;
  height: 200px;
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
  width: 350px;
  height: 200px;
  /* object-fit: cover; */
  z-index: 1;
  border-radius: 10px 10px 0px 0px;
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
  border-radius: 10px 10px 0px 0px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
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

const Text = styled.div<{
  fontFamily?: string;
  fontSize?: string;
  margin?: string;
  color?: string;
}>`
  font-size: ${(props) => props.fontSize};
  font-family: ${(props) => props.fontFamily};
  color: ${(props) => props.color};
  height: auto;
  width: auto;
  max-width: 230px;
  margin: ${(props) => props.margin};
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-break: break-all;
  line-height: 110%;
`;

const PostArea = styled.div`
  display: flex;
  border-radius: 0 0 10px 10px;
  width: 350px;

  height: auto;
  /* overflow: hidden; */
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
  background-color: ${colors.BG_grey};
  transition: max-height linear 1s;
`;

const ProfileArea = styled.div`
  display: flex;
  width: auto;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;

const PostHeaderArea = styled.div`
  display: flex;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;

const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: "Rg";
  margin-left: 10px;
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
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background-color: ${colors.BG_grey};
`;

const PostContentArea = styled.div`
  display: flex;
  width: 320px;
  height: 36px;
  box-sizing: border-box;

  /* white-space: nowrap; */
  overflow: hidden;
  /* text-overflow: ellipsis; */

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: "Rg";
  padding: 0px 10px;
  margin: 10px 0;

  transition: height ease 0.7s;
`;

const ButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

interface AlbumPostProps {
  albumPost: {
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
  };
}

const AlbumPost = ({ albumPost }: AlbumPostProps) => {
  // const contentHeight = useRef<HTMLDivElement>(null);
  // const textHeight = useRef<HTMLDivElement>(null);
  const { email, setEmail, name, setName, id, setId } = useStore();
  const navigate = useNavigate();
  const GoToAlbumPostPage = () => {
    navigate("/AlbumPostPage", { state: { albumPost } });
  };

  const GoToMusicProfilePage = () => {
    navigate("/MusicProfilePage", { state: albumPost.postDetail.author.id });
  };

  // const GoToMusicProfilePage = () => {
  //   navigate("/MusicProfilePage", { state: { albumPost } });
  // };

  ////// Post 시간 계산 //////
  const CreateTime = albumPost.postDetail.createAt;
  const UpdatedTime = albumPost.postDetail.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>("");

  useEffect(() => {
    const updateTimeAgo = () => {
      if (UpdatedTime === null) {
        const time = formatTimeAgo(CreateTime);
        setTimeAgo(time);
      } else {
        const time = formatTimeAgo(UpdatedTime);
        setTimeAgo(time);
      }
    };

    // 처음 마운트될 때 시간 계산
    updateTimeAgo();
  }, [CreateTime, UpdatedTime]);

  const formatTimeAgo = (unixTimestamp: number): string => {
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
  const server = "http://203.255.81.70:8030";
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem("login-token"));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem("login-refreshToken"));
  const PostLikeUrl = server + "/api/album/post/" + (albumPost ? albumPost.postDetail.postId : "") + "/like";

  const changePostLike = async () => {
    console.log("changing Like");
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
  };

  const fetchLike = async () => {
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
          // setAlbumPost(data);
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

  return (
    <AlbumPostContainer>
      <AlbumTitleArea>
        <ImageArea>
          <img
            src={albumPost.postDetail.album.albumCover}
            width="350px"
            height="320px"
            object-fit="cover"
            // z-index="1"
          ></img>
        </ImageArea>
        <GradientBG> </GradientBG>
        <TitleTextArea>
          <Text fontFamily="Bd" fontSize="30px">
            {albumPost.postDetail.album.title}
          </Text>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 2px 10px">
            {albumPost.postDetail.album.artistName}
          </Text>
        </TitleTextArea>
      </AlbumTitleArea>
      <PostArea>
        <PostHeaderArea>
          <ProfileArea onClick={() => GoToMusicProfilePage()}>
            <ProfileImage>
              <img src={albumPost.postDetail.author.profilePicture} width="100%" height="100%"></img>
            </ProfileImage>
            <ProfileTextArea>
              <ProfileName>{albumPost.postDetail.author.username}</ProfileName>
            </ProfileTextArea>
          </ProfileArea>
          <PostUploadTime> {timeAgo}</PostUploadTime>
        </PostHeaderArea>
        <PostContentArea onClick={() => GoToAlbumPostPage()}>
          <p>{albumPost.postDetail.content}</p>
        </PostContentArea>
        {/* <PostContentArea ref={contentHeight}>
          <p ref={textHeight}>{albumPost.content}</p>
        </PostContentArea> */}

        <ButtonArea>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill={
              albumPost.likes.some((like) => like.id === id) ? colors.Button_active : colors.Button_deactive
            }
            className="bi bi-heart-fill"
            viewBox="0 0 16 16"
            onClick={() => changePostLike()}
          >
            <path
              fill-rule="evenodd"
              d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314"
            />
          </svg>
          <Text fontFamily="Rg" fontSize="14px" color="grey" margin="0px 20px 0px 5px">
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
          <Text fontFamily="Rg" fontSize="14px" color="grey" margin="0px 0px 0px 5px">
            답글 {albumPost.comments.length}개
          </Text>
        </ButtonArea>
      </PostArea>
    </AlbumPostContainer>
  );
};

export default AlbumPost;
