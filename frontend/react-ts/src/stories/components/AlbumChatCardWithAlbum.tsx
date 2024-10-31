import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useStore from '../store/store';
const ChatCardContainer = styled.div`
  width: 350px;
  height: 120px;
  padding: 10px;
  box-sizing: border-box;
  margin: 10px 0px;
  background-color: ${colors.BG_grey};
  border-radius: 12px;
  display: flex;
  flex-direction: row;
  align-items: center;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  line-height: 110%;
`;

const AlbumCoverArea = styled.div`
  width: 100px;
  height: 100px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  overflow: hidden;
  object-fit: cover;
`;

const CommentArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 240px;
  height: 90px;
  padding: 0px 10px 0px 10px;
  box-sizing: border-box;
`;

const ChatCardBody = styled.div`
  margin: 5px 0px 10px 0px;
  width: 90%;
  height: 43px;
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
`;

const ProfileArea = styled.div`
  display: flex;
  width: 100%;
  height: 25px;
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
`;

const ProfileTextArea = styled.div`
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
  height: 18px;
  width: 150px;
`;

const ProfileName = styled.div`
  display: flex;
  font-size: 20px;
  font-family: 'Rg';
  margin-left: 10px;
  color: ${colors.Font_black};
`;

const ProfileImageCircle = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover; /* 비율 유지하며 꽉 채움 */
  object-position: center; /* 이미지 가운데 정렬 */
`;

const PostUploadTime = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-start;
  height: 10px;
  font-size: 10px;
  font-family: 'Rg';
  margin-left: 10px;
  color: ${colors.Font_grey};
`;
const ProfileImage = styled.div`
  display: flex;
  overflow: hidden;
  width: 30px;
  height: 30px;
  border-radius: 50%;
`;

const ButtonArea = styled.div`
  width: 100%;
  height: 17px;
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
interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
}
interface commentProps {
  comment: {
    albumDetail: AlbumDetail;
    albumChatCommentDetail: AlbumChatComment;
  };
}

const AlbumChatCardWithAlbum = ({ comment }: commentProps) => {
  ////// Post 시간 계산 //////
  const CreateTime = comment?.albumChatCommentDetail.createAt;
  const UpdatedTime = comment?.albumChatCommentDetail.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');

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
  const navigate = useNavigate();
  const GoToAlbumPage = (spotifyAlbumId: string) => {
    navigate('/AlbumPage', { state: spotifyAlbumId });
  };
  // 좋아요 설정
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);
  const { id, name } = useStore();
  useEffect(() => {
    if (comment) {
      setLikesCount(comment.albumChatCommentDetail.likes.length);
      if (comment.albumChatCommentDetail.likes.some((like: any) => like.id === id)) {
        setIsPostLiked(true);
      }
    }
  }, [comment]);

  const CommentLikeUrl = server + `/api/album/${comment.albumDetail.spotifyId}/comment/${comment.albumChatCommentDetail.albumChatCommentId}/commentLike`;

  console.log(`albumid: ${comment.albumDetail.spotifyId}`);
  const changeCommentLike = async () => {
    console.log('changing Like');
    if (isPostLiked && comment) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsPostLiked(false);
      setLikesCount(likesCount - 1);
      comment.albumChatCommentDetail.likes = comment.albumChatCommentDetail.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsPostLiked(true);
      setLikesCount(likesCount + 1);
      comment?.albumChatCommentDetail.likes.push({
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

  const GoToAlbumChatPage = (comment: AlbumChatComment, spotifyAlbumId: string) => {
    navigate('/AlbumChatPage', { state: { comment, spotifyAlbumId } });
  };

  return (
    <ChatCardContainer>
      <AlbumCoverArea onClick={() => GoToAlbumPage(comment.albumDetail.spotifyId)}>
        <img src={comment.albumDetail.albumCover} width="100%" object-fit="cover"></img>
      </AlbumCoverArea>
      <CommentArea>
        <ProfileArea>
          <ProfileImage>
            <ProfileImageCircle src={comment.albumChatCommentDetail.author.profilePicture} alt="Profile" />
          </ProfileImage>
          <ProfileTextArea>
            <ProfileName>{comment.albumChatCommentDetail.author.username}</ProfileName>
            <PostUploadTime> {timeAgo} </PostUploadTime>
          </ProfileTextArea>
        </ProfileArea>
        <ChatCardBody
          onClick={() => {
            GoToAlbumChatPage(comment.albumChatCommentDetail, comment.albumDetail.spotifyId);
          }}
        >
          <Text fontSize="15px">{comment.albumChatCommentDetail.content}</Text>
        </ChatCardBody>
        <ButtonArea>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="14"
            height="14"
            fill={comment.albumChatCommentDetail.likes.some((like: any) => like.id === comment.albumChatCommentDetail.author.id) ? colors.Button_active : colors.Button_deactive}
            className="bi bi-heart-fill"
            viewBox="0 0 16 16"
            onClick={() => changeCommentLike()}
          >
            <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
          </svg>
          <Text fontSize="14px" color="grey" margin="0px 20px 0px 5px">
            좋아요 {comment.albumChatCommentDetail.likes.length}개
          </Text>
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
            <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
            <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
          </svg>
          <Text fontSize="14px" color="grey" margin="0px 0px 0px 5px">
            답글 {comment.albumChatCommentDetail.comments.length}개
          </Text>
        </ButtonArea>
      </CommentArea>
    </ChatCardContainer>
  );
};

export default AlbumChatCardWithAlbum;
