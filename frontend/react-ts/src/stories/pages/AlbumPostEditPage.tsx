import styled from 'styled-components';
import { colors } from '../../styles/color';
import { useLocation, useNavigate } from 'react-router-dom';
import { useRef, useEffect, useState } from 'react';
import { fetchPOST, MAX_REISSUE_COUNT } from '../utils/fetchData';
import ScoreEdit from '../components/ScoreEdit';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh; //auto;
  width: 100vw;
  background-color: ${colors.BG_grey};
  color: ${colors.Font_black};
`;

const AlbumPostArea = styled.div`
  width: 100vw;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  background-color: ${colors.BG_grey};
`;

const AlbumTitleArea = styled.div`
  //position: sticky; /* 스크롤 시 고정 */
  /* top: 0; 화면 상단에 고정 */
  position: relative;
  width: 100vw;
  height: 80vw;
  //height: 390px;
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
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-end;
  padding: 0px 0px 10px 20px;
  box-sizing: border-box;
  z-index: 3;
`;

const AlbumInfoArea = styled.div`
  position: absolute;
  bottom: 0px;
  left: 0px;
  width: 100%;
  height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: flex-start;
  padding: 0px 0px 10px 10px;
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
  font-family: 'RG';
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
  min-height: 700px;
  height: auto;
  background-color: ${colors.BG_grey};
  font-size: 16px;
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
  score: number;
}

function AlbumPostEditPage() {
  const [albumPost, setAlbumPost] = useState<AlbumPost | null>(null);
  const location = useLocation();
  const [postContent, setPostContent] = useState('');
  const [isEditMode, setIsEditMode] = useState(false);

  // 별점
  const [stars, setStars] = useState<string[]>();
  const [score, setScore] = useState<number>(0);

  useEffect(() => {
    console.log(location.state);
    // post 작성을 위해 처음 들어오는 경우
    if (!location.state) {
      setAlbumPost(null);
    } else {
      // 앨범 선택후 작성하러 들어오는 경우
      if (location.state.postContent === undefined) {
        setAlbumPost(location.state);
        //setStars(scoreToStar(0));
        console.log('from home');
      }
      // 수정하러 들어오는 경우
      else {
        setIsEditMode(true);
        setPostContent(location.state.postContent);
        delete location.state.postContent;
        setAlbumPost(location.state);
        console.log(location.state.score);
        setScore(location.state.score);
        //setStars(scoreToStar(location.state.score));
      }
    }
  }, [location.state]);

  const navigate = useNavigate();
  const GoToSearchPage = () => {
    if (!albumPost) navigate('/SearchPage');
  };
  const GoToHomePage = () => {
    navigate('/Home');
  };
  const GoToAlbumPostPageAfterEdit = () => {
    // navigate('/AlbumPostPage', { state: postId });
    navigate(-1);
  };
  const GoToAlbumPostPage = (postId: string) => {
    navigate('/AlbumPostPage', { state: postId });
  };

  // 게시물 작성
  const fetchPost = async (token: string, refreshToken: string) => {
    const data = {
      content: postContent,
      title: albumPost?.name,
      spotifyAlbumId: albumPost?.albumId,
      score: score,
    };
    await fetchPOST(token, refreshToken, `/api/album/post`, data, MAX_REISSUE_COUNT).then(data => GoToAlbumPostPage(data.postDetail.postId));
  };

  // 게시물 수정
  const fetchEdit = async (token: string, refreshToken: string) => {
    const data = {
      content: postContent,
      score: score,
    };
    fetchPOST(token, refreshToken, `/api/album/post/${albumPost?.postId}`, data, MAX_REISSUE_COUNT).then(() => GoToAlbumPostPageAfterEdit());
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
            <AlbumInfoArea>
              <TitleTextArea>
                <Text fontFamily="EB" fontSize="30px" margin="0px" color={colors.BG_white}>
                  {albumPost.name}
                </Text>
                <Text fontFamily="RG" fontSize="20px" margin="0px 0px 2px 10px" color={colors.BG_white}>
                  {albumPost.albumArtist.name}
                </Text>
              </TitleTextArea>
            </AlbumInfoArea>
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
              <Text fontFamily="EB" fontSize="30px" margin="0px" color="white">
                앨범을 선택해주세요
              </Text>
            </TitleTextArea>
          </AlbumTitleArea>
        )}
        <ButtonArea>
          <Text
            fontFamily="RG"
            fontSize="15px"
            margin="0px 0px 0px 10px"
            color={colors.Font_black}
            onClick={() => {
              isEditMode && albumPost ? GoToAlbumPostPageAfterEdit() : GoToHomePage();
            }}
          >
            취소
          </Text>
          <Text fontFamily="EB" fontSize="20px" margin="0px" color={colors.Font_black}>
            Album Post
          </Text>
          <Text
            fontFamily="RG"
            fontSize="15px"
            margin="0px 10px 0px 0px"
            color={colors.Font_black}
            onClick={() => {
              isEditMode
                ? fetchEdit(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '')
                : fetchPost(localStorage.getItem('login-token') || '', localStorage.getItem('login-refreshToken') || '');
            }}
          >
            저장
          </Text>
        </ButtonArea>

        <Line></Line>
        <ScoreEdit stars={stars || []} score={score} setScore={setScore} setStars={setStars} />
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
        </PostArea>
      </AlbumPostArea>
    </Container>
  );
}

export default AlbumPostEditPage;
