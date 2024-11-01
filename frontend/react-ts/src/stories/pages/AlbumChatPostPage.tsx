import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useLocation, useNavigate } from 'react-router-dom';
import { useRef, useState } from 'react';
import { fetchPOST } from '../utils/fetchData';

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

const CommentArea = styled.div`
  width: 100vw;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  background-color: white;
`;

const Text = styled.div<{
  fontFamily: string;
  fontSize: string;
  color: string;
  margin: string;
}>`
  font-size: ${props => props.fontSize};
  font-family: ${props => props.fontFamily};
  color: ${props => props.color};
  margin: ${props => props.margin};
  max-width: 280px;

  // 두 줄 이상일 때 '...' 처리
  display: -webkit-box;
  -webkit-line-clamp: 2; // 2줄까지만 표시
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
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
const Line = styled.div`
  width: 95vw;
  height: 1px;
  background-color: ${colors.Button_deactive};
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

function CommentPostPage() {
  const location = useLocation();
  const spotifyAlbumId = location.state;

  const [postContent, setPostContent] = useState('');

  const navigate = useNavigate();
  const GoToAlbumPage = () => {
    navigate('/AlbumPage', { state: spotifyAlbumId });
  };

  // 게시물 작성
  const fetchAlbumChat = async () => {
    const token = localStorage.getItem('login-token') as string;
    const refreshToken = localStorage.getItem('login-refreshToken') as string;
    const data = {
      content: postContent,
      sorted: 'recent',
    };
    fetchPOST(token, refreshToken, `/api/album/${spotifyAlbumId}/albumchat`, data).then(() => {
      GoToAlbumPage();
    });
  };

  const textareaRef = useRef<HTMLTextAreaElement | null>(null);
  const adjustTextareaHeight = () => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto'; // Reset height to auto to calculate the new height
      textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`; // Adjust the height based on the content
    }
  };

  return (
    <Container>
      <CommentArea>
        <ButtonArea>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black} onClick={() => GoToAlbumPage()}>
            취소
          </Text>
          <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
            Chat 작성
          </Text>
          <Text
            fontFamily="Rg"
            fontSize="15px"
            margin="0px 10px 0px 0px"
            color={colors.Font_black}
            // onClick={() => (isEditMode ? console.log("fetchEdit") : fetchComment())}
            onClick={() => fetchAlbumChat()}
          >
            저장
          </Text>
        </ButtonArea>
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
      </CommentArea>
    </Container>
  );
}

export default CommentPostPage;
