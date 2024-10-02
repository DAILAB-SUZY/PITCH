import styled from "styled-components";
import { colors } from "../../styles/color";
import { useRef, useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import AlbumChatCard from "../components/AlbumChatCard";
import useStore from "../store/store";

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
  justify-content: flex-start;
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
  background-color: ${colors.Main_Pink};
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

function AlbumPostPage() {
  const location = useLocation();
  //const [albumPost, setAlbumPost] = useState();
  const albumPost = location.state;

  // useEffect(() => {
  //   setAlbumPost(location.state);
  //   const CreateTime = albumPost.createAt;
  //   const UpdatedTime = albumPost.updateAt;
  // }, []);

  const CreateTime = albumPost.createAt;
  const UpdatedTime = albumPost.updateAt;

  ////// Post 시간 계산 //////

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
  const { email, setEmail, name, setName, id, setId } = useStore();

  // // Post 좋아요 상태 확인
  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState(albumPost.likes.length);

  useEffect(() => {
    if (albumPost.likes.some((like: any) => like.id === id)) {
      setIsPostLiked(true);
    }
  }, []);

  // 좋아요 상태 변경 함수
  const server = "http://203.255.81.70:8030";
  const PostLikeUrl = server + "//api/album/post/" + albumPost.postId + "/like";
  const changePostLike = async () => {
    if (isPostLiked) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsPostLiked(false);
      setLikesCount(likesCount - 1);
      albumPost.likes = albumPost.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsPostLiked(true);
      setLikesCount(likesCount + 1);
      albumPost.likes.push(id);
    }
    // like 데이터 POST 요청
    console.log("fetching Data");
    try {
      const response = await fetch(PostLikeUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });
      const data = await response.json();
      console.log("like 성공");
    } catch (error) {
      console.error("like 실패:", error);
    }
  };

  return (
    <Container>
      <AlbumPostArea>
        {/*  TODO: 스크롤 되다가 일부 남기고 멈추기 */}
        <AlbumTitleArea>
          <ImageArea>
            <img
              src={albumPost.album.albumCover}
              width="100%"
              object-fit="cover"
              // z-index="1"
            ></img>
          </ImageArea>
          <GradientBG> </GradientBG>
          <TitleTextArea>
            <Text fontFamily="Bd" fontSize="40px" margin="0px" color="black">
              {albumPost.album.title}
            </Text>
            <Text fontFamily="Rg" fontSize="20px" margin="0px 0px 2px 10px" color="black">
              {albumPost.album.artistName}
            </Text>
          </TitleTextArea>
        </AlbumTitleArea>
        <PostArea>
          <ProfileArea>
            <ProfileImage>
              <img src={albumPost.author.profilePicture}></img>
            </ProfileImage>
            <ProfileTextArea>
              <ProfileName>{albumPost.author.username}</ProfileName>
              <PostUploadTime> {timeAgo} </PostUploadTime>
            </ProfileTextArea>
          </ProfileArea>
          <PostContentArea>{albumPost.content}</PostContentArea>
          <ButtonArea>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="14"
              height="14"
              fill={isPostLiked ? colors.Button_active : colors.Button_deactive}
              className="bi bi-heart-fill"
              viewBox="0 0 16 16"
              // onClick={() => changePostLike()}
            >
              <path
                fill-rule="evenodd"
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
      {/*  TODO: postArea와 ContentArea사이 공간 생기는거 없애기 */}
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
              fill-rule="evenodd"
              d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
            />
          </svg>
        </RowAlignArea>
        {albumPost.comments.map((comment: any) => (
          <AlbumChatCard comment={comment}></AlbumChatCard>
        ))}
      </ChatArea>
    </Container>
  );
}

export default AlbumPostPage;
