import styled from 'styled-components';
import { colors } from '../../styles/color';
import Nav from '../components/Nav';
import AlbumPageChatTab from '../components/AlbumPageChatTab';
import AlbumPagePostTab from '../components/AlbumPagePostTab';
import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Loader from '../components/Loader';
import useStore from '../store/store';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  overflow-y: scroll;
  overflow-x: hidden;
  height: 100vh;
  width: 100vw;
  background-color: white;
  color: black;
`;

const Header = styled.div`
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Body = styled.div`
  margin-top: 120px;
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
`;

const CommentCard = styled.div`
  position: relative;
  display: flex;
  width: 100vw;
  max-width: 600px;
  aspect-ratio: 1 / 1; /* 1:1 비율 유지 */
  border-radius: 0px;
  background: linear-gradient(90deg, #6a85b6, #bac8e0);
  /* box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px; */
  overflow: hidden;
`;

const ImageArea = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
  box-sizing: border-box;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const GradientBG = styled.div`
  position: absolute;
  top: 0px;
  left: 0px;
  z-index: 2;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(0deg, rgba(255, 255, 255, 1) 0%, rgba(255, 255, 255, 0) 80%);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
`;

const CoverImageArea = styled.div`
  position: relative;
  /* top: 0px;
  left: 0px; */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow: hidden;

  box-sizing: border-box;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 3;
`;

const CoverImage = styled.div`
  /* position: absolute;
  top: 0px;
  left: 0px; */
  overflow: hidden;
  width: 200px;
  height: 200px;
  object-fit: cover;
  z-index: 4;
  box-shadow: rgba(0, 0, 0, 0.2) 0px 4px 12px;
`;

const AlbumInfoArea = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 0px 20px 20px 20px;
  box-sizing: border-box;
  z-index: 3;
`;

const AlbumTitleArea = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  height: auto;
`;

const LikeNumberArea = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: center;
  width: 100%;
  height: 40px;
`;

const ContentArea = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 500px;
  box-sizing: border-box;
  padding: 0px 20px 0px 20px;
`;

const ContentInfoArea = styled.div`
  display: flex;
  justify-content: flex-start;
  box-sizing: border-box;
  padding: 0px 0px 20px 30px;
  align-items: center;
  flex-direction: row;
  width: 100vw;
  height: 50px;
  gap: 5px;
  font-size: 20px;
`;

