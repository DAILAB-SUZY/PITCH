import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import { fetchGET, fetchPOST, MAX_REISSUE_COUNT } from '../utils/fetchData';
import { updateTimeAgo } from '../utils/getTimeAgo';
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

const CommentEditArea = styled.div`
  display: flex;
  width: 100vw;
  height: 100%;
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

const CommentContentArea = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  box-sizing: border-box;

  /* white-space: nowrap; */
  /* text-overflow: ellipsis; */

  flex-direction: column;
  justify-content: space-between;
  font-size: 15px;
  font-family: 'Rg';
  padding: 0px 10px;
  margin: 10px 0px 20px 0px;

  transition: height ease 0.7s;
`;

const ContentInput = styled.textarea`
  width: 100%;
  min-height: 200px;
  height: auto;
  background-color: ${colors.BG_grey};
  font-size: 15px;
  border: 0;
  outline: none;
  color: ${colors.Font_black};
  resize: none; /* User can't manually resize */
  overflow-y: scroll; /* Prevent extra scroll bar */
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
  createAt: string; // ISO 날짜 형식
  updateAt: string; // ISO 날짜 형식
  likes: User[];
  comments: AlbumChatComment[]; // 재귀적 구조
  author: User;
}

function AlbumChatCommentPostPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const albumChatId = location.state.albumChatId;
  const spotifyAlbumId = location.state.spotifyAlbumId;
  const [AlbumChatData, setAlbumChatData] = useState<AlbumChatComment>();

  const GoToAlbumChatPage = () => {
    navigate('/AlbumChatPage', { state: { albumChatId, spotifyAlbumId } });
  };

  useEffect(() => {
    fetchAlbumChat(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
  }, []);
  const AlbumChatUrl = `/api/album/${spotifyAlbumId}/albumchat/${albumChatId}?sorted=recent`;
  const fetchAlbumChat = async (token: string, refreshToken: string) => {
    fetchGET(token, refreshToken, AlbumChatUrl, MAX_REISSUE_COUNT).then(data => {
      console.log(data);
      setAlbumChatData(data);
      setCreateTime(data.createAt);
      setUpdatedTime(data.updateAt);
    });
  };

  const [postContent, setPostContent] = useState('');

  // Post 시간 계산
  const [CreateTime, setCreateTime] = useState<string>('');
  const [UpdatedTime, setUpdatedTime] = useState<string>('');
  const [timeAgo, setTimeAgo] = useState<string>('');

  useEffect(() => {
    if (AlbumChatData) {
      const time = updateTimeAgo(CreateTime, UpdatedTime);
      setTimeAgo(time);
    }
  }, [CreateTime, UpdatedTime]);
  // chat 좋아요 상태 확인

  const textareaRef = useRef<HTMLTextAreaElement | null>(null);
  const adjustTextareaHeight = () => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto'; // Reset height to auto to calculate the new height
      textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`; // Adjust the height based on the content
    }
  };

  // Commet 작성
  const AlbumChatPostUrl = `/api/album/${spotifyAlbumId}/albumchat`;
  const fetchAlbumChatComment = async (token: string, refreshToken: string) => {
    const data = {
      content: postContent,
      sorted: 'recent',
      parentAlbumChatCommentId: albumChatId,
    };
    await fetchPOST(token, refreshToken, AlbumChatPostUrl, data, MAX_REISSUE_COUNT);
    GoToAlbumChatPage();
  };

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
              onClick={() => fetchAlbumChatComment(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '')}
            >
              저장
            </Text>
          </ButtonArea>
          <Line></Line>
          <CommentArea>
            <ProfileArea>
              <ProfileImage>
                <ProfileImageCircle src={AlbumChatData?.author.profilePicture} alt="Profile" />
              </ProfileImage>
              <ProfileTextArea>
                <ProfileName>{AlbumChatData?.author.username}</ProfileName>
                <PostUploadTime> {timeAgo}</PostUploadTime>
              </ProfileTextArea>
            </ProfileArea>
            <ChatCardBody>
              <Text fontSize="15px">{AlbumChatData?.content}</Text>
            </ChatCardBody>
            {/* <CommentButtonArea>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="14"
                height="14"
                fill={AlbumChatData?.likes.some((like: any) => like.id === id) ? colors.Button_active : colors.Button_deactive}
                className="bi bi-heart-fill"
                viewBox="0 0 16 16"
                onClick={() => changeCommentLike()}
              >
                <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
              </svg>
              <Text fontSize="14px" color="grey" margin="0px 20px 0px 5px">
                좋아요 {AlbumChatData?.likes.length}개
              </Text>
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="grey" className="bi bi-chat-right-text-fill" viewBox="0 0 16 16" style={{ strokeWidth: 6 }}>
                <path d="M2 1a1 1 0 0 0-1 1v8a1 1 0 0 0 1 1h9.586a2 2 0 0 1 1.414.586l2 2V2a1 1 0 0 0-1-1zm12-1a2 2 0 0 1 2 2v12.793a.5.5 0 0 1-.854.353l-2.853-2.853a1 1 0 0 0-.707-.293H2a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2z" />
                <path d="M3 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3 6a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9A.5.5 0 0 1 3 6m0 2.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5" />
              </svg>
              <Text fontSize="14px" color="grey" margin="0px 0px 0px 5px">
                답글 {AlbumChatData?.comments.length}개
              </Text>
            </CommentButtonArea> */}
          </CommentArea>
        </CommentArea>
        <Line></Line>
        <CommentEditArea>
          <CommentContentArea>
            {/* <form> */}
            <ContentInput
              ref={textareaRef}
              placeholder="댓글을 남겨주세요."
              value={postContent}
              onChange={e => setPostContent(e.target.value)}
              onInput={adjustTextareaHeight} // Adjust height when input changes
            ></ContentInput>
            {/* </form> */}
          </CommentContentArea>
          <ButtonArea></ButtonArea>
        </CommentEditArea>
      </Body>
    </Container>
  );
}

export default AlbumChatCommentPostPage;
