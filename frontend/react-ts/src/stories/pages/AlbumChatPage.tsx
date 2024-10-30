import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useLocation, useNavigate } from 'react-router-dom';
import useStore from '../store/store';
import AlbumChatCommentCard from '../components/AlbumChatCommentCard';
import { useEffect, useState } from 'react';
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh;
  width: 100vw;
  background-color: ${colors.BG_grey};
  color: black;
`;

const Body = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const ButtonArea = styled.div`
  width: 100vw;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin: 0 10 0 10px;
  z-index: 10;
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

const ChatCardBody = styled.div`
  margin: 10px 0px 10px 0px;
  width: 90%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  line-height: 120%;
`;

const CommentButtonArea = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10 0 10px;
`;

const CommentArea = styled.div`
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
  z-index: 10;
`;

const CommentCommentArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: auto;
`;

const Line = styled.div`
  width: 95vw;
  height: 1px;
  background-color: ${colors.Button_deactive};
`;
const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string; color?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
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

function AlbumChatPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [AlbumChatData, setAlbumChatData] = useState<AlbumData>();
  const GoToAlbumChatCommentPostPage = () => {
    navigate('/AlbumChatCommentPostPage', { state: AlbumChatData });
  };

  ////// Post 시간 계산 //////
  const CreateTime = AlbumChatData?.comment.createAt;
  const UpdatedTime = AlbumChatData?.comment.updateAt;
  const [timeAgo, setTimeAgo] = useState<string>('');
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  console.log(timeAgo);
  useEffect(() => {
    const updateTimeAgo = () => {
      if (CreateTime && UpdatedTime) {
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
  }, [CreateTime, UpdatedTime, AlbumChatData]);

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

  // chat 좋아요 상태 확인

  // 좋아요 설정
  const [isPostLiked, setIsPostLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);
  const { id, name } = useStore();
  useEffect(() => {
    if (AlbumChatData?.comment) {
      setLikesCount(AlbumChatData?.comment.likes.length);
      if (AlbumChatData?.comment.likes.some((like: any) => like.id === id)) {
        setIsPostLiked(true);
      }
    }
  }, [AlbumChatData?.comment]);

  const CommentLikeUrl = server + `/api/album/${AlbumChatData?.spotifyAlbumId}/comment/${AlbumChatData?.comment.albumChatCommentId}/commentLike`;

  const changeCommentLike = async () => {
    console.log('changing Like');
    if (isPostLiked && AlbumChatData?.comment) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsPostLiked(false);
      setLikesCount(likesCount - 1);
      AlbumChatData.comment.likes = AlbumChatData.comment.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsPostLiked(true);
      setLikesCount(likesCount + 1);
      AlbumChatData?.comment.likes.push({
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

  useEffect(() => {
    const data = location.state;
    setAlbumChatData(data);
    console.log(data);
  }, []);

  return (
    <Container>
      <Body>
        <CommentArea>
          <ButtonArea>
            <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.BG_grey}>
              뒤로가기
            </Text>
            <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
              Comment
            </Text>
            <Text
              fontFamily="Rg"
              fontSize="15px"
              margin="0px 10px 0px 0px"
              color={colors.Font_black}
              // onClick={() => (isEditMode ? console.log("fetchEdit") : fetchComment())}
              onClick={() => GoToAlbumChatCommentPostPage()}
            >
              답글작성
            </Text>
          </ButtonArea>
          <Line></Line>
          <CommentArea>
            <ProfileArea>
              <ProfileImage>
                <ProfileImageCircle src={AlbumChatData?.comment.author.profilePicture} alt="Profile" />
              </ProfileImage>
              <ProfileTextArea>
                <ProfileName>{AlbumChatData?.comment.author.username}</ProfileName>
                <PostUploadTime> {timeAgo}</PostUploadTime>
              </ProfileTextArea>
            </ProfileArea>
            <ChatCardBody>
              <Text fontSize="15px">{AlbumChatData?.comment.content}</Text>
            </ChatCardBody>
            <CommentButtonArea>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="14"
                height="14"
                fill={AlbumChatData?.comment.likes.some((like: any) => like.id === id) ? colors.Button_active : colors.Button_deactive}
                className="bi bi-heart-fill"
                viewBox="0 0 16 16"
                onClick={() => changeCommentLike()}
              >
                <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
              </svg>
              <Text fontSize="14px" color="grey" margin="0px 20px 0px 5px">
                좋아요 {AlbumChatData?.comment.likes.length}개
              </Text>
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
                <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
                <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
              </svg>
              <Text fontSize="14px" color="grey" margin="0px 0px 0px 5px">
                답글 {AlbumChatData?.comment.comments.length}개
              </Text>
            </CommentButtonArea>
          </CommentArea>
        </CommentArea>
        <Line></Line>
        <CommentCommentArea>
          {AlbumChatData?.comment.comments.map(comment => <AlbumChatCommentCard comment={comment} spotifyAlbumId={AlbumChatData.spotifyAlbumId}></AlbumChatCommentCard>)}
        </CommentCommentArea>
      </Body>
    </Container>
  );
}

export default AlbumChatPage;
