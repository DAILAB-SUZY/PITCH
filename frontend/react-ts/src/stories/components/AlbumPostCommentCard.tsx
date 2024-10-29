import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
const ChatCardContainer = styled.div`
  width: 350px;
  height: auto;
  padding: 10px;
  margin-bottom: 20px;
  background-color: ${colors.BG_grey};
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
`;

const ChatCardBody = styled.div`
  margin: 10px 0px 10px 0px;
  width: 90%;
  height: auto;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Text = styled.div<{ fontSize?: string; margin?: string; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'Rg';
  color: ${props => props.color};
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  height: 30px;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;
const PostUploadTime = styled.div`
  display: flex;
  font-size: 10px;
  font-family: 'Rg';
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
  font-family: 'Rg';
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

const ButtonArea = styled.div`
  width: 100%;
  height: 15px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

interface commentProps {
  comment: {
    id: number;
    content: string;
    createAt: number;
    updateAt: number;
    likes: [
      {
        id: number;
        username: string;
        profilePicture: string;
      },
    ];
    childComments: [
      {
        id: number;
        content: string;
        author: {
          id: number;
          username: string;
          profilePicture: string;
        };
      },
    ];
    author: {
      id: number;
      username: string;
      profilePicture: string;
    };
  };
}

const AlbumPostCommentCard = ({ comment }: commentProps) => {
  ////// Post 시간 계산 //////
  const CreateTime = comment?.createAt;
  const UpdatedTime = comment?.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  useEffect(() => {
    const updateTimeAgo = () => {
      if (CreateTime) {
        if (UpdatedTime === null) {
          const time = formatTimeAgo(CreateTime);
          console.log('수정안됨');
          setTimeAgo(time);
        } else {
          const time = formatTimeAgo(UpdatedTime);
          console.log('수정됨');
          setTimeAgo(time);
        }
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

  // 수정/삭제 버튼
  const editMenu = () => {
    console.log('edit');
    setIsDropdownOpen(!isDropdownOpen);
  };

  // 삭제요청
  // const deleteComment = async () => {
  //   const CommenrDeleteUrl = `/api/album/post/${}/comment/${comment.id}`;
  //   if (token) {
  //     if (albumPost) {
  //       console.log(`delete id: ${albumPost.postDetail.postId}Post...`);
  //     }
  //     try {
  //       const response = await fetch(PostDeleteUrl, {
  //         method: "DELETE",
  //         headers: {
  //           "Content-Type": "application/json",
  //           Authorization: `Bearer ${token}`,
  //         },
  //       });
  //       if (response.ok) {
  //         const data = await response.json();
  //         setAlbumPost(data);
  //         console.log("deleted");
  //         GoToHomePage();
  //       } else if (response.status === 401) {
  //         console.log("reissuing Token");
  //         const reissueToken = await fetch(reissueTokenUrl, {
  //           method: "POST",
  //           headers: {
  //             "Content-Type": "application/json",
  //             "Refresh-Token": `${refreshToken}`,
  //           },
  //         });
  //         const data = await reissueToken.json();
  //         localStorage.setItem("login-token", data.token);
  //         setToken(data.token);
  //         localStorage.setItem("login-refreshToken", data.refreshToken);
  //         setRefreshToken(data.refreshToken);
  //         deletePost();
  //       } else {
  //         console.error("Failed to delete data:", response.status);
  //       }
  //     } catch (error) {
  //       console.error("delete 실패:", error);
  //     }
  //   }
  // };

  return (
    <ChatCardContainer>
      <ProfileArea>
        <ProfileImage>
          <img src={comment.author.profilePicture}></img>
        </ProfileImage>
        <ProfileTextArea>
          <ProfileName>{comment.author.username}</ProfileName>
          <PostUploadTime> {timeAgo} </PostUploadTime>
        </ProfileTextArea>
      </ProfileArea>
      <ChatCardBody>
        <Text fontSize="15px">{comment.content}</Text>
      </ChatCardBody>
      <ButtonArea>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="14"
          height="14"
          fill={comment.likes.some((like: any) => like.id === comment.author.id) ? colors.Button_active : colors.Button_deactive}
          className="bi bi-heart-fill"
          viewBox="0 0 16 16"
        >
          <path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 20px 0px 5px">
          좋아요 {comment.likes.length}개
        </Text>
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
          <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
          <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 0px 0px 5px">
          답글 {comment.childComments.length}개
        </Text>
      </ButtonArea>
    </ChatCardContainer>
  );
};

export default AlbumPostCommentCard;