const Line = styled.div`
  width: 100%;
  max-width: 500px;
  box-sizing: border-box;
  padding: 0px 20px 0px 20px;
  /* width: 320px; */
  height: 1px;
  background-color: ${colors.Button_deactive};
`;
const Text = styled.div<{ fontSize?: string; margin?: string; fontFamily?: string; opacity?: number; color?: string; width?: string }>`
  font-size: ${props => props.fontSize};
  margin: ${props => props.margin};
  font-family: ${props => props.fontFamily};
  opacity: ${props => props.opacity};
  color: ${props => props.color};
  width: ${props => props.width};

  //white-space: nowrap;
  overflow: hidden; // 너비를 넘어가면 안보이게
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  text-align: center;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const TabArea = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 350px;
  margin-bottom: 10px;
`;
const TabBtn = styled.div<{ opacity?: string }>`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-family: 'Bd';
  width: 170px;
  height: 30px;
  border-radius: 5px;

  font-size: 20px;

  color: ${props => props.color};
  opacity: ${props => props.opacity};
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

interface AlbumDetail {
  albumId: number;
  title: string;
  albumCover: string;
  artistName: string;
  genre: string;
  spotifyId: string;
  likes: User[]; // 앨범을 좋아요한 사용자 리스트
}

function AlbumPage() {
  const location = useLocation();
  const spotifyAlbumId = location.state;

  const [albumDetail, setAlbumDetail] = useState<AlbumDetail>();
  const [isLoading, setIsLoading] = useState(false);
  const [tabState, setTabState] = useState('chat');

  const server = 'http://203.255.81.70:8030';
  let AlbumPageUrl = `${server}/api/album/${spotifyAlbumId}`;

  const reissueTokenUrl = `${server}/api/auth/reissued`;
  const [token, setToken] = useState(localStorage.getItem('login-token'));
  const [refreshToken, setRefreshToken] = useState(localStorage.getItem('login-refreshToken'));

  useEffect(() => {
    fetchSearch();
  }, []);

  const fetchSearch = async () => {
    if (token && !isLoading) {
      setIsLoading(true);
      console.log('검색시작');
      Search(AlbumPageUrl);
    }
  };
  const Search = async (URL: string) => {
    console.log('fetching album chat list...');
    try {
      const response = await fetch(URL, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setAlbumDetail(data);
        console.log(data);
      } else if (response.status === 401) {
        ReissueToken();
        fetchSearch();
      } else {
        console.error('Failed to fetch data:', response.status);
      }
    } catch (error) {
      console.error('Error fetching the JSON file:', error);
    } finally {
      setIsLoading(false);
      console.log('finished');
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
        throw new Error(`failed to reissue token : ${response.status}`);
      }
    } catch (error) {
      console.error(error);
    }
  };

  //

  // Post 좋아요 상태 확인

  // 좋아요 설정
  const [isAlbumLiked, setIsAlbumLiked] = useState(false);
  const [likesCount, setLikesCount] = useState<number>(0);
  const { id, name } = useStore();
  useEffect(() => {
    if (albumDetail) {
      setLikesCount(albumDetail.likes.length);
      if (albumDetail.likes.some((like: any) => like.id === id)) {
        setIsAlbumLiked(true);
      }
    }
  }, [albumDetail]);

  const AlbumLikeUrl = server + `/api/album/${spotifyAlbumId}/like`;

  const changeAlbumLike = async () => {
    console.log('changing Like');
    if (isAlbumLiked && albumDetail) {
      // 이미 좋아요를 눌렀다면 좋아요 취소
      setIsAlbumLiked(false);
      setLikesCount(likesCount - 1);
      albumDetail.likes = albumDetail.likes.filter((like: any) => like.id !== id);
    } else {
      // 좋아요 누르기
      setIsAlbumLiked(true);
      setLikesCount(likesCount + 1);
      albumDetail?.likes.push({
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
        const response = await fetch(AlbumLikeUrl, {
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
          ReissueToken();
          fetchLike();
        } else {
          console.error('Failed to fetch data:', response.status);
        }
      } catch (error) {
        console.error('like 실패:', error);
      }
    }
  };

  //
  const navigate = useNavigate();
  const GoToAlbumChatPostPage = () => {
    navigate('/AlbumChatPostPage', { state: spotifyAlbumId });
  };
  const albumPostPreset = {
    albumArtist: {
      artistId: '',
      imageUrl: '',
      name: albumDetail?.artistName,
    },
    albumId: spotifyAlbumId,
    imageUrl: albumDetail?.albumCover,
    name: albumDetail?.title,
    total_tracks: 0,
    release_date: '',
    postId: 0,
  };
  const GoToAlbumPostEditPage = () => {
    navigate('/AlbumPostEditPage', { state: albumPostPreset });
  };

  return (
    <Container>
      <Header>
        <Nav page={3}></Nav>
      </Header>
      <Body>
        {!isLoading && (
          <>
            <CommentCard>
              <ImageArea>
                <img src={albumDetail?.albumCover} width="120%" height="120%" object-fit="cover"></img>
              </ImageArea>
              <GradientBG> </GradientBG>
              <CoverImageArea>
                <CoverImage>
                  <img src={albumDetail?.albumCover} width="100%" height="100%" object-fit="cover"></img>
                </CoverImage>
              </CoverImageArea>
            </CommentCard>
            <AlbumInfoArea>
              <AlbumTitleArea>
                <Text width="300px" fontSize="30px" margin="0px 0px 5px 0px" fontFamily="Bd" color={colors.Font_black}>
                  {albumDetail?.title}
                </Text>
                <Text fontSize="20px" margin="0px 0px 5px 0px" fontFamily="Bd" opacity={0.6} color={colors.Font_black}>
                  {albumDetail?.artistName}
                </Text>
                <Text fontSize="12px" margin="0px" fontFamily="Bd" opacity={0.5} color={colors.Font_black}>
                  {albumDetail?.genre}
                </Text>
              </AlbumTitleArea>
              <LikeNumberArea>
                <svg
                  onClick={() => changeAlbumLike()}
                  xmlns="http://www.w3.org/2000/svg"
                  width="14"
                  height="14"
                  fill={isAlbumLiked ? colors.Button_active : colors.Button_deactive}
                  className="bi bi-heart-fill"
                  viewBox="0 0 16 16"
                >
                  <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314" />
                </svg>
                <Text fontSize="15px" margin="0px 0px 0px 10px" fontFamily="Bd" color={colors.Font_black}>
                  {albumDetail?.likes.length}
                </Text>
                <Text fontSize="15px" margin="0px 0px 0px 5px" fontFamily="Rg" color={colors.Font_black}>
                  likes
                </Text>
              </LikeNumberArea>
            </AlbumInfoArea>
            <Line></Line>
            <ContentArea>
              {/* <Title fontSize="30px" margin="0px 0px 0px 0px" >
                Chats
              </Title> */}
              <TabArea>
                <TabBtn
                  opacity={tabState === 'chat' ? '1' : '0.3'}
                  onClick={() => {
                    setTabState('chat');
                    console.log('chat');
                  }}
                >
                  CHAT
                </TabBtn>
                <TabBtn
                  opacity={tabState === 'post' ? '1' : '0.3'}
                  onClick={() => {
                    setTabState('post');
                    console.log('post');
                  }}
                >
                  POST
                </TabBtn>
              </TabArea>
              {tabState === 'chat' && albumDetail && (
                <>
                  <ContentInfoArea>
                    Chat 작성하기
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="20"
                      height="20"
                      fill="currentColor"
                      className="bi bi-pencil-square"
                      viewBox="0 0 16 16"
                      onClick={() => {
                        GoToAlbumChatPostPage();
                      }}
                    >
                      <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                      <path
                        fillRule="evenodd"
                        d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                      />
                    </svg>
                  </ContentInfoArea>
                  <AlbumPageChatTab spotifyAlbumId={spotifyAlbumId}></AlbumPageChatTab>
                </>
              )}
              {tabState === 'post' && (
                <>
                  <ContentInfoArea>
                    Post 작성하기
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="20"
                      height="20"
                      fill="currentColor"
                      className="bi bi-pencil-square"
                      viewBox="0 0 16 16"
                      onClick={() => {
                        GoToAlbumPostEditPage();
                      }}
                    >
                      <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z" />
                      <path
                        fillRule="evenodd"
                        d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"
                      />
                    </svg>
                  </ContentInfoArea>
                  <AlbumPagePostTab spotifyAlbumId={spotifyAlbumId}></AlbumPagePostTab>
                </>
              )}
            </ContentArea>
          </>
        )}
        {isLoading ? <Loader></Loader> : null}
      </Body>
    </Container>
  );
}

export default AlbumPage;
