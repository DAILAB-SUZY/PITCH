import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
import useStore from '../store/store';
import { useNavigate } from 'react-router-dom';
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
  font-family: 'Rg';
  color: ${props => props.color};

  text-overflow: ellipsis;
  display: '-webkit-box';
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
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
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
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
  createAt: number; // ISO 날짜 형식
  updateAt: number; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: User;
}

interface AlbumData {
  comment: AlbumChatComment;
  spotifyAlbumId: string;
}

const AlbumChatCard = ({ comment, spotifyAlbumId }: AlbumData) => {
  ////// Post 시간 계산 //////
  const CreateTime = comment.createAt;
  const UpdatedTime = comment?.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  //console.log(`album id : ${albumId}`);
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

  // chat 좋아요 상태 확인

  // 좋아요 설정
  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);
  const { id, name } = useStore();
  useEffect(() => {
    if (comment) {
      setLikesCount(comment.likes.length);
      if (comment.likes.some((like: any) => like.id === id)) {
        setIsPostLiked(true);
      }
    }
  }, [comment]);

  const CommentLikeUrl = server + `/api/album/${spotifyAlbumId}/comment/${comment.albumChatCommentId}/commentLike`;

  console.log(`albumid: ${spotifyAlbumId}`);
  const changeCommentLike = async () => {
    console.log('changing Like');
    if (isPostLiked && comment) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsPostLiked(false);
      setLikesCount(likesCount - 1);
      comment.likes = comment.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsPostLiked(true);
      setLikesCount(likesCount + 1);
      comment?.likes.push({
        id: id,
        username: name,
        profilePicture: 'string',
        dnas: [],
      });
    }
    fetchLike();
    // like 데이터 POST 요청
  };

  const fetchLike = async () => {
    if (token) {
      console.log('fetching Like Data');
      try {
        const response = await fetch(CommentLikeUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const data = await response.json();
          if (data.likes.some((like: any) => like.id === id)) {
            console.log('like 추가');
          } else {
            console.log('like 삭제');
          }
        } else if (response.status === 401) {
          await ReissueToken();
          fetchLike();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('like 실패:', error);
      }
    }
  };
  const ReissueToken = async () => {
    console.log('reissuing Token');
    try {
      const response = await fetch(reissueTokenUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Refresh-Token': `${refreshToken}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('login-token', data.token);
        localStorage.setItem('login-refreshToken', data.refreshToken);
        setToken(data.token);
        setRefreshToken(data.refreshToken);
      } else {
        console.error('failed to reissue token', response.status);
      }
    } catch (error) {
      console.error('Refresh Token 재발급 실패', error);
    }
  };

  const navigate = useNavigate();
  const GoToAlbumChatPage = () => {
    navigate('/AlbumChatPage', { state: { comment, spotifyAlbumId } });
  };

  const GoToMusicProfilePage = (userId: number) => {
    navigate('/MusicProfilePage', { state: userId });
  };

  return (
    <ChatCardContainer>
      <ProfileArea
        onClick={() => {
          GoToMusicProfilePage(comment.author.id);
        }}
      >
        <ProfileImage>
          <img src={comment.author.profilePicture}></img>
        </ProfileImage>
        <ProfileTextArea>
          <ProfileName>{comment.author.username}</ProfileName>
          <PostUploadTime> {timeAgo} </PostUploadTime>
        </ProfileTextArea>
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
          onClick={() => changeCommentLike()}
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
