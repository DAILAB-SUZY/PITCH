import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useRef, useState } from 'react';
import useStore from '../store/store';
import { useNavigate } from 'react-router-dom';
import { fetchPOST, fetchDELETE, MAX_REISSUE_COUNT } from '../utils/fetchData';
import { updateTimeAgo } from '../utils/getTimeAgo';
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
  min-height: 30px;
  max-height: 75px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  line-height: 120%;

  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
`;

const Text = styled.div<{ fontSize?: string; margin?: string; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: 'RG';
  color: ${props => props.color};

  text-overflow: ellipsis;
  display: '-webkit-box';
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
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
  font-family: 'RG';
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
  font-family: 'SB';
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
  object-fit: cover;
  object-position: center; /* 이미지 가운데 정렬 */
`;

const ProfileInfoArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
`;

const ButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
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
  height: 40px;
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

interface DNA {
  dnaKey: number;
  dnaName: string;
}
interface User {
  id: number;
  username: string;
  profilePicture: string;
  dnas: DNA[];
}

interface AlbumChatComment {
  albumChatCommentId: number;
  content: string;
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: User;
}

interface AlbumData {
  comment: AlbumChatComment;
  spotifyAlbumId: string;
  setAlbumChatList: React.Dispatch<React.SetStateAction<AlbumChatComment[]>>;
  setPageNumber: React.Dispatch<React.SetStateAction<number>>;
  setIsEnd: React.Dispatch<React.SetStateAction<boolean>>;
}

const AlbumChatCard = ({ comment, spotifyAlbumId, setAlbumChatList, setPageNumber, setIsEnd }: AlbumData) => {
  // Post 시간 계산
  const CreateTime = comment.createAt;
  const UpdatedTime = comment.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');

  useEffect(() => {
    if (comment) {
      const time = updateTimeAgo(CreateTime, UpdatedTime);
      setTimeAgo(time);
    }
  }, [CreateTime, UpdatedTime]);

  // chat 좋아요 상태 확인

  // 좋아요 설정
  const [isChatLiked, setIsChatLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);
  const { id, name } = useStore();
  useEffect(() => {
    if (comment) {
      setLikesCount(comment.likes.length);
      if (comment.likes.some((like: any) => like.id === id)) {
        setIsChatLiked(true);
      }
    }
  }, [comment]);

  const ChatLikeUrl = `/api/album/${spotifyAlbumId}/comment/${comment.albumChatCommentId}/commentLike`;
  const changeChatLike = async () => {
    if (isChatLiked && comment) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsChatLiked(false);
      setLikesCount(likesCount - 1);
      comment.likes = comment.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsChatLiked(true);
      setLikesCount(likesCount + 1);
      comment?.likes.push({
        id: id,
        username: name,
        profilePicture: 'string',
        dnas: [],
      });
    }
    fetchLike(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  };

  const fetchLike = async (token: string, refresuToken: string) => {
    fetchPOST(token, refresuToken, ChatLikeUrl, {}, MAX_REISSUE_COUNT);
  };

  const navigate = useNavigate();
  const GoToAlbumChatPage = () => {
    const albumChatId = comment.albumChatCommentId;
    navigate('/AlbumChatPage', { state: { albumChatId, spotifyAlbumId } });
  };

  const GoToMusicProfilePage = (userId: number) => {
    navigate('/MusicProfilePage', { state: userId });
  };

  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  // 수정/삭제 버튼
  const editMenu = () => {
    console.log('edit');
    setIsDropdownOpen(!isDropdownOpen);
  };

  // 삭제요청
  const DeleteChatUrl = `/api/album/${spotifyAlbumId}/albumchat/${comment.albumChatCommentId}?sorted=recent`;
  const deleteChat = async (token: string, refreshToken: string) => {
    await fetchDELETE(token, refreshToken, DeleteChatUrl, MAX_REISSUE_COUNT);
    setAlbumChatList([]);
    setPageNumber(0);
    setIsEnd(false);
  };

  return (
    <ChatCardContainer>
      <ProfileArea>
        <ProfileInfoArea>
          <ProfileImage
            onClick={() => {
              GoToMusicProfilePage(comment.author.id);
            }}
          >
            <ProfileImageCircle src={comment.author.profilePicture} alt="Profile" />
          </ProfileImage>
          <ProfileTextArea>
            <ProfileName
              onClick={() => {
                GoToMusicProfilePage(comment.author.id);
              }}
            >
              {comment.author.username}
            </ProfileName>
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
      <ChatCardBody
        onClick={() => {
          GoToAlbumChatPage();
        }}
      >
        <Text fontSize="15px">{comment.content}</Text>
      </ChatCardBody>
      <ButtonArea>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="14"
          height="14"
          fill={comment.likes.some((like: any) => like.id === id) ? colors.Button_active : colors.Button_deactive}
          className="bi bi-heart-fill"
          viewBox="0 0 16 16"
          onClick={() => changeChatLike()}
        >
          <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 20px 0px 5px">
          좋아요 {comment.likes.length}개
        </Text>
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
          <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
          <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
        </svg>
        <Text fontSize="14px" color="grey" margin="0px 0px 0px 5px">
          답글 {comment.comments.length}개
        </Text>
      </ButtonArea>
    </ChatCardContainer>
  );
};

export default AlbumChatCard;
