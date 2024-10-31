import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useLocation, useNavigate } from 'react-router-dom';
import { useRef, useEffect, useState } from 'react';

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
  position: sticky; /* 스크롤 시 고정 */
  top: 0; /* 화면 상단에 고정 */
  position: relative;
  width: 100vw;
  height: 390px;
  /* padding: 0px 0px 20px 10px; */
  /* z-index: 3; */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  color: white;
  box-sizing: border-box;
  overflow: hidden;
  z-index: 100;
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
  overflow: hidden;
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
  background: linear-gradient(0deg, #000 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const UnselectedGradientBG = styled.div`
  position: absolute;
  overflow: hidden;
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
  background: linear-gradient(0deg, ${colors.Main_Pink} 0%, rgba(0, 0, 0, 0) 100%);
  backdrop-filter: blur(0px);
`;

const TitleTextArea = styled.div`
  position: absolute;
  bottom: 10px;
  left: 10px;
  /* position: sticky;
  top: 10px; */
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

const PostArea = styled.div`
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

const PostContentArea = styled.div`
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

interface AlbumPost {
  albumArtist: {
    artistId: string;
    imageUrl: string;
    name: string;
  };
  albumId: string;
  imageUrl: string;
  name: string;
  total_tracks: number;
  release_date: string;
  postId: number;
}

function AlbumPostEditPage() {
  const [albumPost, setAlbumPost] = useState<AlbumPost | null>(null);
  //   const [postContent, setPostContent] = useState("내용을 입력해주세요");
  //   const { email, setEmail, name, setName, id, setId } = useStore();
  const location = useLocation();
  const [postContent, setPostContent] = useState('');
  const [isEditMode, setIsEditMode] = useState(false);
  const server = 'http://203.255.81.70:8030';
  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  useEffect(() => {
    console.log(location.state);
    // post 작성을 위해 처음 들어왔으면
    if (!location.state) {
      setAlbumPost(null);
    } else {
      if (location.state.postContent === undefined) {
        setAlbumPost(location.state);
      } else {
        setIsEditMode(true);
        setPostContent(location.state.postContent);
        delete location.state.postContent;
        setAlbumPost(location.state);
      }
    }
  }, [location.state]);

  const navigate = useNavigate();
  const GoToSearchPage = () => {
    navigate('/SearchPage');
  };
  const GoToHomePage = () => {
    navigate('/Home');
  };

  // 게시물 작성
  const fetchPost = async () => {
    let PostUrl = `${server}/api/album/post`;
    if (token && albumPost) {
      try {
        console.log(`Posting...`);
        console.log(albumPost);
        console.log(postContent);
        const response = await fetch(PostUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            content: postContent,
            title: albumPost.name,
            spotifyAlbumId: albumPost.albumId,
            score: 0,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log('Post Success');
          console.log(data);
          GoToHomePage();
        } else if (response.status === 401) {
          ReissueToken();
          fetchPost();
        } else {
          console.error('Failed to Post data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
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

  // 게시물 수정
  const fetchEdit = async () => {
    const token = localStorage.getItem('login-token');
    const refreshToken = localStorage.getItem('login-refreshToken');
    let EditUrl = `${server}/api/album/post/${albumPost?.postId}`;
    if (token && albumPost) {
      try {
        console.log(`Edit Posting...`);
        console.log(albumPost);
        console.log(postContent);
        const response = await fetch(EditUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            content: postContent,
            score: 0,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log('Post Success');
          console.log(data);
          GoToHomePage();
        } else if (response.status === 401) {
          console.log('reissuing Token');
          const reissueToken = await fetch(reissueTokenUrl, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Refresh-Token': `${refreshToken}`,
            },
          });
          const data = await reissueToken.json();
          localStorage.setItem('login-token', data.token);
          localStorage.setItem('login-refreshToken', data.refreshToken);
          fetchPost();
        } else {
          console.error('Failed to Post data:', response.status);
        }
      } catch (error) {
        console.error('Error fetching the JSON file:', error);
      } finally {
        console.log('finished');
      }
    }
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
      <AlbumPostArea>
        {/*  TODO: 스크롤 되다가 일부 남기고 멈추기 */}
        {albumPost && (
          <AlbumTitleArea
            onClick={() => {
              GoToSearchPage();
            }}
          >
            <ImageArea>
              <img
                src={albumPost.imageUrl}
                width="100%"
                object-fit="cover"
                // z-index="1"
              ></img>
            </ImageArea>
            <GradientBG> </GradientBG>
            <TitleTextArea>
              <Text fontFamily="Bd" fontSize="30px" margin="0px" color={colors.BG_white}>
                {albumPost.name}
              </Text>
              <Text fontFamily="Rg" fontSize="20px" margin="0px 0px 2px 10px" color={colors.BG_white}>
                {albumPost.albumArtist.name}
              </Text>
            </TitleTextArea>
          </AlbumTitleArea>
        )}
        {!albumPost && (
          <AlbumTitleArea
            onClick={() => {
              GoToSearchPage();
            }}
          >
            <UnselectedGradientBG></UnselectedGradientBG>
            <TitleTextArea>
              <Text fontFamily="Bd" fontSize="30px" margin="0px" color="white">
                앨범을 선택해주세요
              </Text>
            </TitleTextArea>
          </AlbumTitleArea>
        )}
        <ButtonArea>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 0px 0px 10px" color={colors.Font_black} onClick={() => GoToHomePage()}>
            취소
          </Text>
          <Text fontFamily="Bd" fontSize="20px" margin="0px" color={colors.Font_black}>
            Album Post
          </Text>
          <Text fontFamily="Rg" fontSize="15px" margin="0px 10px 0px 0px" color={colors.Font_black} onClick={isEditMode ? fetchEdit : fetchPost}>
            저장
          </Text>
        </ButtonArea>
        <Line></Line>
        <PostArea>
          <PostContentArea>
            {/* <form> */}
            <ContentInput
              ref={textareaRef}
              placeholder="앨범에 대한 자유로운 감상평을 남겨주세요."
              value={postContent}
              onChange={e => setPostContent(e.target.value)}
              onInput={adjustTextareaHeight} // Adjust height when input changes
            ></ContentInput>
            {/* </form> */}
          </PostContentArea>
          <ButtonArea></ButtonArea>
        </PostArea>
      </AlbumPostArea>
    </Container>
  );
}

export default AlbumPostEditPage;
