import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useRef, useState } from 'react';
import { fetchPOST, fetchDELETE } from '../utils/fetchData';
import { updateTimeAgo } from '../utils/getTimeAgo';
import useStore from '../store/store';
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
  justify-content: space-between;
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
`;
const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const ProfileInfoArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
`;

const EditBtn = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
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
    createdAt: string;
    updatedAt: string;
    likes: {
      id: number;
      username: string;
      profilePicture: string;
    }[];
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
  postId: number;
}

const AlbumPostCommentCard = ({ comment, postId }: commentProps) => {
  // Post 시간 계산
  const CreateTime = comment.createdAt;
  const UpdatedTime = comment.updatedAt;
  const [timeAgo, setTimeAgo] = useState<string>('');
  useEffect(() => {
    if (comment) {
      const time = updateTimeAgo(CreateTime, UpdatedTime);
      setTimeAgo(time);
    }
  }, [CreateTime, UpdatedTime]);
  console.log('comment info');
  console.log(comment);
  console.log(postId);
  /////////////

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
  const { name, id } = useStore();

  //Post Comment 좋아요 상태 확인
  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);

  useEffect(() => {
    if (comment) {
      setLikesCount(comment.likes.length);
      if (comment.likes.some((like: any) => like.id === id)) {
        setIsPostLiked(true);
      }
    }
  }, [comment]);

  const changeCommentLike = async () => {
    if (isPostLiked && comment) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsPostLiked(false);
      setLikesCount(likesCount - 1);
      comment.likes = comment.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsPostLiked(true);
      setLikesCount(likesCount + 1);
      comment.likes.push({
        id: id,
        username: name,
        profilePicture: 'string',
      });
    }
    fetchLike(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  };

  const CommentLikeUrl = `/api/album/post/${postId}/comment/${comment.id}/like`;
  const fetchLike = async (token: string, refresuToken: string) => {
    fetchPOST(token, refresuToken, CommentLikeUrl, {});
  };

  // 수정/삭제 버튼
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const editMenu = () => {
    console.log('edit');
    setIsDropdownOpen(!isDropdownOpen);
  };

  // 삭제요청
  const DeleteChatUrl = `/api/album/post/${postId}/comment/${comment.id}`;
  const deleteChat = async (token: string, refreshToken: string) => {
    fetchDELETE(token, refreshToken, DeleteChatUrl);
  };

  return (
    <ChatCardContainer>
      <ProfileArea>
        <ProfileInfoArea>
          <ProfileImage>
            <ProfileImageCircle src={comment.author.profilePicture} alt="Profile" />
          </ProfileImage>
          <ProfileTextArea>
            <ProfileName>{comment.author.username}</ProfileName>
            <PostUploadTime> {timeAgo} </PostUploadTime>
          </ProfileTextArea>
        </ProfileInfoArea>
        {comment.author.id === id && (
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
                <DropdownItem onClick={() => deleteChat(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '')}>삭제</DropdownItem>
              </DropdownMenu>
            )}
          </EditBtn>
        )}
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
          onClick={() => changeCommentLike()}
        >
          <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 20px 0px 5px">
          좋아요 {comment.likes.length}개
        </Text>
        {/* <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
          <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
          <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 0px 0px 5px">
          답글 {comment.childComments.length}개
        </Text> */}
      </ButtonArea>
    </ChatCardContainer>
  );
};

export default AlbumPostCommentCard;
